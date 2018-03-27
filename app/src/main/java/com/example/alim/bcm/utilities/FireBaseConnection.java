package com.example.alim.bcm.utilities;

import com.loopj.android.http.*;
/**
 * Created by alim on 21-Mar-18.
 */

public final class FireBaseConnection {



    public static final String PASSWORD = "password";

    public static final String USERNAME = "username";


    public static final String BASE_URL = "https://bcmfirebaseproject.firebaseio.com/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
