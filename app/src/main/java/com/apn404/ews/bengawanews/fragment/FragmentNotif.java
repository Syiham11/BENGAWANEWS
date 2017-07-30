package com.apn404.ews.bengawanews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.apn404.ews.bengawanews.JSONParser;
import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.helper.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotif extends Fragment {


    public FragmentNotif() {
        // Required empty public constructor
    }

    JSONParser jsonParser = new JSONParser();
    String url_tambah_lokasi= "http://banjir.mitigasi.com/banjir/android/tambah_lokasi.php";
    String url_reset_lokasi= "http://banjir.mitigasi.com/banjir/android/reset_lokasi.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_ID_LOKASI = "id_lokasi";
    protected static final String TAG_NAMA_LOKASI = "nama_lokasi";
    protected static final String TAG_EMAIL = "email";

    protected Spinner lokasi;
    protected ProgressDialog pDialog;
    protected String strnamalokasi,strid_lokasi,stremail;
    protected Button pilih,reset;
    SessionManager session;

    View view;

    @Override
    public void onStart() {
        super.onStart();
        this.session = new SessionManager(getActivity());
        this.session.checkLogin();

        stremail = this.session.getUserDetails().get(SessionManager.KEY_EMAIL);
        session = new SessionManager(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notif, container, false);
        this.lokasi = (Spinner) view.findViewById(R.id.lokasi);
        this.pilih = (Button) view.findViewById(R.id.simpann);
        this.reset = (Button) view.findViewById(R.id.reset);

        try {
            JSONArray data = new JSONArray(getJSONUrl("http://banjir.mitigasi.com/banjir/android/getLokasi.php"));

            final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<>();
            HashMap<String, String> map;

            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<>();
                map.put("id_lokasi", c.getString("id_lokasi"));
                map.put("nama_lokasi", c.getString("nama_lokasi"));
                MyArrList.add(map);

            }
            lokasi.setAdapter(new SimpleAdapter(getActivity(),
                    MyArrList, R.layout.list_spiner_lokasi, new String[]
                    {"id_lokasi", "nama_lokasi"}, new int[]
                    {R.id.id_lokasi, R.id.nama_lokasi}));

            lokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View selectedItemView, int position, long id) {
                    strid_lokasi = MyArrList.get(position).get("id_lokasi");
                    Log.e("Error",""+strid_lokasi);
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                    Toast.makeText(FragmentNotif.this.getActivity(), "Your Selected : Nothing", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.pilih.setOnClickListener(new pilih());
        this.reset.setOnClickListener(new reset());
        return view;
    }

    class pilih implements View.OnClickListener {
        public void onClick(View v) {
            if (stremail!=null){
            Toast.makeText(FragmentNotif.this.getActivity(),stremail, Toast.LENGTH_SHORT).show();
            }
            new tambahlokasi().execute();
        }
    }
    class reset implements View.OnClickListener {
        public void onClick(View v) {
            new resetLokasi().execute();
        }
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

    public class tambahlokasi extends AsyncTask<String, String, String> {
        int success;

        public tambahlokasi() {
            //this.strtanggal = FragmentNotif.this.tanggal_lokasi.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            FragmentNotif.this.pDialog = new ProgressDialog(FragmentNotif.this.getActivity());
            FragmentNotif.this.pDialog.setMessage("Loading...");
            FragmentNotif.this.pDialog.setIndeterminate(false);
            FragmentNotif.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getlokasiList();
        }

        public String getlokasiList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(FragmentNotif.TAG_ID_LOKASI, strid_lokasi));
            params.add(new BasicNameValuePair(FragmentNotif.TAG_EMAIL, stremail));

            JSONObject json = jsonParser.makeHttpRequest(url_tambah_lokasi, "POST", params);

            if (json == null) {
                return "Error Converting";
            }
            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            FragmentNotif.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(FragmentNotif.this.getActivity(), "Please Check Your Internet Connection...",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(FragmentNotif.this.getActivity(), "Your Connection Error Please Try Again...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                AlertDialog.Builder pDialog = new AlertDialog.Builder(FragmentNotif.this.getActivity());
                pDialog.setTitle("Tambah lokasi");
                pDialog.setMessage("Data lokasi Berhasil Tersimpan");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else if (success == 0) {
                Toast.makeText(FragmentNotif.this.getActivity(), "Inputan Gagal", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {

            }
        }
    }

    public class resetLokasi extends AsyncTask<String, String, String> {
        int success;

        public resetLokasi() {
            //this.strtanggal = FragmentNotif.this.tanggal_lokasi.getText().toString();
        }

        protected void onPreExecute() {
            super.onPreExecute();
            FragmentNotif.this.pDialog = new ProgressDialog(FragmentNotif.this.getActivity());
            FragmentNotif.this.pDialog.setMessage("Loading...");
            FragmentNotif.this.pDialog.setIndeterminate(false);
            FragmentNotif.this.pDialog.show();
        }

        protected String doInBackground(String... params) {
            return getlokasiList();
        }

        public String getlokasiList() {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(FragmentNotif.TAG_ID_LOKASI, strid_lokasi));
            params.add(new BasicNameValuePair(FragmentNotif.TAG_EMAIL, stremail));

            JSONObject json = jsonParser.makeHttpRequest(url_reset_lokasi, "POST", params);

            if (json == null) {
                return "Error Converting";
            }
            try {
                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            FragmentNotif.this.pDialog.dismiss();

            if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(FragmentNotif.this.getActivity(), "Please Check Your Internet Connection...",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(FragmentNotif.this.getActivity(), "Your Connection Error Please Try Again...",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                AlertDialog.Builder pDialog = new AlertDialog.Builder(FragmentNotif.this.getActivity());
                pDialog.setTitle("Tambah lokasi");
                pDialog.setMessage("Data lokasi Berhasil Tersimpan");
                pDialog.setNeutralButton("OK", new berhasil()).show();
            }
            else if (success == 0) {
                Toast.makeText(FragmentNotif.this.getActivity(), "Inputan Gagal", Toast.LENGTH_SHORT).show();
            }
        }

        protected class berhasil implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int d) {

            }
        }
    }
}
