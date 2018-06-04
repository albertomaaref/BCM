package com.example.alim.bcm.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alim.bcm.model.Richiesta;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.alim.bcm.model.Constants.TAG;

public class Geocode extends AsyncTask<List<Richiesta>, Void, List<String>> {
    private Context context;
    private ProgressDialog progressDialog ;
    private LatLng latLngA;
    private LatLng latLngB;
    public AsynchResponse asyncResponse = null;

    public Geocode(Context context) {
        this.context = context;
        this.progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog.setMessage("sto caricando");
        progressDialog.show();
    }

    @Override
    protected List<String> doInBackground(List<Richiesta>... richieste) {
        List<String>response = new ArrayList<>();
        List<Richiesta> listaRichieste = richieste[0];
        HttpGeodecoding httpGeodecoding = new HttpGeodecoding();
        try {

            for (Richiesta richiesta: listaRichieste
                 ) {
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address="+richiesta.getCantiere()+"&key=AIzaSyCgtIgULUcESz1-Z7grOqFY94vaKACZnIE&");
                response.add(httpGeodecoding.getGeodecoding(url)) ;
            }

            //HttpDataHandler httpDataHandler = new HttpDataHandler();

            //Log.i("url",urlA);

            Log.e(TAG, this.getClass()+" Indirizzi carcati");

        }
        catch (Exception e){
            Log.e(TAG, this.getClass()+" errore caricamento indirizzi");
            return null;
        }

        return response;
    }


    @Override
    protected void onPostExecute(List<String> stringList) {
        //Log.i("ASYNC",s[0]+" "+s[1]);
        HashMap<String,LatLng> list = new HashMap<>();
        for (String stringa: stringList
             ) {

            try {
                JSONObject jsonObject = new JSONObject(stringa);
                String latA = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lngA = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();
                String localita = ((JSONArray)jsonObject.get("results")).getJSONObject(0).get("formatted_address")
                        .toString();

                latLngA = new LatLng(Double.parseDouble(latA),Double.parseDouble(lngA));
                list.put(localita,latLngA);




            }

            catch (JSONException e) {
                e.printStackTrace();
                asyncResponse.onResponse(null);
            }
        }
        if ( progressDialog.isShowing()) progressDialog.dismiss();
        asyncResponse.onResponse(list);



    }
}
