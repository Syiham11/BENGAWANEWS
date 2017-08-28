<?php 
$con = mysqli_connect("mysql.idhostinger.com","u826911054_ews","u826911054","u826911054_uno");

$sql = "SELECT lokasi.id_lokasi,nama_lokasi,longitude,lattitude,history.ketinggian_air,history.date_time FROM lokasi JOIN history ON lokasi.id_lokasi=history.id_lokasi WHERE date_time IN(SELECT MAX(date_time) FROM lokasi JOIN history ON lokasi.id_lokasi=history.id_lokasi GROUP BY lokasi.nama_lokasi)";

$query = mysqli_query($con, $sql) OR die(mysqli_connect_error());
	$json = array();
	while($row = mysqli_fetch_assoc($query)){
		$json[] = $row;
	}
	echo json_encode($json);
	mysqli_close($con);
?>