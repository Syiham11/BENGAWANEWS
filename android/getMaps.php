<?php

include "db_connect.php";

$query = "SELECT lokasi.id_lokasi,nama_lokasi,longitude,lattitude,tingkat_awas,tingkat_waspada,tingkat_siaga,history.ketinggian_air,history.date_time FROM lokasi JOIN history ON lokasi.id_lokasi=history.id_lokasi WHERE date_time IN(SELECT MAX(date_time) FROM lokasi JOIN history ON lokasi.id_lokasi=history.id_lokasi GROUP BY lokasi.nama_lokasi)";
$hasil = MYSQLi_QUERY($connect,$query);
$response = array();

while ($data = MYSQLi_FETCH_ARRAY($hasil))
{
	$lokasi['id_lokasi']    = $data['id_lokasi'];
	$lokasi['nama_lokasi']  = $data['nama_lokasi'];
	$lokasi['longitude']  = $data['longitude'];
	$lokasi['lattitude']  = $data['lattitude'];
	$lokasi['tingkat_awas']  = $data['tingkat_awas'];
	$lokasi['tingkat_waspada']  = $data['tingkat_waspada'];
	$lokasi['tingkat_siaga']  = $data['tingkat_siaga'];
	$lokasi['ketinggian_air']  = $data['ketinggian_air'];
	$lokasi['date_time']  = $data['date_time'];

	if ($lokasi['ketinggian_air'] > $lokasi['tingkat_siaga'] && $lokasi['ketinggian_air'] < $lokasi['tingkat_waspada'] && $lokasi['ketinggian_air'] < $lokasi['tingkat_awas']) {
				//$lokasi = array('status' => "SIAGA");
				$lokasi['status']="SIAGA";
				//array_push($response, $lokasi);
			}
		else if ($lokasi['ketinggian_air'] > $lokasi['tingkat_siaga'] && $lokasi['ketinggian_air'] > $lokasi['tingkat_waspada'] && $lokasi['ketinggian_air'] < $lokasi['tingkat_awas']) {
				//$lokasi = array('status' => "WASPADA");
				$lokasi['status']="WASPADA";
				//array_push($response, $lokasi);
			}
		else if ($lokasi['ketinggian_air'] > $lokasi['tingkat_siaga'] && $lokasi['ketinggian_air'] > $lokasi['tingkat_waspada'] && $lokasi['ketinggian_air'] > $lokasi['tingkat_awas']) {
				//$lokasi = array('status' => "AWAS");
				$lokasi['status']="AWAS";
				//array_push($response, $lokasi);
			}
		else{
			$lokasi['status']="NORMAL";
		}

	array_push($response, $lokasi);
    //array_push($response["lokasi"], $lokasi);
}

echo json_encode($response);
?>