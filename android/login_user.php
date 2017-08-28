<?php

include "db_connect.php";

$email = $_GET["email"];

$query = "SELECT * FROM users WHERE email='$email' ";
$hasil =MYSQLi_QUERY($connect,$query);

$response = array();
$response["users"] = array();

if (MYSQLi_NUM_ROWS($hasil) > 0) {
	while ($data = MYSQLi_FETCH_ARRAY($hasil))
	{

		$h['nama']    	= $data['nama'];
		$h['email']    	= $data['email'];
		$h['uid']    	= $data['uid'];
		$h['token']    = $data['token'];

		array_push($response["users"], $h);
	}

	$response["success"] = 1;
	echo json_encode($response);
}
else {

	$response["success"] = 0;
	$response["message"] = "Tidak ada data";
	echo json_encode($response);
}

?>