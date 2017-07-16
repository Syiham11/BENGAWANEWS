package com.apn404.ews.bengawanews.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.data.Data;

import java.util.List;

public class Adapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> items;

    public Adapter(Activity activity, List<Data> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) convertView = inflater.inflate(R.layout.list_home_row, null);

        TextView id_lokasi = (TextView) convertView.findViewById(R.id.id_lokasi);
        TextView nama_lokasi = (TextView) convertView.findViewById(R.id.nama_lokasi);
        TextView date_time = (TextView) convertView.findViewById(R.id.date_time);
        TextView ketinggian_air = (TextView) convertView.findViewById(R.id.ketinggian_air);

        Data data = items.get(position);

        id_lokasi.setText(data.getId_lokasi());
        nama_lokasi.setText(data.getNama_lokasi());
        date_time.setText(data.getDate_time());
        ketinggian_air.setText(data.getKetinggian_air());

        return convertView;
    }

}
