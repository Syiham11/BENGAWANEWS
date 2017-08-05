package com.apn404.ews.bengawanews;

/**
 * Created by agungeks on 28/07/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.apn404.ews.bengawanews.firebase.Config;
import com.apn404.ews.bengawanews.helper.SessionManager;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProsesLoginActivity extends Activity {

    private static final String TAG = ProsesLoginActivity.class.getSimpleName();
    JSONParser jsonParser = new JSONParser();
    String url_login_user= "http://ews.apn404.com/TA/android/login_user.php";
    String url_register_user= "http://ews.apn404.com/TA/android/register_user.php";

    // JSON Node names, ini harus sesuai yang di API
    public static final String TAG_NAMA = "nama";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_UID = "uid";
    public static final String TAG_TOKEN = "token";
    String regId;
    int success;
    String nama,email,uid;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proses_login);

        session = new SessionManager(getApplication());

        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        //nyimpen ke database
        if(user != null){
            nama  = user.getDisplayName();
            email = user.getEmail();
            uid   = user.getUid();
            new LoginUser().execute();
        }
        else{
            goLoginScreen();
        }
    }

    private void goLoginScreen(){
        Intent intent =new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public class LoginUser extends AsyncTask<String,String,String> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProsesLoginActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {

            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair(TAG_EMAIL, email));
            params.add(new BasicNameValuePair(TAG_UID, uid));

            // Note that create Post url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_login_user, "GET", params);
            if(json == null){
                return "Error Converting";
            }

            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);
                JSONArray hasil = json.getJSONArray("users");

                for (int i = 0; i < hasil.length(); i++) {
                    JSONObject c = hasil.getJSONObject(i);

                    String email = c.getString("email").trim();
                    String uid = c.getString("uid").trim();
                    //membuat session email fb
                    session.createLoginUser(email,uid);
                }
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(ProsesLoginActivity.this, "Periksa Koneksi Internet",  Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
            else if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(ProsesLoginActivity.this, "Periksa Koneksi Internet",  Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
            else if (success == 0){
                new simpan().execute();
            }
            else if (success == 1){
                ProsesLoginActivity.this.startActivity(new Intent(ProsesLoginActivity.this, MainActivity.class));
                ProsesLoginActivity.this.finish();
            }
        }
    }


    public class simpan extends AsyncTask<String,String,String> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProsesLoginActivity.this);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return getUser();
        }

        private String getUser() {
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair(TAG_NAMA, nama));
            params.add(new BasicNameValuePair(TAG_EMAIL, email));
            params.add(new BasicNameValuePair(TAG_UID, uid));
            params.add(new BasicNameValuePair(TAG_TOKEN, regId));

            // Note that create Post url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_register_user, "POST", params);
            if(json == null){
                return "Error Converting";
            }

            try {
                success = json.getInt("success");
                Log.e("error", "nilai sukses=" + success);

               return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception Caught";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result.equalsIgnoreCase("Exception Caught")){
                Toast.makeText(ProsesLoginActivity.this, "Periksa Koneksi Internet",  Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
            else if (result.equalsIgnoreCase("Error Converting")){
                Toast.makeText(ProsesLoginActivity.this, "Periksa Koneksi Internet",  Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
            else if (success == 1){
                new LoginUser().execute();
            }
        }
    }
}