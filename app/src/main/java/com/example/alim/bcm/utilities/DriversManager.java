package com.example.alim.bcm.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Constants;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DriversManager  {
    private static DriversManager ourInstance = null;
    private TaskCompletion delegato;



    public static DriversManager getInstance() {
        if (ourInstance == null){
            ourInstance = new DriversManager();
        }
        return ourInstance;
    }

    private DriversManager() {

    }


    public void getAutistiInternal(@NonNull TaskCompletion taskCompletion ){
        delegato = taskCompletion;
        FireBaseConnection.get(Constants.UTENTI+"/"+Constants.AUTISTA + ".json", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                delegato.taskToDo(Constants.SUCCESSO,s);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegato.taskToDo(Constants.ERROR,""+statusCode);
            }
        });
    }


}
