<?php
  $username = "qinq";
  $password = "0XwfpyRI58mvJ6a6";
  $hostname = "localhost"; 

  //connection to the database
  $conn = new mysqli($hostname, $username, $password, 'qinq');
  
  $sql = "CREATE TABLE IF NOT EXISTS `games` (";
  $sql .= "`id` int(11) NOT NULL AUTO_INCREMENT,";
  $sql .= "`code` varchar(4) NOT NULL,";
  $sql .= "`port` varchar(5) NOT NULL,";
  $sql .= "PRIMARY KEY (`id`),";
  $sql .= "UNIQUE KEY `game_code_uniq` (`code`)";
  $sql .= ") AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;\n";
  
  $sql .= "CREATE TABLE `game_messages` (";
  $sql .= "`id` int(11) NOT NULL,";
  $sql .= "`code` varchar(4) NOT NULL,";
  $sql .= "`uid` int(11) NOT NULL,";
  $sql .= "`message` longtext NOT NULL,";
  $sql .= "PRIMARY KEY (`id`)";
  $sql .= ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";

  $sql .= "CREATE TABLE `game_names` (";
  $sql .= "`id` int(11) NOT NULL,";
  $sql .= "`uid` int(11) NOT NULL,";
  $sql .= "`code` varchar(4) NOT NULL,";
  $sql .= "`name` varchar(40) NOT NULL,";
  $sql .= "PRIMARY KEY (`id`)";
  $sql .= ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";


  $conn->query($sql);
?>
