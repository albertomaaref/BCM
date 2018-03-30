package com.example.alim.bcm.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Articolo;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.utilities.DownloadItems;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.RequestManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RichiesteFragment extends Fragment  {

    private Button bApprovaRichiesta;
    private Button bAggiungiNota;
    private RecyclerView recyclerViewMateriali;
    private RecyclerView recyclerViewAttrezzi;
    private LinearLayoutManager lm1;
    private LinearLayoutManager lm2;

    private List<? extends Articolo> listaCestino = new ArrayList<>();
    private DatabaseReference ref;
    private FirebaseDatabase database;
    private Spinner spinnerRichieste;
    private Spinner spinnerCantieri;
    private final Richiesta richiesta = new Richiesta();
    private static final String MATERIALI = "MATERIALI";
    private static final String ATTREZZI = "ATTREZZI";
    private boolean start = false;
    int check ;
    private FrameLayout frameLayoutAttrezzi;
    private FrameLayout frameLayoutMateriali;



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

        lm1 = new LinearLayoutManager(getContext());
        lm2 = new LinearLayoutManager(getContext());
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
        recyclerViewAttrezzi = view.findViewById(R.id.recyclerViewAttrezzi);
        recyclerViewMateriali = view.findViewById(R.id.recyclerMateriali);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);
        bAggiungiNota = view.findViewById(R.id.bAggiungiNota);
        spinnerRichieste = view.findViewById(R.id.sFragrichieste);
        spinnerCantieri = view.findViewById(R.id.sCantieri);
        frameLayoutMateriali = view.findViewById(R.id.frameMateriali);
        frameLayoutAttrezzi = view.findViewById(R.id.frameAttrezzi);

        bApprovaRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RichiesteFragment fr = new RichiesteFragment();

                if (spinnerRichieste.getSelectedItem().toString().equals(ATTREZZI)){
                    richiesta.setCantiere(spinnerCantieri.getSelectedItem().toString());
                    richiesta.setId(10);
                    richiesta.setListaAttrezzi((ArrayList<Attrezzo>) listaCestino);
                    RequestManager.sendRequest(getContext(),richiesta,Constants.ATTREZZI, getFragmentManager(),fr);
                }
                else if (spinnerRichieste.getSelectedItem().toString().equals(MATERIALI)){
                    richiesta.setCantiere(spinnerCantieri.getSelectedItem().toString());
                    richiesta.setId(10);
                    richiesta.setListaMateriali((ArrayList<Materiale>) listaCestino);

                    RequestManager.sendRequest(getContext(),richiesta,Constants.MATERIALI,getFragmentManager(),fr);

                }
                else {
                    Toast.makeText(getContext(),"ERROR",Toast.LENGTH_SHORT).show();
                }

            }
        });

        bAggiungiNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });






        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("alimaaref","onViewCreated "+ this.getClass());
        super.onViewCreated(view, savedInstanceState);
        check=0;
        spinnerRichieste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (true){
                    if (spinnerRichieste.getSelectedItem().toString().equals(ATTREZZI)){
                        DownloadItems downloadItems = DownloadItems.getDownloadItems();
                        downloadItems.scaricaListFromDB(getContext(),listaCestino,recyclerViewAttrezzi, lm1,Constants.ATTREZZI);

                        frameLayoutMateriali.setVisibility(View.GONE);
                        frameLayoutAttrezzi.setVisibility(View.VISIBLE);
                    }
                    else if (spinnerRichieste.getSelectedItem().toString().equals(MATERIALI)){
                        DownloadItems downloadItems = DownloadItems.getDownloadItems();
                        downloadItems.scaricaListFromDB(getContext(),listaCestino,recyclerViewMateriali, lm2,Constants.MATERIALI);

                        frameLayoutAttrezzi.setVisibility(View.GONE);
                        frameLayoutMateriali.setVisibility(View.VISIBLE);
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    public void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.aggiungi_nota, null);
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
