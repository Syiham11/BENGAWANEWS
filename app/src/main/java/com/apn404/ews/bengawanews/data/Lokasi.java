package com.apn404.ews.bengawanews.data;

/**
 * Created by agungeks on 01/08/2017.
 */

public class Lokasi {
    private String id_lokasi;
    private String nama_lokasi;

    public void setId_lokasi (String id_lokasi)
    {
        this.id_lokasi = id_lokasi;
    }

    public String getId_lokasi()
    {
        return id_lokasi;
    }

    public void setNama_lokasi (String nama_lokasi)
    {
        this.nama_lokasi = nama_lokasi;
    }

    public String getNama_lokasi()
    {
        return nama_lokasi;
    }
}
