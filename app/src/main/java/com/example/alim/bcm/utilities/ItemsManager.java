package com.example.alim.bcm.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.alim.bcm.adapters.AttrezzoAdapter;
import com.example.alim.bcm.adapters.MaterialeAdapter;
import com.example.alim.bcm.model.Articolo;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.services.InternetController;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.example.alim.bcm.model.Constants.ATTREZZI;
import static com.example.alim.bcm.model.Constants.MATERIALI;
import static com.example.alim.bcm.model.Constants.SUCCESSO;
import static com.example.alim.bcm.model.Constants.TAG;

/**
 * Created by alim on 29-Mar-18.
 */

public class ItemsManager implements TaskCompletion {

    private TaskCompletion delegato;
    private ProgressDialog progressDialog;
    private List<? extends Articolo> listaCestino;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private static ItemsManager istanza = null;

    private ItemsManager(Context context) {
        this.context = context;
    }

    private Context context;


    private AttrezzoAdapter attrezzoAdapter;
    private MaterialeAdapter materialeAdapter;

    public static ItemsManager getIstance(Context context) {
        if (istanza == null) {
            istanza = new ItemsManager(context);
        }

        return istanza;
    }

    public void scaricaListArticoliFromDB(final Context context, final List<? extends Articolo> listaCestino, final RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager, final String tipologia) {
        delegato = this;
        this.listaCestino = listaCestino;
        this.context = context;
        this.layoutManager = layoutManager;
        this.recyclerView = recyclerView;


        // controllo internet connection

        if (InternetController.getInsatance(context).isOnline()){

            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            List <Attrezzo> listaAttrezzo = (List<Attrezzo>) InternalStorage.readObject(context,ATTREZZI);
            List <Materiale> listaMateriale = (List<Materiale>) InternalStorage.readObject(context,MATERIALI);

            if (listaAttrezzo != null && listaAttrezzo.size()>0 && tipologia.equalsIgnoreCase(ATTREZZI)){
                setRecyclerAttrezzi(listaAttrezzo);
            }
            else if (listaMateriale != null && listaMateriale.size()>0 && tipologia.equalsIgnoreCase(MATERIALI)){
                setRecyclerMateriali(listaMateriale);
            }
            else {

                FireBaseConnection.get(tipologia + ".json", null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s = new String(responseBody);
                        delegato.taskToDo(Constants.SUCCESSO, s, tipologia);


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        delegato.taskToDo(Constants.ERROR, new String(responseBody), tipologia);
                    }
                });
            }
        }

        else Toast.makeText(context,"PROLEMI DI RETE",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void taskToDo(String esito, String bodyResponse) {

    }

    @Override
    public void taskToDo(String esito, String bodyResponse, String param1) {
        progressDialog.dismiss();
        progressDialog.cancel();
        if (esito.equals(Constants.SUCCESSO)) {
            if (param1.equals(ATTREZZI)) {
                final List<? extends Articolo> lista;
                lista = JsonParser.getAttrezzi(bodyResponse);
                if (lista!=null && lista.size()>0){

                    InternalStorage.writeObject(context,ATTREZZI,lista);
                    setRecyclerAttrezzi((List<Attrezzo>) lista);
                }

                else Log.i(TAG,this.getClass()+" | scaricaListaArticoliFromDB | lista Attrezzi vuota");


            } else if (param1.equals(Constants.MATERIALI)) {
                final List<? extends Articolo> lista;
                lista = JsonParser.getMateriali(bodyResponse);
                if (lista != null && lista.size()>0) {

                    InternalStorage.writeObject(context,MATERIALI,lista);
                    setRecyclerMateriali((List<Materiale>) lista);
                }
                else Log.i(TAG,this.getClass()+" | scaricaListaArticoliFromDB | lista materiali vuota");
            }


        } else if (esito.equals(Constants.ERROR)) {
            Toast.makeText(context, "ERROE NEL DOWNLOAD DEGLI ATTREZZI", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecyclerMateriali(List<Materiale> lista) {
        progressDialog.dismiss();
        progressDialog.cancel();
        materialeAdapter = new MaterialeAdapter(lista, context, new MaterialeAdapter.OnMaterialeClickListener() {
            @Override
            public void onMaterialeCheck(Materiale materiale) {
                if (materiale.getQuantita() < 1) {
                    Toast.makeText(context, "CONTROLLARE QUANTITA", Toast.LENGTH_SHORT).show();

                } else {
                    List<Materiale> list = new ArrayList<>();
                    list = (List<Materiale>) listaCestino;
                    list.add(materiale);
                    listaCestino = list;
                }
            }

            @Override
            public void onMaterialeUncheck(Materiale materiale) {
                List<Materiale> list = new ArrayList<>();
                list = (List<Materiale>) listaCestino;
                list.remove(materiale);
                listaCestino = list;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(materialeAdapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void setRecyclerAttrezzi(List<Attrezzo> lista) {
        progressDialog.dismiss();
        progressDialog.cancel();
        attrezzoAdapter = new AttrezzoAdapter(context, lista, new AttrezzoAdapter.OnAttrezzoClickListener() {
            @Override
            public void onAttrezzoCheck(Attrezzo attrezzo) {

                List<Attrezzo> list = new ArrayList<>();
                list = (List<Attrezzo>) listaCestino;
                list.add(attrezzo);
                listaCestino = list;
            }

            @Override
            public void onAttrezzoUncheck(Attrezzo attrezzo) {
                List<Attrezzo> list = new ArrayList<>();
                list = (List<Attrezzo>) listaCestino;
                list.remove(attrezzo);
                listaCestino = list;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(attrezzoAdapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public List<? extends Articolo> getListaCestino() {
        return listaCestino;
    }

    public void setListaCestino(List<? extends Articolo> listaCestino) {
        this.listaCestino = listaCestino;
    }

    public void removeAll() {
        listaCestino.removeAll(listaCestino);
    }

    public void cariacIdArticoli (String articolo,@NonNull TaskCompletion taskCompletion){


        final TaskCompletion finalTaskCompletion = taskCompletion;
        FireBaseConnection.get(articolo + ".json", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                finalTaskCompletion.taskToDo(SUCCESSO,s);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
