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
  $sql  = "CREATE TABLE IF NOT EXISTS `games` (";
  $sql .= "`id` int(11) NOT NULL AUTO_INCREMENT,";
  $sql .= "`code` varchar(4) NOT NULL,";
  $sql .= "`port` varchar(5) NOT NULL,";
  $sql .= "PRIMARY KEY (`id`),";
  $sql .= "UNIQUE KEY `game_code_uniq` (`code`)";
  $sql .= ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;\n";
  $stmt = $pdo->query($sql);
  
  $sql  = "CREATE TABLE IF NOT EXISTS `game_messages` (";
  $sql .= "`id` int(11) NOT NULL AUTO_INCREMENT,";
  $sql .= "`code` varchar(4) NOT NULL,";
  $sql .= "`uid` int(11) NOT NULL,";
  $sql .= "`message` longtext NOT NULL,";
  $sql .= "PRIMARY KEY (`id`)";
  $sql .= ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;\n";
  $stmt = $pdo->query($sql);

  $sql  = "CREATE TABLE IF NOT EXISTS `game_names` (";
  $sql .= "`id` int(11) NOT NULL AUTO_INCREMENT,";
  $sql .= "`uid` int(11) NOT NULL,";
  $sql .= "`code` varchar(4) NOT NULL,";
  $sql .= "`name` varchar(40) NOT NULL,";
  $sql .= "PRIMARY KEY (`id`)";
  $sql .= ") ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;\n";
  $stmt = $pdo->query($sql);
  */
?>
