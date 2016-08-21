<?php
  include 'connect.php';
  
  function getCode() {
    require 'connect.php';
    
    $characters = 'ABCDEFGHKLMNPQRSTUVXYZ';
    do {
      $randstring = '';
      while(strlen($randstring) < 4) {
        $randstring .= substr($characters, rand(0, strlen($characters)), 1);
      }
      $sql = "SELECT count(*) FROM `games` WHERE code='{$randstring}'";
      $count = $pdo->query($sql)->fetchColumn();
    } while($count > 0);
    return $randstring;
  }
  
  $code = getCode();
  
  if (ob_get_level() == 0) ob_start();
  
  echo "poll\n";
  echo $code."\n";
  
  ob_flush();
  flush();
  
  $sql  = "INSERT INTO `games` (code, port)";
  $sql .= " VALUES (\"".$code."\", \"0\");";

  //error_log($sql);

  $pdo->query($sql);
?>
