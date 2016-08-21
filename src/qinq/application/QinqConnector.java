package qinq.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import qinq.resource.Game;
import qinq.resource.Player;
import qinq.resource.QinqWebSocketAddapter;

public class QinqConnector {
  public static final String REMOTE_SERVER_DEFAULT = "az.heliohost.org/qinq";
  private String             strRemoteHost;
  private String             strCode;
  private Game               game;
  private int                nType;
  private Socket             sock;
  private BufferedWriter     bw;
  private BufferedReader     br;

  public QinqConnector(Game game) {
    this(QinqConnector.REMOTE_SERVER_DEFAULT, game);
  }

  public QinqConnector(String strRemoteHost, Game game) {
    this.strRemoteHost = strRemoteHost;
    this.strCode = "";
    this.nType = 0;
    this.game = game;

    if (!this.strRemoteHost.startsWith("html://"))
      this.strRemoteHost = "http://" + this.strRemoteHost;
    if (!this.strRemoteHost.endsWith("/"))
      this.strRemoteHost = this.strRemoteHost + "/";
  }

  public String start(int nPort) {
    try {
      // make http request to start server code
      System.out.println("starting server");

      URL url = new URL(this.strRemoteHost + "creategame.php");
      System.setProperty("java.net.preferIPv4Stack", "true");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.connect();
      InputStream is = conn.getInputStream();
      this.br = new BufferedReader(new InputStreamReader(is));

      switch (br.readLine()) {
        case "socket":
          System.out.println("socket");
          this.nType = 1;
          int nRemotePort = Integer.valueOf(br.readLine());

          // connect and send port
          System.out.println("connecting");
          this.sock = new Socket(url.getHost(), nRemotePort);
          this.bw = new BufferedWriter(
              new OutputStreamWriter(this.sock.getOutputStream()));

          // get code
          System.out.println("reading code");
          this.br = new BufferedReader(
              new InputStreamReader(this.sock.getInputStream()));
          this.strCode = br.readLine();

          new Thread(() -> socket_run()).start();
          break;
        case "poll":
          System.out.println("poll");
          this.nType = 2;

          // get code
          System.out.println("reading code");
          this.strCode = this.br.readLine();
          this.br = null;

          new Thread(() -> poll_run()).start();
          break;
        default:
          System.out.println("unable to connect");
          this.br = null;
          return this.strCode = "";

      }

    }
    catch (IOException e) {
      this.stop();
      return this.strCode = "";
    }
    return String.format("%s?code=%s", this.strRemoteHost, this.strCode);
  }

  public void stop() {
    this.nType = 0;
    if (this.strCode != null && !this.strCode.isEmpty()) {
      try {
        URL url =
            new URL(this.strRemoteHost + "endgame.php?code=" + this.strCode);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        conn.getInputStream();

        // close writer
        this.strCode = "";
      }
      catch (IOException e) {
      }
    }
    // close socket
    try {
      if (this.bw != null) {
        this.bw.write("end");
        this.bw.flush();
        this.bw.close();
      }
      if (this.br != null)
        this.br.close();
      if (this.sock != null)
        this.sock.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void socket_run() {
    String message;
    try {
      while ((message = br.readLine()) != null && this.nType == 1) {
        try {
          if (message.isEmpty())
            continue;
          JSONObject json = new JSONObject(message);
          if (json.has("uid")) {
            int id = json.getInt("uid");
            Player p;
            if ((p = QinqConnector.this.game.getPlayerById(id)) != null) {
              p.getSocket().onMessage(json);
            }
          }
          else if (json.has("action")
              && json.getString("action").equalsIgnoreCase("create user")) {
            QinqWebSocketAddapter socket =
                new QinqWebSocketAddapter(game, QinqConnector.this);
            socket.onMessage(json);
          }
        }
        catch (JSONException e) {
          System.out.println("Bad JSON: " + message);
        }
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (RuntimeException e) {
      return;
    }
  }

  private void poll_run() {
    try {
      while (this.nType == 2) {
        URL url = new URL(String.format("%sgetinfo.php?code=%s&id=0",
            this.strRemoteHost, this.strCode));

        System.setProperty("java.net.preferIPv4Stack", "true");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        this.br = new BufferedReader(new InputStreamReader(is));
        socket_run();
        this.br.close();
        Thread.sleep(200);
      }
    }
    catch (MalformedURLException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void send(String message) {
    if (this.nType == 1) {
      try {
        this.bw.write(message + "\n");
        this.bw.flush();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    else if (this.nType == 2) {
      try {
        JSONObject json = null;
        try {
          json = new JSONObject(message);
        }
        catch (JSONException e) {
        }
        message = "message=" + encodeURIComponent(message);

        int id = json != null && json.has("uid") ? json.getInt("uid") : 0;

        String strUrl = String.format("%ssenddata.php?code=%s&id=%d",
            strRemoteHost, this.strCode, id);

        if (json != null && json.has("action")
            && json.getString("action").equalsIgnoreCase("creating")) {
          strUrl += "&name=" + encodeURIComponent(json.getString("name"));
        }
        URL url = new URL(strUrl);
        // http://192.168.7.3/qinq/seddata.php?code=VXLV&id=0&name=AZ

        System.setProperty("java.net.preferIPv4Stack", "true");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setFixedLengthStreamingMode(message.getBytes().length);
        conn.setRequestProperty("Content-Type",
            "application/x-www-form-urlencoded");
        java.io.BufferedOutputStream out =
            new java.io.BufferedOutputStream(conn.getOutputStream());
        PrintStream pstream = new PrintStream(out);
        pstream.print(message);
        pstream.close();
      }
      catch (MalformedURLException e) {
        e.printStackTrace();
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public static String encodeURIComponent(String s) {
    String result;

    try {
      result = URLEncoder.encode(s, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      result = s;
    }

    return result;
  }
}
