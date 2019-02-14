package com.example.alim.bcm.utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.alim.bcm.CapoCantiereActivity;
import com.example.alim.bcm.ImpiegatoActivity;
import com.example.alim.bcm.R;
import com.example.alim.bcm.data.model.Constants;
import com.example.alim.bcm.data.model.Richiesta;
import com.example.alim.bcm.services.InternetController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.alim.bcm.data.model.Constants.TAG;

/**
 * Created by alim on 29-Mar-18.
 */

public class RequestManager {

    private TaskCompletion delegato;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private static RequestManager istanza = null;
    private Context context;
    DatabaseReference ref;
    FirebaseDatabase database;


    private RequestManager() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
    }

    public static RequestManager getIstance() {
        if (istanza == null) {
            istanza = new RequestManager();
        }
        return istanza;
    }

    public void sendRequest(final Context context, final Richiesta richiesta, final String type, final android.support.v4.app.FragmentManager fragmentManager, final Fragment fragment) {

        // controllo se c connessione

        if (InternetController.getInsatance(context).isOnline()){

            Activity activity = (Activity) context;
            int idContainer = 0;
            if (activity instanceof ImpiegatoActivity) {
                idContainer = R.id.fragmentImpiegato;
            } else if (activity instanceof CapoCantiereActivity) {
                idContainer = R.id.fragmentCapo;
            }
            ref.child("richieste/" + "R"+richiesta.getId() + "/id").setValue(richiesta.getId());
            ref.child("richieste/" + "R"+richiesta.getId() + "/cantiere").setValue(richiesta.getCantiere());
            ref.child("richieste/" + "R"+richiesta.getId() + "/dataConsegna").setValue(richiesta.getDataConesgna());
            ref.child("richieste/" + "R"+richiesta.getId() + "/nota").setValue(richiesta.getTestoLibero());
            ref.child("richieste/" + "R"+richiesta.getId() + "/stato").setValue(richiesta.getStato());
            ref.child("richieste/" + "R"+richiesta.getId() + "/corriere").setValue(richiesta.getAutista());


            //int lunghezza = 0;
            if (type.equalsIgnoreCase(Constants.ATTREZZI)) {
                ref.child("richieste/" + "R"+richiesta.getId() + "/lista" + type.toUpperCase()).setValue(richiesta.getListaAttrezzi());
                //lunghezza = richiesta.getListaAttrezzi().size();
                    /*for (int i = 0 ; i< lunghezza; i++){
                        ref.child("richieste/"+richiesta.getId()+"/lista"+type+"/"+(i+1)).setValue(richiesta.getListaAttrezzi().get(i));
                    }*/
            } else if (type.equalsIgnoreCase(Constants.MATERIALI)) {
                ref.child("richieste/" + "R"+richiesta.getId() + "/lista" + type.toUpperCase()).setValue(richiesta.getListaMateriali());

            }
            //lunghezza  = richiesta.getListaMateriali().size();
                /*for (int i = 0 ; i<= lunghezza; i++){

                }*/

            ItemsManager itemsManager = ItemsManager.getIstance(context);
            itemsManager.removeAll();
            fragmentManager.beginTransaction().replace(idContainer, fragment).commit();

        }

        else Toast.makeText(context,"PROLEMI DI RETE",Toast.LENGTH_SHORT).show();


    }

    public void downloadRequests(@NonNull TaskCompletion taskCompletion) {
        delegato = taskCompletion;

        if (InternetController.getInsatance(context).isOnline()){

            FireBaseConnection.get(Constants.RICHIESTE + ".json", null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    delegato.taskToDo(Constants.SUCCESSO, s);
                    Log.e(TAG, "" + this.getClass() + " caricati le richieste");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    delegato.taskToDo(Constants.ERROR, String.valueOf(statusCode));
                    Log.e(TAG, "" + this.getClass() + " errore caricamento richieste");
                }
            });
        }

        else Toast.makeText(context,"PROLEMI DI RETE",Toast.LENGTH_SHORT).show();



    }


    public List<Richiesta> getRichiesteByAutista(String nomeAutista, List<Richiesta> listarichieste) {

        List<Richiesta> lista = new ArrayList<>();
        try {

            for (Richiesta richiesta : listarichieste
                    ) {
                if (richiesta.getAutista().equalsIgnoreCase(nomeAutista)) {
                    lista.add(richiesta);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, this.getClass() + " " + e);
        }

        return lista;
    }


}
