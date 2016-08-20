<?php

  define('WS_OP_CONTINUATION', 0X0);
  define('WS_OP_TEXT', 0x1);
  define('WS_FRAME_MIN_SIZE', 2);
  
  include 'connect.php';
  //ini_set('display_errors', 1);
  set_time_limit(20);
  if (ob_get_level() == 0) ob_start();
  
  function getCode() {
    $characters = 'ABCDEFGHKLMNPQRSTUVXYZ';
    $randstring = '';
    while(strlen($randstring) < 4) {
      $randstring .= substr($characters, rand(0, strlen($characters)), 1);
    }
    return $randstring;
  }
  
  //error_log("starting");
  
  $sock_main = socket_create(AF_INET, SOCK_STREAM, 0);
  socket_set_option($sock_main, SOL_SOCKET, SO_REUSEADDR, 1);
  socket_bind($sock_main, 0) or die('Could not bind to address');
  socket_getsockname($sock_main, $address, $port);
  //error_log("listening on ".$port);
  echo $port."\n";
  
  ob_flush();
  flush();
  
  socket_listen($sock_main);
  //error_log("waiting on client");
  
  $client_main = socket_accept($sock_main);
  $m_sock = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
  socket_set_option($m_sock, SOL_SOCKET, SO_REUSEADDR, 1);
  
  $cls = array($m_sock, $client_main);
  socket_bind($m_sock, 0);
  socket_getsockname($m_sock, $address, $port);
  
  $code = getCode();
  
  $sql  = "INSERT INTO `games` (code, address)";
  $sql .= " VALUES (\"".$code."\", \"".$port."\");";

  //error_log($sql);

  $conn->query($sql);  
  
  //error_log("sql done");
  
  socket_write($client_main, $code."\n");
  
  //LOOP
  
  socket_listen($m_sock, 40);
  //error_log("Starting server...");
  
  $writeResult = TRUE;
  
  do {
    usleep(4000);
    //error_log("loop");
    $changed = $cls;
    $val = @socket_select($changed,$write=NULL,$except=NULL,0);
    foreach ($changed as $sock) {
      if($sock === $m_sock) {
        //error_log("wait...");
        $msgsock = socket_accept($m_sock);
        array_push($cls, $msgsock);
        //error_log("Connected...");
        socket_recv($msgsock, $hds, 2048, null);
        if(preg_match("/Sec-WebSocket-Key: (.*)\r\n/",$hds,$matchs)) {
          //error_log("do handshake...");
          $key = $matchs[1] . '258EAFA5-E914-47DA-95CA-C5AB0DC85B11';
          $key =  base64_encode(sha1($key, true));
          $headers = "HTTP/1.1 101 Switching Protocols\r\n".
          "Upgrade: websocket\r\n".
          "Connection: Upgrade\r\n".
          "Sec-WebSocket-Accept: $key".
          "\r\n\r\n";
          socket_write($msgsock, $headers);
          //error_log("handshak done...");
        }
      }
      else {
        if($sock == $client_main) {
          $bytes = socket_recv($sock, $buffer, 8000, null);
          foreach(preg_split("/((\r?\n)|(\r\n?))/", $buffer) as $data) {
            if(strlen($data) > 1) {
              //error_log("APPLICATION: ".$data);
              foreach ($cls as $socket) {
                if($socket != $m_sock && $socket != $sock && $val > 0) {
                  if($d != "end") {
                    sendMessage($data, $socket);
                  }
                  else {
                    unset($cls[$socket]);
                    socket_close($cls[$socket]);
                  }
                }
              }
            }
          }
        }
        else {
          $bytes = socket_recv($sock, $buffer, 8000, null);
          $d = unmask($buffer);
          foreach(preg_split("/((\r?\n)|(\r\n?))/", $d) as $data) {
            if(strlen($data) > 1) {
              if(!endsWith($data, "WebSocket Protocol Error")) {
                //error_log("CLIENT: ".$d);
                $writeResult = socket_write($client_main, $d."\n");
              }
              else {
                unset($cls[$socket]);
                socket_close($cls[$socket]);
                break 3;
              }
            }
          }
        }
      }
    }
    //error_log("WriteResult: ".$writeResult);
  } while($writeResult !== FALSE);
  function unmask($payload) {
    $length = ord($payload[1]) & 127;
    if($length == 126) {
      $masks = substr($payload, 4, 4);
      $data = substr($payload, 8);
    }
    elseif($length == 127) {
      $masks = substr($payload, 10, 4);
      $data = substr($payload, 14);
    }
    else {
      $masks = substr($payload, 2, 4);
      $data = substr($payload, 6);
    }
    $text = '';
    for ($i = 0; $i < strlen($data); ++$i) {
      $text .= $data[$i] ^ $masks[$i%4];
    }
    return $text;
  }
  function encode($text) {
    // 0x1 text frame (FIN + opcode)
    $b1 = 0x80 | (0x1 & 0x0f);
    $length = strlen($text);
    if($length <= 125) 
      $header = pack('CC', $b1, $length);
    elseif($length > 125 && $length < 65536)
      $header = pack('CCS', $b1, 126, $length);
    elseif($length >= 65536)
      $header = pack('CCN', $b1, 127, $length);
    return $header.$text;
  }
  function endswith($string, $test) {
    $strlen = strlen($string);
    $testlen = strlen($test);
    if ($testlen > $strlen) return false;
    return substr_compare($string, $test, $strlen - $testlen, $testlen) === 0;
  }
  function sendMessage($message, $client, $opcode = WS_OP_TEXT) {
    $fin_rrr_opcode = $opcode | 0x80;
    $length = strlen($message);
    $header_length = WS_FRAME_MIN_SIZE;
    $data = pack('C', $fin_rrr_opcode);
    // check if we need to include extended length
    if ($length > 125 && $length <= 0xffff) {
      $header_length += 2;
      $data .= pack('Cn', 126, $length);
    }
    elseif ($length > 0xffff) {
      $header_length += 8;
      $data .= pack('CNN', 127, 0, $length);
    }
    else {
      $data .= pack('C', $length);
    }
    //$message = utf8_encode($message);
    $data = $data . $message;
    $sent = socket_send($client, $data, $length + $header_length, 0);
    if ($sent === false) {
      $this->debug("Failed to send message");
      return -1;
    }
  }
  
  $sql = "DELETE FROM `games` WHERE code='".$code."'";
  $conn->query($sql);
  
  $conn->close();
  socket_close($m_sock);
  socket_close($client_main);
  socket_close($sock_main);
?>
