<?php
  include 'connect.php';
  if(!isset($_GET['code'])) {
    echo 0;
    exit;
  }


  $code = $_GET['code'];

  $sql = "SELECT por FROM `games` WHERE code=?";
  if($result->num_rows !== 1) {
    echo 0;
    exit;
  }
  
  $stmt = $pdo->prepare($sql);
  $stmt->execute(array($code));
  
  $row = $stmt->fetch());
  $port = (int)$row['port'];
  
  echo $port."\n";
?>
