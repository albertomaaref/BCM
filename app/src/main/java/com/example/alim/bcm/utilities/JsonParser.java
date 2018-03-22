package com.example.alim.bcm.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by alim on 22-Mar-18.
 */

public final class JsonParser {

    public static String getPassword(String string) {

        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (key.toLowerCase().equals("password")) {
                    return object.getString(key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
