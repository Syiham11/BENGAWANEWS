<?php

$response = array();
 
if (isset($_POST['id_lokasi'])) {

    $id_lokasi = $_POST['id_lokasi'];
    $email = $_POST['email'];

    include 'db_connect.php';


    $query = "UPDATE users SET id_lokasi='$id_lokasi' where email='$email'";
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