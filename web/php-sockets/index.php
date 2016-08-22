<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="css/styles.css" />
    <link rel="shortcut icon" href="images/qinq.ico" />
    <title>QINQ</title>
  </head>
  <body onload="onLoad()">
    <script src="js/script.js"></script>
  <header id="header">
      <div id="timer">30</div>
      <div id="name"></div>
      <div id="score"></div>
      <h1>QINQ</h1>
    </header>
    <div id="welcome">
      <p style="float:right;margin-right:30px;margin-top:-41px;">
        Get the latest version of the Game Server here: 
        <a href="https://github.com/Andy29485/qinq/releases/latest">
          <img src="http://img.shields.io/github/release/Andy29485/qinq.svg" >
        </a>
      </p>
      <form style="clear:both;">
        <p>
          <label for="game-code">Game Code:</label>
          <input type="text" name="game-code" id="code-field"
            <?php 
              if(isset($_GET['code'])) {
                $code = urldecode($_GET['code']);
                echo "value=\"{$code}\"";
              }
            ?>
          >
        </p>
        <p>
          <label for="player-name">Name:</label>
          <input type="text" name="player-name" id="name-field"
            <?php 
              if(isset($_GET['name'])) {
                $name = urldecode($_GET['name']);
                echo "value=\"{$name}\"";
              }
            ?>
          >
        </p>
      </form>
      <span id="name-error">Name in use</span>
      <button onclick="createPlayer()">Join Game</button>
      <button onclick="spectate()">Spectate</button>
    </div>
    <div id="question-box">
      <div id="answer-question">TODO</div>
      <form>
        <input type="text" name="answer" id="answer-field">
      </form>
      <button onclick="submitAnswer()">Submit</button>
    </div>
    <div id="vote-box">
      <div id="votes-left-div">
        votes left: <span id="votes-left-span">TODO</span>
      </div>
      <div id="vote-question">TODO</div>
      <section id="answers">
        <!-- place holder -->
      </section>
    </div>
    <div id="results-box">
      <!-- place holder -->
    </div>
  </body>
</html>
