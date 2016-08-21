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
    $name = substr(urldecode($_GET['name']), 0, 40);
    $sql  = "SELECT uid FROM `game_names` WHERE name like ? AND code=?;";

    //error_log($sql);
    
    $stmt = $pdo->prepare($sql);
    $stmt->execute(array($name, $code));
    
    while ($row = $stmt->fetch()) {
      $id = (int)$row['uid'];
      
      if($id !== 0) { 
        echo $id."\n";
        ob_flush();
        flush();
        
        $sql  = "DELETE FROM `game_names` WHERE name=? AND code=?;";

        $stmt = $pdo->prepare($sql);
        $stmt->execute(array($name, $code));
        
        exit;
      }
    }
    exit;
  }
    
  $sql  = "SELECT id,message FROM `game_messages` WHERE uid=? AND code=?;";

  $stmt = $pdo->prepare($sql);
  $stmt->execute(array($id, $code));
  
  while ($row = $stmt->fetch()) {
    $message = $row['message'];
    $mid = $row['id'];
    
    echo $message."\n";
    ob_flush();
    flush();
    
    $sql  = "DELETE FROM `game_messages` WHERE id=?;";

    $stmt2 = $pdo->prepare($sql);
    $stmt2->execute(array($mid));
  }
  
  exit;
?>
