package com.example.alim.bcm.utilities;

import android.support.annotation.NonNull;

import com.example.alim.bcm.data.model.Constants;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import static com.example.alim.bcm.data.model.Constants.CAPOCANTIERE;
import static com.example.alim.bcm.data.model.Constants.ERROR;
import static com.example.alim.bcm.data.model.Constants.SUCCESSO;
import static com.example.alim.bcm.data.model.Constants.UTENTI;

public class ManagerSiteAndPersonal {
    private static ManagerSiteAndPersonal ourInstance = null;




    public static ManagerSiteAndPersonal getInstance() {
        if (ourInstance == null){
            ourInstance = new ManagerSiteAndPersonal();
        }
        return ourInstance;
    }

    private ManagerSiteAndPersonal() {

    }


    public void getAutistiInternal(@NonNull TaskCompletion taskCompletion ){
        final TaskCompletion delegato = taskCompletion;
        FireBaseConnection.get(UTENTI+"/"+Constants.AUTISTA + ".json", null, new AsyncHttpResponseHandler() {
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

    public void getCantieri (@NonNull TaskCompletion taskCompletion){
        final TaskCompletion delegato = taskCompletion;
        FireBaseConnection.get(Constants.CANTIERI + ".json", null, new AsyncHttpResponseHandler() {
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

    public void getBosses (@NonNull TaskCompletion taskCompletion){
        final TaskCompletion delegato = taskCompletion;
        FireBaseConnection.get(UTENTI + "/" + CAPOCANTIERE + ".json", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                delegato.taskToDo(SUCCESSO,s);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegato.taskToDo(ERROR,""+statusCode);
            }
        });
    }


}
