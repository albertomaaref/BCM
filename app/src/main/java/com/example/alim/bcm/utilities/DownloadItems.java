package com.example.alim.bcm.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.alim.bcm.adapters.AttrezzoAdapter;
import com.example.alim.bcm.adapters.MaterialeAdapter;
import com.example.alim.bcm.model.Articolo;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Materiale;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by alim on 29-Mar-18.
 */

public class DownloadItems implements TaskCompletion {

    private TaskCompletion delegato;
    private ProgressDialog progressDialog;
    private List<? extends  Articolo> listaCestino ;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private static DownloadItems istanza = null;

    private DownloadItems (){}

    private Context context;



    private AttrezzoAdapter attrezzoAdapter;
    private MaterialeAdapter materialeAdapter;

    public static DownloadItems getDownloadItems(){
        if (istanza == null){
            istanza = new DownloadItems();
        }

        return istanza;
    }

    public  void scaricaListFromDB(final Context context, final List<? extends Articolo> listaCestino, final RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager, final String tipologia){
        delegato = this;
        this.listaCestino = listaCestino;
        this.context = context;
        this.layoutManager = layoutManager;
        this.recyclerView = recyclerView;
        recyclerView.setVisibility(View.GONE);


        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        FireBaseConnection.get(tipologia, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                delegato.taskToDo(Constants.SUCCESSO,s,tipologia);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegato.taskToDo(Constants.ERROR,new String(responseBody),tipologia);
            }
        });


    }

    @Override
    public void taskToDo(String esito, String bodyResponse) {

    }

    @Override
    public void taskToDo(String esito, String bodyResponse, String param1) {
        progressDialog.dismiss();
        progressDialog.cancel();
        if (esito.equals(Constants.SUCCESSO)){
            if  ( param1.equals(Constants.ATTREZZI)){
                final List<? extends Articolo> lista ;
                lista = JsonParser.getAttrezzi(bodyResponse);
                attrezzoAdapter =new AttrezzoAdapter(context, (List<Attrezzo>) lista, new AttrezzoAdapter.OnAttrezzoClickListener() {
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


            }else if (param1.equals(Constants.MATERIALI)){
                final List<? extends Articolo> lista ;
                lista = JsonParser.getMateriali(bodyResponse);
                materialeAdapter = new MaterialeAdapter((List<Materiale>) lista, context, new MaterialeAdapter.OnMaterialeClickListener() {
                    @Override
                    public void onMaterialeCheck(Materiale materiale) {
                        List<Materiale> list =  new ArrayList<>();
                        list = (List<Materiale>) listaCestino;
                        list.add(materiale);
                        listaCestino = list;                    }

                    @Override
                    public void onMaterialeUncheck(Materiale materiale) {
                        List<Materiale> list =  new ArrayList<>();
                        list = (List<Materiale>) listaCestino;
                        list.remove(materiale);
                        listaCestino = list;
                    }
                });
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(materialeAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            }




        }
        else if (esito.equals(Constants.ERROR)){
            Toast.makeText(context,"ERROE NEL DOWNLOAD DEGLI ATTREZZI", Toast.LENGTH_SHORT).show();
        }
    }

    public List<? extends Articolo> getListaCestino() {
        return listaCestino;
    }

    public void setListaCestino(List<? extends Articolo> listaCestino) {
        this.listaCestino = listaCestino;
    }

    public void removeAll(){
        listaCestino.removeAll(listaCestino);
    }
}
