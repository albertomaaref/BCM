package com.example.alim.bcm.utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Iterator;

public class JSONMapper {
    private static final String TAG = "JSONMapper";

    public static String toJSON(Object obj) {

        try {
            GsonBuilder builder = new GsonBuilder();
            builder.disableHtmlEscaping();

            Gson gson = builder.create();

            String value = gson.toJson(obj);

            return value;
        } catch (Exception e) {
            Log.e(TAG, "errore toJSON", e);
        }

        return null;
    }

    public static String toJSONWithExposeAnnotation(Object obj) {

        try {
            GsonBuilder builder = new GsonBuilder();
            builder.disableHtmlEscaping();
            builder.excludeFieldsWithoutExposeAnnotation();
            // builder.serializeNulls();
            Gson gson = builder.create();

            String value = gson.toJson(obj);

            return value;
        } catch (Exception e) {
            Log.e(TAG, "Eccezione durante la creazione del content", e);
        }

        return null;
    }

    public static Object fromJSON(String content, Type className) {

        try {
            Gson gson = new Gson();

            Object obj = gson.fromJson(content, className);

            return obj;
        } catch (Exception e) {
            Log.e(TAG, "Eccezione durante il parse del content", e);
            e.printStackTrace();
        }

        return null;
    }

    public static String fromObjToArray(String response) {
        try {

            JSONObject object = new JSONObject(response);
            Iterator keys = object.keys();
            JSONArray jsonArray = new JSONArray();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                jsonArray.put(object.get(key));
            }
            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
