package com.example.alim.bcm.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.alim.bcm.CapoCantiereActivity;
import com.example.alim.bcm.ImpiegatoActivity;
import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.RichiestaAdapter;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Richiesta;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by alim on 29-Mar-18.
 */

public class RequestManager  {

    private TaskCompletion delegato;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private static RequestManager istanza  = null;
    private Context context;
    DatabaseReference ref;
    FirebaseDatabase database;




    private RequestManager() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
    }

    public static RequestManager getIstance (){
        if (istanza == null){
            istanza = new RequestManager();
        }
        return istanza;
    }

    public  void sendRequest(final Context context, final Richiesta richiesta, final String type, final android.support.v4.app.FragmentManager fragmentManager, final Fragment fragment) {

        // controllo se è stato scelto un cantiere
        if (richiesta.getCantiere().equalsIgnoreCase("seleziona cantiere")) {
            Toast.makeText(context, "SELEZIONARE CANTIERE", Toast.LENGTH_SHORT).show();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Procedere con la richiesta ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Activity activity = (Activity) context;
                    int idContainer = 0;
                    if (activity instanceof ImpiegatoActivity) {
                        idContainer = R.id.fragmentImpiegato;
                    } else if (activity instanceof CapoCantiereActivity) {
                        idContainer = R.id.fragmentCapo;
                    }
                    ref.child("richieste/" + richiesta.getId() + "/id").setValue(richiesta.getId());
                    ref.child("richieste/" + richiesta.getId() + "/cantiere").setValue(richiesta.getCantiere());
                    ref.child("richieste/" + richiesta.getId() + "/dataConsegna").setValue(richiesta.getDataConesgna());
                    ref.child("richieste/" + richiesta.getId() + "/nota").setValue(richiesta.getTestoLibero());
                    ref.child("richieste/" + richiesta.getId() + "/stato").setValue(richiesta.getStato());
                    ref.child("richieste/" + richiesta.getId() + "/corriere").setValue(richiesta.getAutista());





                    //int lunghezza = 0;
                if (type.equalsIgnoreCase(Constants.ATTREZZI)) {
                    ref.child("richieste/"+richiesta.getId()+"/lista"+type.toUpperCase()).setValue(richiesta.getListaAttrezzi());
                    //lunghezza = richiesta.getListaAttrezzi().size();
                    /*for (int i = 0 ; i< lunghezza; i++){
                        ref.child("richieste/"+richiesta.getId()+"/lista"+type+"/"+(i+1)).setValue(richiesta.getListaAttrezzi().get(i));
                    }*/
                }
                else if (type.equalsIgnoreCase(Constants.MATERIALI)) {
                    ref.child("richieste/"+richiesta.getId()+"/lista"+type.toUpperCase()).setValue(richiesta.getListaMateriali());

                }
                    //lunghezza  = richiesta.getListaMateriali().size();
                /*for (int i = 0 ; i<= lunghezza; i++){

                }*/

                    ItemsManager itemsManager = ItemsManager.getDownloadItems();
                    itemsManager.removeAll();
                    fragmentManager.beginTransaction().replace(idContainer, fragment).commit();

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

    public  void downloadRequests (@NonNull TaskCompletion taskCompletion){
        delegato = taskCompletion;

        FireBaseConnection.get(Constants.RICHIESTE + ".json", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                delegato.taskToDo(Constants.SUCCESSO,s);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegato.taskToDo(Constants.ERROR,String.valueOf(statusCode));

            }
        });


    }


    public void downloadRequestfromAutista(Richiesta richiesta, Context context, final TaskCompletion taskCompletion){

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        FireBaseConnection.get(Constants.UTENTI + "/" + Constants.AUTISTA + "/" + richiesta.getAutista() + ".json", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                taskCompletion.taskToDo(Constants.SUCCESSO, s);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                taskCompletion.taskToDo(Constants.ERROR,""+statusCode);
            }
        });


    }


    public void assegnaRichiesta(Richiesta richiesta, List<Integer> listaRichieste){
        ref.child("richieste/" + richiesta.getId() + "/corriere").setValue(richiesta.getAutista());
        ref.child(Constants.UTENTI+"/"+Constants.AUTISTA+"/"+richiesta.getAutista()+"/listaRichieste/").setValue(listaRichieste);
    }




}
