<?php

include "db_connect.php";

$query = "SELECT * FROM lokasi ORDER BY id_lokasi ";
$hasil = MYSQLi_QUERY($connect,$query);

$response = array();

while ($data = MYSQLi_FETCH_ARRAY($hasil))
{
	$h['id_lokasi']    = $data['id_lokasi'];
	$h['nama_lokasi']  = $data['nama_lokasi'];
	$h['status']  = $data['status'];
	
	array_push($response, $h);
}
 echo json_encode($response);
?>