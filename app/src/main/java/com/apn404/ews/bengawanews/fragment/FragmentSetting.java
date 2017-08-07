package com.apn404.ews.bengawanews.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.adapter.LokasiAdapter;
import com.apn404.ews.bengawanews.helper.SessionManager;

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

import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentSetting extends Fragment {

    public FragmentSetting() {
        // Required empty public constructor
    }

    View view;
    TextView textView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String stremail;
    ListView lvCountries;
    final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
    SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);
        final ListView lvCountries = (ListView) view.findViewById(R.id.lv_tampungan);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        TextView textView = (TextView) view.findViewById(R.id.kuni);

        try{
            this.session = new SessionManager(getActivity());
            this.session.checkLogin();
            stremail = this.session.getUserDetails().get(SessionManager.KEY_EMAIL);
            session = new SessionManager(getActivity().getApplicationContext());

            //JSONArray data = new JSONArray(getJSONUrl("http://ews.apn404.com/TA/android/getTampungan.php?email=agungeks@student.uns.ac.id"));
            JSONArray data = new JSONArray(getJSONUrl("http://ews.apn404.com/TA/android/getTampungan.php?email="+stremail+""));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                //ngambil data dari database
                map.put("id_tampungan", c.getString("id_tampungan"));
                map.put("id_lokasi", c.getString("id_lokasi"));
                map.put("email", c.getString("email"));
                map.put("nama_lokasi", c.getString("nama_lokasi"));

                MyArrList.add(map);

            }

            lvCountries.setAdapter(new SimpleAdapter(getActivity(),
                    MyArrList, R.layout.lv_layout, new String[]
                    {"id_tampungan","id_lokasi", "email","nama_lokasi"}, new int[]
                    {R.id.id_tampungan_lay,R.id.id_lokasi_lay, R.id.email_lay,R.id.nama_lokasi_lay}));

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    lvCountries.setAdapter(new SimpleAdapter(getActivity(),
                            MyArrList, R.layout.lv_layout, new String[]
                            {"id_tampungan","id_lokasi", "email","nama_lokasi"}, new int[]
                            {R.id.id_tampungan_lay,R.id.id_lokasi_lay, R.id.email_lay,R.id.nama_lokasi_lay}));
                    Toast.makeText(FragmentSetting.this.getActivity(), "Data Ter-Update", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorUncheck,R.color.colorAccent);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

        }catch (JSONException e){
            e.printStackTrace();
        }
        textView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return view;
    }

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setTitle("Sekilas Info !");
        alertDialogBuilder
                .setMessage("Anda akan mendapatkan notifikasi dari lokasi yang aktif")
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
