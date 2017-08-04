package com.apn404.ews.bengawanews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.apn404.ews.bengawanews.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBeranda extends Fragment {


    public FragmentBeranda() {
        // Required empty public constructor
    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beranda, container, false);
        ListView lvCountries = (ListView) view.findViewById(R.id.lv_fr_beranda);

        try{
            JSONArray data = new JSONArray(getJSONUrl("http://ews.apn404.com/TA/android/getBerandan.php"));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                //ngambil data dari database
                map.put("id_lokasi", c.getString("id_lokasi"));
                map.put("nama_lokasi", c.getString("nama_lokasi"));
                map.put("longitude", c.getString("longitude"));
                map.put("lattitude", c.getString("lattitude"));
                map.put("tingkat_awas", c.getString("tingkat_awas"));
                map.put("tingkat_waspada", c.getString("tingkat_waspada"));
                map.put("tingkat_siaga", c.getString("tingkat_siaga"));
                map.put("ketinggian_air", c.getString("ketinggian_air"));
                map.put("date_time", c.getString("date_time"));
                map.put("status", c.getString("status"));

                MyArrList.add(map);
            }

            lvCountries.setAdapter(new SimpleAdapter(getActivity(),
                    MyArrList, R.layout.lv_beranda, new String[]
                    {"id_lokasi","nama_lokasi","longitude","lattitude","tingkat_awas","tingkat_waspada","tingkat_siaga","ketinggian_air","date_time","status"}, new int[]
                    {R.id.id_lokasib,R.id.nama_lokasib,R.id.longitudeb,R.id.lattitudeb,R.id.tingkat_awasb,R.id.tingkat_waspadab,R.id.tingkat_siagab, R.id.ketinggian_airb,R.id.date_timeb,R.id.status}));

        }catch (JSONException e){
            e.printStackTrace();
        }
        return view;
    }

    public String getJSONUrl(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
