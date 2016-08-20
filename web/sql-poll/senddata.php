<?php
  include 'connect.php';
  
  if(isset($_GET['code']) && isset($_POST['message'])) {
    $code = $_GET['code'];
    $message = urldecode($_POST['message']);
  }
  else {
    exit;
  }
  $id = isset($_GET['id']) ? $_GET['id'] : '0';
    
  $sql  = "INSERT INTO `game_messages` (code, uid, message)";
  $sql .= " VALUES (?, ?, ?);";
  
  $stmt = $pdo->prepare($sql);
  $stmt->execute(array($code, $id, $message));
  
  if(isset($_GET['name']) && $id !== 0) {
    $name = substr($_GET['name'], 0, 40);
    $sql  = "INSERT INTO `game_names` (code, uid, name)";
    $sql .= " VALUES (?, ?, ?);";

    $stmt = $pdo->prepare($sql);
    $stmt->execute(array($code, $id, $name));
  }
?>
