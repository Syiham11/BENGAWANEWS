package com.apn404.ews.bengawanews.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.apn404.ews.bengawanews.LoginActivity;
import com.apn404.ews.bengawanews.MainActivity;
import com.apn404.ews.bengawanews.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    SharedPreferences sharedPreferences;
    String string;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sharedPreferences = getSharedPreferences("Sesi", 0);
                string = sharedPreferences.getString("nama_user",null);
                if(string==null) {

                }else{
                    sendPushNotification(json);
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");

            /*String judul = (String) data.get("title\\\": \\\"\".$row1['nama_lokasi'].\"");
            String tinggi = (String) data.get("text\\\": \\\"Tinggi/Status : \".$row3['air'].\"/Waspada");

            if (judul == tinggi){

            }*/

            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an intent for the notification
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);


            //if there is no image
            if(imageUrl.equals("null")){
                //displaying small notification
                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}