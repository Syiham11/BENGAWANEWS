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
				//$select_users = "SELECT token FROM users WHERE id_lokasi='".$lokasi['id_lokasi']."'";

			$select_users = "SELECT users.token,tampungan.id_lokasi FROM tampungan JOIN users ON tampungan.email=users.email WHERE id_lokasi='".$lokasi['id_lokasi']."'";
			$hasil_users = MYSQLi_QUERY($connect,$select_users);

			
				while ($firebase = MYSQLi_FETCH_ARRAY($hasil_users)) {
				
				$ch = curl_init("https://fcm.googleapis.com/fcm/send");
				$header=array('Content-Type: application/json',
				"Authorization: key=AIzaSyDTAutoJbB4uF1CmhdqCu-2jzB5-pDdnDw");
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
				curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, "{ \"notification\": {    \"title\": \"".$lokasi['nama_lokasi']."\",   \"sound\": \"default\",    \"text\": \"Tinggi/Status : ".$lokasi['ketinggian_air']." CM/SIAGA\" },    \"to\" : \"".$firebase['token']."\",    \"click_action\": \"global\"}");
				curl_exec($ch);
				curl_close($ch);
				}//end while 3	
			}
		else if ($lokasi['ketinggian_air'] > $lokasi['tingkat_siaga'] && $lokasi['ketinggian_air'] > $lokasi['tingkat_waspada'] && $lokasi['ketinggian_air'] < $lokasi['tingkat_awas']) {
				//$select_users = "SELECT token FROM users WHERE id_lokasi='".$lokasi['id_lokasi']."'";

			$select_users = "SELECT users.token,tampungan.id_lokasi FROM tampungan JOIN users ON tampungan.email=users.email WHERE id_lokasi='".$lokasi['id_lokasi']."'";
			$hasil_users = MYSQLi_QUERY($connect,$select_users);

			
				while ($firebase = MYSQLi_FETCH_ARRAY($hasil_users)) {
				
				$ch = curl_init("https://fcm.googleapis.com/fcm/send");
				$header=array('Content-Type: application/json',
				"Authorization: key=AIzaSyDTAutoJbB4uF1CmhdqCu-2jzB5-pDdnDw");
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
				curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, "{ \"notification\": {    \"title\": \"".$lokasi['nama_lokasi']."\",   \"sound\": \"default\",    \"text\": \"Tinggi/Status : ".$lokasi['ketinggian_air']." CM/WASPADA\" },    \"to\" : \"".$firebase['token']."\",    \"click_action\": \"global\"}");
				curl_exec($ch);
				curl_close($ch);
				}//end while 3	
			}
		else if ($lokasi['ketinggian_air'] > $lokasi['tingkat_siaga'] && $lokasi['ketinggian_air'] > $lokasi['tingkat_waspada'] && $lokasi['ketinggian_air'] > $lokasi['tingkat_awas']) {
				//$select_users = "SELECT token FROM users WHERE id_lokasi='".$lokasi['id_lokasi']."'";

			$select_users = "SELECT users.token,tampungan.id_lokasi FROM tampungan JOIN users ON tampungan.email=users.email WHERE id_lokasi='".$lokasi['id_lokasi']."'";
			$hasil_users = MYSQLi_QUERY($connect,$select_users);

			
				while ($firebase = MYSQLi_FETCH_ARRAY($hasil_users)) {
				
				$ch = curl_init("https://fcm.googleapis.com/fcm/send");
				$header=array('Content-Type: application/json',
				"Authorization: key=AIzaSyDTAutoJbB4uF1CmhdqCu-2jzB5-pDdnDw");
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
				curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, "{ \"notification\": {    \"title\": \"".$lokasi['nama_lokasi']."\",   \"sound\": \"default\",    \"text\": \"Tinggi/Status : ".$lokasi['ketinggian_air']." CM/AWAS\" },    \"to\" : \"".$firebase['token']."\",    \"click_action\": \"global\"}");
				curl_exec($ch);
				curl_close($ch);
				}//end while 3	
			}
		else if ($lokasi['ketinggian_air'] < $lokasi['tingkat_siaga']) {
				//$select_users = "SELECT token FROM users WHERE id_lokasi='".$lokasi['id_lokasi']."'";

			$select_users = "SELECT users.token,tampungan.id_lokasi FROM tampungan JOIN users ON tampungan.email=users.email WHERE id_lokasi='".$lokasi['id_lokasi']."'";
			$hasil_users = MYSQLi_QUERY($connect,$select_users);

			
				while ($firebase = MYSQLi_FETCH_ARRAY($hasil_users)) {
				
				$ch = curl_init("https://fcm.googleapis.com/fcm/send");
				$header=array('Content-Type: application/json',
				"Authorization: key=AIzaSyDTAutoJbB4uF1CmhdqCu-2jzB5-pDdnDw");
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
				curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, "{ \"notification\": {    \"title\": \"".$lokasi['nama_lokasi']."\",   \"sound\": \"default\",    \"text\": \"Tinggi/Status : ".$lokasi['ketinggian_air']." CM/NORMAL\" },    \"to\" : \"".$firebase['token']."\",    \"click_action\": \"global\"}");
				curl_exec($ch);
				curl_close($ch);
				}//end while 3	
			}
		else{
			echo "gagal";
		}

	array_push($response, $lokasi);
    //array_push($response["lokasi"], $lokasi);
}

echo json_encode($response);
?>