package com.example.alim.bcm.fragments;

import android.app.AlertDialog;
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
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.utilities.ItemsManager;
import com.example.alim.bcm.utilities.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the


 */
public class MaterialiFragment extends Fragment {

    private Button bApprovaRichiesta;
    private Button bAggiungiNota;
    private LinearLayoutManager lm;
    private RecyclerView recyclerViewMateriale;
    private List<Materiale> listaCestino = new ArrayList<>();
    private Richiesta richiesta = new Richiesta();
    private Spinner spinnerCantieri;


    public MaterialiFragment() {
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
        return inflater.inflate(R.layout.fragment_materiali, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        spinnerCantieri = view.findViewById(R.id.sCantieri);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);
        bAggiungiNota = view.findViewById(R.id.bAggiungiNota);
        lm = new LinearLayoutManager(getContext());
        recyclerViewMateriale = view.findViewById(R.id.recyclerMateriali);
        ItemsManager itemsManager = ItemsManager.getDownloadItems();
        itemsManager.scaricaListFromDB(getContext(),listaCestino,recyclerViewMateriale,lm,Constants.MATERIALI);

        bAggiungiNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        bApprovaRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                richiesta.setCantiere(spinnerCantieri.getSelectedItem().toString());
                richiesta.setId(10);
                richiesta.setListaMateriali(listaCestino);
                //richiesta.setTestoLibero();
                MaterialiFragment fr = (MaterialiFragment) getFragmentManager().findFragmentById(R.id.fragmentImpiegato);
                RequestManager.sendRequest(getContext(),richiesta,Constants.MATERIALI,getFragmentManager(),fr);
            }
        });


    }





    @Override
    public void onDetach() {
        super.onDetach();

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
