<?php
  $username = "qinq";
  $password = "0XwfpyRI58mvJ6a6";
  $hostname = "localhost";
  $database = "qinq";
  $charset  = "utf8";

  $dsn = "mysql:host={$hostname};dbname={$database};charset={$charset}";
  $opt = [
      PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
      PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
      PDO::ATTR_EMULATE_PREPARES   => false,
  ];
  $pdo = new PDO($dsn, $username, $password, $opt);
  
  /*
  $sql = "CREATE TABLE IF NOT EXISTS `games` (";
  $sql .= "`id` int(11) NOT NULL AUTO_INCREMENT,";
  $sql .= "`code` varchar(4) NOT NULL,";
  $sql .= "`port` varchar(5) NOT NULL,";
  $sql .= "PRIMARY KEY (`id`),";
  $sql .= "UNIQUE KEY `game_code_uniq` (`code`)";
  $sql .= ") AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";
  $stmt = $pdo->query($sql);
  */
?>
