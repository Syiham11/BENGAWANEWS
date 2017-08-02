package com.apn404.ews.bengawanews.adapter;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.data.Lokasi;

public class LokasiAdapter extends BaseAdapter {
    private Activity activity;

    private ArrayList<Lokasi> data_lokasi=new ArrayList<>();
    private static LayoutInflater inflater = null;

    public LokasiAdapter(Activity a, ArrayList<Lokasi> d) {
        activity = a; data_lokasi = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data_lokasi.size();
    }
    public Object getItem(int position) {
        return data_lokasi.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.list_notif_row, null);
        }
        TextView id_lokasi = (TextView) vi.findViewById(R.id.id_lokasi2);
        TextView nama_lokasi = (TextView) vi.findViewById(R.id.nama_lokasi2);

        Lokasi daftar_lokasi = data_lokasi.get(position);
        id_lokasi.setText(daftar_lokasi.getId_lokasi());
        nama_lokasi.setText(daftar_lokasi.getNama_lokasi());

        return vi;
    }
}
