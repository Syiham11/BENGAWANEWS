package com.apn404.ews.bengawanews.data;

public class Data {
    private String id_lokasi,nama_lokasi,date_time,ketinggian_air;

    public Data(){

    }
    public Data(String id_lokasi, String nama_lokasi, String date_time, String ketinggian_air){
        this.id_lokasi=id_lokasi;
        this.nama_lokasi=nama_lokasi;
        this.date_time=date_time;
        this.ketinggian_air=ketinggian_air;
    }

    public String getId_lokasi(){
        return id_lokasi;
    }
    public void setId_lokasi(String id_lokasi){
        this.id_lokasi=id_lokasi;
    }

    public String getNama_lokasi(){
        return nama_lokasi;
    }
    public void setNama_lokasi(String nama_lokasi){
        this.nama_lokasi=nama_lokasi;
    }

    public String getDate_time(){
        return date_time;
    }
    public void setDate_time(String date_time){
        this.date_time=date_time;
    }

    public String getKetinggian_air(){
        return ketinggian_air;
    }
    public void setKetinggian_air(String ketinggian_air){
        this.ketinggian_air=ketinggian_air;
    }
}
