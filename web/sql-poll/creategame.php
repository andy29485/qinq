<?php
  include 'connect.php';
  
  $code = getCode();
  
  if (ob_get_level() == 0) ob_start();
  
  echo "poll\n";
  echo $code."\n";
  
  ob_flush();
  flush();
  
  $sql  = "INSERT INTO `game_names` (code, port)";
  $sql .= " VALUES (\"".$code."\", \"0\");";

  //error_log($sql);

  $conn->query($sql); 
?>
