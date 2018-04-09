package com.example.alim.bcm.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alim.bcm.GestioneRichiestaActivity;
import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.RichiestaAdapter;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.model.StatoRichiesta;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.RequestManager;
import com.example.alim.bcm.utilities.TaskCompletion;

import java.util.ArrayList;
import java.util.List;


public class RichiesteFragment extends Fragment {

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    Spinner sCantiere;

    TextView tNumRichieste;
    TextView tNumAttesa;
    TextView tNumConsegna;
    List<Richiesta> richiestaList = new ArrayList<>();
    RichiestaAdapter richiestaAdapter;


    public RichiesteFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_richieste, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerRichieste);
        layoutManager = new LinearLayoutManager(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();

        tNumRichieste = view.findViewById(R.id.tNumRichieste);
        tNumAttesa = view.findViewById(R.id.tNumAttesa);
        tNumConsegna = view.findViewById(R.id.tNumConsegna);

        sCantiere = view.findViewById(R.id.sCantiere);


        sCantiere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtroLista(sCantiere.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        RequestManager requestManager = RequestManager.getIstance();
        requestManager.downloadRequests(new TaskCompletion() {
            @Override
            public void taskToDo(String esito, String bodyResponse) {
                if (esito.equals(Constants.SUCCESSO)){

                    richiestaList = JsonParser.getRichieste(bodyResponse);

                    Log.i(Constants.TAG,""+this.getClass());
                    if (richiestaList != null && richiestaList.size()>0){
                         richiestaAdapter = new RichiestaAdapter(getContext(), richiestaList, new RichiestaAdapter.OnClickCardListener() {
                            @Override
                            public void onclickCard(Richiesta richiesta) {
                                Intent intent = new Intent(getContext(), GestioneRichiestaActivity.class);
                                intent.putExtra("richiesta", richiesta);
                                startActivity(intent);
                            }
                        });
                         setTextView(richiestaList);

                    }
                    else {
                        Toast.makeText(getContext(),"Errore caricamento richieste",Toast.LENGTH_LONG).show();
                    }

                }
                else if (esito.equals(Constants.ERROR)){

                }

                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void taskToDo(String esito, String bodyResponse, String param1) {

            }
        });
        //RequestManager.getIstance().downloadRequests(getContext(),layoutManager,recyclerView);
        super.onViewCreated(view, savedInstanceState);
    }

    private void filtroLista(String cantiere) {
        List<Richiesta> listaFiltrata = new ArrayList<>();
        if (!cantiere.equalsIgnoreCase("seleziona cantiere")){


            for (Richiesta richiesta: richiestaList
                    ) {
                if (richiesta.getCantiere().equalsIgnoreCase(cantiere)){
                    listaFiltrata.add(richiesta);
                }
            }
        }

        if (listaFiltrata.size()>0){

            richiestaAdapter = new RichiestaAdapter(getContext(), listaFiltrata, new RichiestaAdapter.OnClickCardListener() {
                @Override
                public void onclickCard(Richiesta richiesta) {
                    Intent intent = new Intent(getContext(), GestioneRichiestaActivity.class);
                    intent.putExtra("richiesta", richiesta);
                    startActivity(intent);
                }
            });

            setTextView(listaFiltrata);

            recyclerView.setAdapter(richiestaAdapter);
        }



    }

    public void setTextView(List<Richiesta> list){
        int inAttesa = 0;
        int inConesgna = 0;
        for (Richiesta richiesta: list
                ) {
            if (richiesta.getStato().equals(StatoRichiesta.IN_ATTESA))inAttesa++;
            else if (richiesta.getStato().equals(StatoRichiesta.IN_CONSEGNA))inConesgna++;
        }
        tNumAttesa.setText(""+inAttesa);
        tNumConsegna.setText(""+inConesgna);
        tNumRichieste.setText(""+list.size());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(richiestaAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
