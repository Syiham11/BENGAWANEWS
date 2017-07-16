package com.apn404.ews.bengawanews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.apn404.ews.bengawanews.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.apn404.ews.bengawanews.adapter.Adapter;
import com.apn404.ews.bengawanews.application.Controller;
import com.apn404.ews.bengawanews.data.Data;
import com.apn404.ews.bengawanews.utils.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    View view;
    SwipeRefreshLayout swipe;
    List<Data> itemList = new ArrayList<>();
    Adapter adapter;

    private static final String TAG = FragmentHome.class.getSimpleName();

    public static final String TAG_ID_LOKASI        = "id_lokasi";
    public static final String TAG_NAMA_LOKASI      = "nama_lokasi";
    public static final String TAG_DATE_TIME        = "date_time";
    public static final String TAG_KETINGGIAN_AIR   = "ketinggian_air";


    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        //view = inflater.inflate(R.layout.activity_main, container, false);
        // menghubungkan variablel pada layout dan pada java
        swipe   = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        list    = (ListView) view.findViewById(R.id.list);

        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new Adapter(getActivity(), itemList);
        list.setAdapter(adapter);

        // menamilkan widget refresh
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           itemList.clear();
                           adapter.notifyDataSetChanged();
                           callVolley();
                       }
                   }
        );

        return view;
    }


    @Override
    public void onRefresh() {
        itemList.clear();
        adapter.notifyDataSetChanged();
        callVolley();
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(true);

        // membuat request JSON
        String url_select = Server.URL + "getHome.php";
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Data item = new Data();

                        item.setId_lokasi(obj.getString(TAG_ID_LOKASI));
                        item.setNama_lokasi(obj.getString(TAG_NAMA_LOKASI));
                        item.setDate_time(obj.getString(TAG_DATE_TIME));
                        item.setKetinggian_air(obj.getString(TAG_KETINGGIAN_AIR));

                        // menambah item ke array
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                adapter.notifyDataSetChanged();

                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }


}

