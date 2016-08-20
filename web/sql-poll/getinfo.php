<?php
  include 'connect.php';
  
  if(isset($_GET['code'])) {
    $code = $_GET['code'];
  }
  else {
    exit;
  }
  $id = (isset($_GET['id']) ? $_GET['id'] : '0');
  
  if (ob_get_level() == 0) ob_start();
  
  if(isset($_GET['name'])) {
    $name = urldecode($_GET['name']);
    $sql  = "SELECT uid FROM `game_names` WHERE name=? AND code=?;";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param('ss', substr($name, 0, 40), $code);
    
    $stmt->execute();
    $result = $stmt->get_result();
    
    while ($row = $result->fetch_assoc()) {
      $id = (int)$row['uid'];
      
      if($id !== 0) { 
        echo $id."\n";
        ob_flush();
        flush();
        
        $sql  = "DELETE FROM `game_names` WHERE name=? AND code=?;";

        $stmt = $conn->prepare($sql);
        $stmt->bind_param('ss', substr($name, 0, 40), $code);
        
        $stmt->execute();
        exit;
      }
    }
    
    $sql  = "SELECT message FROM `game_messages` WHERE uid=? AND code=?;";

    $stmt = $conn->prepare($sql);
    $stmt->bind_param('is', $id, $code);
    
    $stmt->execute();
    $result = $stmt->get_result();
    
    while ($row = $result->fetch_assoc()) {
      $message = $row['message'];
      
      echo $message."\n";
      ob_flush();
      flush();
      
      $sql  = "DELETE FROM `game_messages` WHERE uid=? AND ";
      $sql .= "message=? AND code=?;";

      $stmt = $conn->prepare($sql);
      $stmt->bind_param('iss', $id, $message, $code);
      
      $stmt->execute();
    }
    
    exit;
  }
?>
