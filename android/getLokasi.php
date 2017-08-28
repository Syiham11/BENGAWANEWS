<?php

include "db_connect.php";


$query = "SELECT * FROM lokasi ORDER BY id_lokasi ";
$hasil = MYSQLi_QUERY($connect,$query);
$response = array();
$response["lokasi"] = array();

while ($data = MYSQLi_FETCH_ARRAY($hasil))
{
	$lokasi = array();
	$lokasi['id_lokasi']    = $data['id_lokasi'];
	$lokasi['nama_lokasi']  = $data['nama_lokasi'];
	
	array_push($response["lokasi"], $lokasi);
}

$response["success"] = 1;

echo json_encode($response);
?>