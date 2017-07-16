package com.apn404.ews.bengawanews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apn404.ews.bengawanews.adapter.JSONParser;
import com.apn404.ews.bengawanews.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends Activity  {

    Button daftar, login;
    Intent a;
    EditText nama_user,no_hp;
    String url, success;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        daftar = (Button) findViewById(R.id.daftar);
        login = (Button) findViewById(R.id.login);
        nama_user = (EditText) findViewById(R.id.nama_user);
        no_hp = (EditText) findViewById(R.id.no_hp);

        daftar.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View arg0) {
                Intent daftar = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(daftar);

            }
        });

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                url = "http://ews.apn404.com/TA/android/Login.php?" + "nama_user="
                        + nama_user.getText().toString()+ "&no_hp="
                        + no_hp.getText().toString();

                if (nama_user.getText().toString().trim().length() > 0
                        && no_hp.getText().toString().trim().length() > 0)
                {
                    new Masuk().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Nama / Nomor belum terisi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class Masuk extends AsyncTask<String, String, String>
    {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Sedang memuat...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... arg0) {
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url);
            try {
                success = json.getString("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("login");
                if (success.equals("1")) {
                    for (int i = 0; i < hasil.length(); i++) {
                        JSONObject c = hasil.getJSONObject(i);
                        String nama_user = c.getString("nama_user").trim();
                        String no_hp = c.getString("no_hp").trim();
                        session.createLoginSession(nama_user,no_hp);
                        Log.e("ok", " ambil data");
                    }
                } else {
                    Log.e("erro", "tidak bisa ambil data 0");
                }

            } catch (Exception e) {
                Log.e("erro", "tidak bisa ambil data 1");
            }
            return null;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (success.equals("1")) {
                a = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(a);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Nama / Nomor Tidak Valid!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Keluar Aplikasi ?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LoginActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }
}