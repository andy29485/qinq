<?php
  include 'connect.php';
  if(!isset($_GET['code'])) {
    echo 0;
    exit;
  }


  $code = $_GET['code'];

  $sql = "SELECT address FROM `games` WHERE code='".$code."'";
  $result = $conn->query($sql);
  $address = $result->fetch_assoc();
  $port = (int)$address['address'];
  echo $port;
?>
