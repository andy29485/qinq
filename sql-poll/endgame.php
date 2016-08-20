<?php
  include 'connect.php';
  ini_set('display_errors', 1);
  set_time_limit(20);

  if(isset($_GET['code'])) {
    $code = $_GET['code'];
    $code = preg_replace("/[^A-Za-z]/", "", $code);
  }
  else {
    exit;
  }
  
  $sql = "DELETE FROM `games` WHERE code='".$code."'";
  $conn->query($sql); 
  
  $conn->close();
?>
