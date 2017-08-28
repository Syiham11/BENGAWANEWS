<?php

$response = array();
 
if (isset($_POST['uid']) && isset($_POST['nama'])) {

    $email = $_POST['email'];
    $nama = $_POST['nama'];
    $uid = $_POST['uid'];
    $token = $_POST['token'];
   
    include 'db_connect.php';
 
    $query = "INSERT INTO users(email,nama,uid,token) values ('$email','$nama','$uid','$token')";
    $result= MySQLi_QUERY($connect, $query);
 
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Successfully Created";
 
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>