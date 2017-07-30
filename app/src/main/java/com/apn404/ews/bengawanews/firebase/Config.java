package com.apn404.ews.bengawanews.firebase;

/**
 * Created by agungeks on 26/07/2017.
 */

public class Config {
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    public static final String TOPIC_JURUG = "jurug";
    public static final String TOPIC_MOJOLABAN = "mojolaban";
    public static final String TOPIC_POLOKARTO = "polokarto";
    public static final String TOPIC_PONOROGO = "ponorogo";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}

