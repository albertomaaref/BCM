package com.example.alim.bcm.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.utilities.ItemsManager;
import com.example.alim.bcm.utilities.ImpiegatoTasks;
import com.example.alim.bcm.utilities.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AttrezzziFragment extends Fragment implements ImpiegatoTasks {


    private Button bApprovaRichiesta;
    private Button bAggiungiNota;
    private RecyclerView recyclerViewAttrezzi;
    private LinearLayoutManager lm;
    private List<Attrezzo> listaCestino = new ArrayList<>();
    private Spinner spinnerCantieri;
    final  private Richiesta richiesta = new Richiesta();




    public AttrezzziFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ItemsManager itemsManager = ItemsManager.getDownloadItems();
        itemsManager.scaricaListFromDB(getContext(),listaCestino,recyclerViewAttrezzi,lm, Constants.ATTREZZI);
        bApprovaRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richiesta.setCantiere(spinnerCantieri.getSelectedItem().toString());
                richiesta.setId(10);
                richiesta.setListaAttrezzi(listaCestino);
                //richiesta.setTestoLibero();
                AttrezzziFragment fr = (AttrezzziFragment) new AttrezzziFragment();
                RequestManager.sendRequest(getContext(),richiesta,Constants.ATTREZZI,getFragmentManager(),fr);
            }
        });

        bAggiungiNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lm = new LinearLayoutManager(getContext());



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_attrezzi, container, false);
        lm = new LinearLayoutManager(getContext());
        recyclerViewAttrezzi = view.findViewById(R.id.recyclerViewAttrezzi);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);
        spinnerCantieri = view.findViewById(R.id.sCantieri);
        bAggiungiNota = view.findViewById(R.id.bAggiungiNota);

        return view;

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void comunicateObjectsToDriver(List<Object> objects, Autista autista) {

    }

    public void showInputDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.aggiungi_nota,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);
        final EditText nota = promptView.findViewById(R.id.eAggiungiNota);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                richiesta.setTestoLibero(nota.getText().toString());
            }
        });

        alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }







}
