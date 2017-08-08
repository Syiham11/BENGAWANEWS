package com.apn404.ews.bengawanews.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.apn404.ews.bengawanews.JSONParser;
import com.apn404.ews.bengawanews.R;
import com.apn404.ews.bengawanews.adapter.LokasiAdapter;
import com.apn404.ews.bengawanews.data.Lokasi;
import com.apn404.ews.bengawanews.helper.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotif extends Fragment {

    ListView list_lokasi;
    ArrayList<Lokasi> daftar_lokasi = new ArrayList<>();
    JSONArray daftarlokasi = null;
    protected JSONParser jsonParser;
    int success;

    String url_insert_lokasi = "http://ews.apn404.com/TA/android/insert_tampungan.php";
    String url_hapus_lokasi = "http://ews.apn404.com/TA/android/hapus_lokasi.php";

    protected static final String TAG_SUCCESS = "success";
    protected static final String TAG_LOKASI = "lokasi";
    protected static final String TAG_ID_LOKASI = "id_lokasi";
    protected static final String TAG_NAMA_LOKASI = "nama_lokasi";
    protected static final String TAG_EMAIL = "email";
    protected ProgressDialog pDialog;
    protected String strid_lokasi,stremail,strnama_lokasi;
    SessionManager session;
    View view;
    TextView textView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public FragmentNotif() {
        // Required empty public constructor
        this.jsonParser = new JSONParser();
    }

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
        list_lokasi = (ListView) view.findViewById(R.id.listnotif);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        TextView textView = (TextView) view.findViewById(R.id.kuni);

        ReadLokasiTask m = (ReadLokasiTask) new ReadLokasiTask().execute();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list_lokasi.setAdapter(new LokasiAdapter(FragmentNotif.this.getActivity(), FragmentNotif.this.daftar_lokasi));
                Toast.makeText(FragmentNotif.this.getActivity(), "Data Ter-Update", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorUncheck,R.color.colorAccent);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        list_lokasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                strnama_lokasi = daftar_lokasi.get(position).getNama_lokasi();
                new android.support.v7.app.AlertDialog.Builder(FragmentNotif.this.getActivity())
                        .setMessage("Aktifkan Notifikasi ?" + " " + strnama_lokasi)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                strid_lokasi = daftar_lokasi.get(position).getId_lokasi();
                                new pilihya().execute();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                strid_lokasi = daftar_lokasi.get(position).getId_lokasi();
                                new pilihtidak().execute();
                            }
                        })
                        .show();

            }
        });

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
                .setMessage("Silakan pilih lokasi dengan memilih tombol Ya ,jika anda ingin mendapatkan notifikasi")
                .setCancelable(false)
                .setPositiveButton("Oke",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    class ReadLokasiTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String url_tampil_lokasi;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FragmentNotif.this.getActivity());
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            url_tampil_lokasi= "http://ews.apn404.com/TA/android/getLokasi.php";

            Lokasi tempLokasi = new Lokasi();
            try {
                JSONObject json = FragmentNotif.this.jsonParser.getJSONFromUrl(url_tampil_lokasi);

                success = json.getInt(TAG_SUCCESS);
                Log.e("error", "nilai sukses=" + success);
                if (success == 1) {
                    daftarlokasi = json.getJSONArray(TAG_LOKASI);
                    for (int i = 0; i < daftarlokasi.length(); i++) {
                        JSONObject c = daftarlokasi.getJSONObject(i);
                        tempLokasi = new Lokasi();
                        tempLokasi.setId_lokasi(c.getString(TAG_ID_LOKASI));
                        tempLokasi.setNama_lokasi(c.getString(TAG_NAMA_LOKASI));
                        daftar_lokasi.add(tempLokasi);
                    }

                    return "OK";
                } else {
                    return "no results";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(FragmentNotif.this.getActivity(), "Periksa Koneksi Internet Anda...", Toast.LENGTH_LONG).show();
            }
            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(FragmentNotif.this.getActivity(), "Data Lokasi Masih Kosong", Toast.LENGTH_LONG).show();
            }
            if(success == 1) {
                list_lokasi.setAdapter(new LokasiAdapter(FragmentNotif.this.getActivity(), FragmentNotif.this.daftar_lokasi));
            }
        }

    }


    public class pilihya extends AsyncTask<String, String, String> {
        int success;

        public pilihya() {

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

            JSONObject json = jsonParser.makeHttpRequest(url_insert_lokasi, "POST", params);

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
                Toast.makeText(FragmentNotif.this.getActivity(), "Mohon Periksa Koneksi Internet",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(FragmentNotif.this.getActivity(), "Mohon Periksa Koneksi Internet",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                Toast.makeText(FragmentNotif.this.getActivity(), "Notifikasi Diaktifkan", Toast.LENGTH_SHORT).show();
            }
            else if (success == 0) {
                Toast.makeText(FragmentNotif.this.getActivity(), "Notifikasi Diaktifkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class pilihtidak extends AsyncTask<String, String, String> {
        int success;

        public pilihtidak() {

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

            JSONObject json = jsonParser.makeHttpRequest(url_hapus_lokasi, "POST", params);

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
                Toast.makeText(FragmentNotif.this.getActivity(), "Mohon Periksa Koneksi Internet",  Toast.LENGTH_SHORT).show();
            }
            else if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(FragmentNotif.this.getActivity(), "Mohon Periksa Koneksi Internet",  Toast.LENGTH_SHORT).show();
            }
            else if (success == 1){
                Toast.makeText(FragmentNotif.this.getActivity(), "Notifikasi Di Non-aktifkan", Toast.LENGTH_SHORT).show();
            }
            else if (success == 0) {
                Toast.makeText(FragmentNotif.this.getActivity(), "Notifikasi Di Non-aktifkan", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
