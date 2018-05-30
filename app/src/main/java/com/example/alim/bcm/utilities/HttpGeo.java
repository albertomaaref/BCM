package com.example.alim.bcm.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpGeo {

    public HttpGeo() {
    }

    public String getGeodecoding(String requestUrl){
        URL url;
        String response="";
        try {
            requestUrl = requestUrl.replaceAll("\\s+","");
            url = new URL(requestUrl);
            HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(155000);
            connection.setConnectTimeout(155000);
            connection.setDoInput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type","applicatin/x-www-form-urlencoded");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                String line ="";
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null){
                    response+=line;
                }


            }
            else response="";
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return response;


    }


}
