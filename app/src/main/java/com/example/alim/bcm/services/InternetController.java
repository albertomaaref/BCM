package com.example.alim.bcm.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetController {
    private Context context;

    public static InternetController instance = null;

    private InternetController(Context context){
        this.context = context;
    }

    public static InternetController getInsatance (Context context){
        if (instance == null)
            instance = new InternetController(context);
        return instance;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
