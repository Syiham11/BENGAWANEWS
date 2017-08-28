<?php

$response = array();
 
if (isset($_POST['email'])) {

    $email = $_POST['email'];
    $id_lokasi = $_POST['id_lokasi'];
    //$status = $_POST['status'];
    //$id_tampungan = $_POST['id_tampungan'];  
    include 'db_connect.php';
 
 //nek ngecek email e enek ra lagi update id_tampungan;


    $query1 = "SELECT * FROM tampungan where email='$email' and id_lokasi='$id_lokasi'";
    $result1= MySQLi_QUERY($connect, $query1);

    if(MYSQLi_NUM_ROWS($result1) > 0){
      
    }

    else{
        $query2 = "INSERT INTO tampungan(email,id_lokasi) values ('$email','$id_lokasi')";
        $result2= MySQLi_QUERY($connect, $query2);
        
    }    
   
    if ($result2) {
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