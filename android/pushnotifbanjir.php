<?php

$connection = mysqli_connect("mysql.idhostinger.com","u826911054_ews","u826911054","u826911054_uno");

/*
1.ambil semua data tabel lokasi
2.ambil data history terbaru berdasarkan id_lokasi
3.ambil ketinggian_air dari id_history	terbaru
4.jika ketinggian_air memenuhi if ,maka dicek user yang mempunyai token dari lokasi yang dipilih,BARU NOTIFIKASI DIKIRIM
6.jika ketinggian_air memenuhi if ,maka dicek user yang mempunyai token dari lokasi yang Tidak ada data kondisi air yang memenuhi di lokasi,NOTIFIKASI TIDAK DIKIRIM
5.kalau tidak memenuhi di ulang lagi while 2 
*/
$select_lokasi = "SELECT * FROM lokasi";
$hasil_lokasi = MYSQLi_QUERY($connection,$select_lokasi);

while ($row1 = MYSQLi_FETCH_ARRAY($hasil_lokasi)) {

	$select_history_terbaru = "SELECT MAX(id_history) AS 'idh' FROM history WHERE id_lokasi='".$row1['id_lokasi']."'";
	$hasil_history_terbaru = MYSQLi_QUERY($connection,$select_history_terbaru);

	while ($row2 = MYSQLi_FETCH_ARRAY($hasil_history_terbaru)) {
		
		$select_tinggi = "SELECT ketinggian_air FROM history WHERE id_history='".$row2['idh']."'";
		$hasil_tinggi = MYSQLi_QUERY($connection,$select_tinggi);
		$row3 = MYSQLi_FETCH_ARRAY($hasil_tinggi);

		if ($row3['ketinggian_air'] > $row1['tingkat_siaga'] && $row3['ketinggian_air'] < $row1['tingkat_waspada'] && $row3['ketinggian_air'] < $row1['tingkat_awas']) {
			
			//$select_users = "SELECT token FROM users WHERE id_lokasi='".$row1['id_lokasi']."'";

			$select_users = "SELECT users.token,tampungan.id_lokasi FROM tampungan JOIN users ON tampungan.email=users.email WHERE id_lokasi='".$row1['id_lokasi']."'";
			$hasil_users = MYSQLi_QUERY($connection,$select_users);

			
				while ($firebase = MYSQLi_FETCH_ARRAY($hasil_users)) {
				
				$ch = curl_init("https://fcm.googleapis.com/fcm/send");
				$header=array('Content-Type: application/json',
				"Authorization: key=AIzaSyDTAutoJbB4uF1CmhdqCu-2jzB5-pDdnDw");
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
				curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, "{ \"notification\": {    \"title\": \"".$row1['nama_lokasi']."\",   \"sound\": \"default\",    \"text\": \"Tinggi/Status : ".$row3['ketinggian_air']." CM/SIAGA\" },    \"to\" : \"".$firebase['token']."\",    \"click_action\": \"global\"}");
				curl_exec($ch);
				curl_close($ch);
				}//end while 3	
		}//end if 1

		elseif ($row3['ketinggian_air'] > $row1['tingkat_siaga'] && $row3['ketinggian_air'] > $row1['tingkat_waspada'] && $row3['ketinggian_air'] < $row1['tingkat_awas']) {
			
			$select_users = "SELECT users.token,tampungan.id_lokasi FROM tampungan JOIN users ON tampungan.email=users.email WHERE id_lokasi='".$row1['id_lokasi']."'";
			$hasil_users = MYSQLi_QUERY($connection,$select_users);

				while ($firebase = MYSQLi_FETCH_ARRAY($hasil_users)) {
				
				$ch = curl_init("https://fcm.googleapis.com/fcm/send");
				$header=array('Content-Type: application/json',
				"Authorization: key=AIzaSyDTAutoJbB4uF1CmhdqCu-2jzB5-pDdnDw");
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
				curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, "{ \"notification\": {    \"title\": \"".$row1['nama_lokasi']."\",   \"sound\": \"default\",    \"text\": \"Tinggi/Status : ".$row3['ketinggian_air']." CM/WASPADA\" },    \"to\" : \"".$firebase['token']."\",    \"click_action\": \"global\"}");
				curl_exec($ch);
				curl_close($ch);
			}
		}//end else if 2
		elseif ($row3['ketinggian_air'] > $row1['tingkat_siaga'] && $row3['ketinggian_air'] > $row1['tingkat_waspada'] && $row3['ketinggian_air'] > $row1['tingkat_awas']) {

			$select_users = "SELECT users.token,tampungan.id_lokasi FROM tampungan JOIN users ON tampungan.email=users.email WHERE id_lokasi='".$row1['id_lokasi']."'";
			$hasil_users = MYSQLi_QUERY($connection,$select_users);

				while ($firebase = MYSQLi_FETCH_ARRAY($hasil_users)) {
				
				$ch = curl_init("https://fcm.googleapis.com/fcm/send");
				$header=array('Content-Type: application/json',
				"Authorization: key=AIzaSyDTAutoJbB4uF1CmhdqCu-2jzB5-pDdnDw");
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
				curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, "{ \"notification\": {    \"title\": \"".$row1['nama_lokasi']."\",   \"sound\": \"default\",    \"text\": \"Tinggi/Status : ".$row3['ketinggian_air']." CM/AWAS\" },    \"to\" : \"".$firebase['token']."\",    \"click_action\": \"global\"}");
				curl_exec($ch);
				curl_close($ch);
			}//end while 3	
		}//end else if 3
		elseif ($row3['ketinggian_air'] < $row1['tingkat_siaga']) {

			$select_users = "SELECT users.token,tampungan.id_lokasi FROM tampungan JOIN users ON tampungan.email=users.email WHERE id_lokasi='".$row1['id_lokasi']."'";
			$hasil_users = MYSQLi_QUERY($connection,$select_users);

				while ($firebase = MYSQLi_FETCH_ARRAY($hasil_users)) {
				
				$ch = curl_init("https://fcm.googleapis.com/fcm/send");
				$header=array('Content-Type: application/json',
				"Authorization: key=AIzaSyDTAutoJbB4uF1CmhdqCu-2jzB5-pDdnDw");
				curl_setopt($ch, CURLOPT_HTTPHEADER, $header);
				curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
				curl_setopt($ch, CURLOPT_POST, 1);
				curl_setopt($ch, CURLOPT_POSTFIELDS, "{ \"notification\": {    \"title\": \"".$row1['nama_lokasi']."\",   \"sound\": \"default\",    \"text\": \"Tinggi/Status : ".$row3['ketinggian_air']." CM/NORMAL\" },    \"to\" : \"".$firebase['token']."\",    \"click_action\": \"global\"}");
				curl_exec($ch);
				curl_close($ch);
			}//end while 3	
		}//end else if 4

		else {
			echo "gagal";
		}//end else
	}//end while 2

}//end while 1
?>