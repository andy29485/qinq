<?php
  include 'connect.php';
  
  if(isset($_GET['code']) && $_POST['message']) {
    $code = $_GET['code'];
    $message = $_POST['message'];
  }
  else {
    exit;
  }
  $id = (isset($_GET['id']) ? $_GET['id'] : '0');
  

  $sql  = "INSERT INTO `game_messages` (code, uid, message)";
  $sql .= " VALUES (?, ?, ?);";

  $stmt = $conn->prepare($sql);
  $stmt->bind_param('sis', $code), $id, urldecode($message));
  
  $stmt->execute();
  
  if(isset($_GET['name'])) {
    $name = $_GET['name'];
    $sql  = "INSERT INTO `game_names` (uid, code, name)";
    $sql .= " VALUES (?, ?, ?);";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param('iss', $id, $code, substr($name, 0, 40));
    
    $stmt->execute();
  }
?>
