<?php

include "db_connect.php";

$email = $_GET['email'];

$query = "SELECT * FROM tampungan JOIN lokasi ON tampungan.id_lokasi=lokasi.id_lokasi where tampungan.id_lokasi !='0' and email='$email' ORDER BY lokasi.id_lokasi ";
$hasil = MYSQLi_QUERY($connect,$query);

$response = array();

while ($data = MYSQLi_FETCH_ARRAY($hasil))
{
    $h['id_tampungan']    = $data['id_tampungan'];
    $h['id_lokasi']  = $data['id_lokasi'];
    $h['email']  = $data['email'];
    $h['nama_lokasi'] =$data['nama_lokasi'];
    array_push($response, $h);
}
 echo json_encode($response);
?>