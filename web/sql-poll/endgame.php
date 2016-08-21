<?php
  include 'connect.php';
  ini_set('display_errors', 1);

  if(isset($_GET['code'])) {
    $code = $_GET['code'];
    $code = preg_replace("/[^A-Za-z]/", "", $code);
  }
  else {
    exit;
  }
  
  $sql = "DELETE FROM `games` WHERE code='".$code."'";
  $pdo->query($sql);
  
  $sql = "DELETE FROM `game_names` WHERE code='".$code."'";
  $pdo->query($sql);
  
  $sql = "DELETE FROM `game_messages` WHERE code='".$code."'";
  $pdo->query($sql);
?>
