package com.example.alim.bcm.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.alim.bcm.data.model.Articolo;
import com.example.alim.bcm.data.model.Attrezzo;
import com.example.alim.bcm.data.model.CapoCantiere;
import com.example.alim.bcm.data.model.Constants;
import com.example.alim.bcm.data.model.Materiale;
import com.example.alim.bcm.data.model.Richiesta;
import com.example.alim.bcm.data.model.StatoRichiesta;
import com.example.alim.bcm.services.SelectDataDialog;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.ItemsManager;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.RequestManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.alim.bcm.data.model.Constants.ATTREZZI;
import static com.example.alim.bcm.data.model.Constants.MATERIALI;
import static com.example.alim.bcm.data.model.Constants.UTENTE_ATTIVO;

/**
 * A simple {@link Fragment} subclass.
 */
public class CapoDemandFragment extends Fragment {

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

    private boolean start = false;
    int check;
    private FrameLayout frameLayoutAttrezzi;
    private FrameLayout frameLayoutMateriali;
    private EditText eDataConsegna;
    private CapoCantiere capoCantiere = null;

    private SwipeRefreshLayout refreshAttrezzi;
    private SwipeRefreshLayout refreshMateriali;


    public CapoDemandFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        capoCantiere = (CapoCantiere) InternalStorage.readObject(getContext(), UTENTE_ATTIVO);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_capo_demand, container, false);

        lm1 = new LinearLayoutManager(getContext());
        lm2 = new LinearLayoutManager(getContext());
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
        recyclerViewAttrezzi = view.findViewById(R.id.recyclerViewAttrezzi);
        recyclerViewMateriali = view.findViewById(R.id.recyclerMateriali);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);
        bAggiungiNota = view.findViewById(R.id.bAggiungiNota);
        spinnerCantieri = view.findViewById(R.id.sCantieri);
        spinnerRichieste = view.findViewById(R.id.sFragrichieste);
       /* frameLayoutMateriali = view.findViewById(R.id.frameMateriali);
        frameLayoutAttrezzi = view.findViewById(R.id.frameAttrezzi);*/
        eDataConsegna = view.findViewById(R.id.eDataConsegna);
        refreshAttrezzi = view.findViewById(R.id.refreshAttrezzi);
        refreshMateriali = view.findViewById(R.id.refreshMateriali);

        // controllo se il capoCantiere ha un cantiere in carico

        /*if (capoCantiere != null && capoCantiere.getCantiere() != null && !capoCantiere.getCantiere().equalsIgnoreCase("")) {
            bAggiungiNota.setEnabled(false);
            bApprovaRichiesta.setEnabled(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        }*/

        bApprovaRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capoCantiere != null && capoCantiere.getCantiere() != null && !capoCantiere.getCantiere().equalsIgnoreCase("")) {

                    CapoDemandFragment fr = new CapoDemandFragment();
                    richiesta.setCantiere(capoCantiere.getCantiere());
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    richiesta.setId(sharedPreferences.getInt(Constants.ID_RICHIESTA, 404) + 1);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.ID_RICHIESTA, sharedPreferences.getInt(Constants.ID_RICHIESTA, 404) + 1).commit();
                    richiesta.setDataConesgna(eDataConsegna.getText().toString());
                    richiesta.setCantiere(capoCantiere.getCantiere());
                    richiesta.setStato(StatoRichiesta.IN_ATTESA);
                    String type = "";
                    if (spinnerRichieste.getSelectedItem().toString().equalsIgnoreCase(ATTREZZI)) {
                        type = ATTREZZI;
                        richiesta.setListaAttrezzi((ArrayList<Attrezzo>) listaCestino);
                    } else if (spinnerRichieste.getSelectedItem().toString().equalsIgnoreCase(MATERIALI)) {
                        type = MATERIALI;
                        richiesta.setListaMateriali((ArrayList<Materiale>) listaCestino);
                    }
                    RequestManager requestManager = RequestManager.getIstance();
                    requestManager.sendRequest(getContext(), richiesta, type, getFragmentManager(), fr);
                } else
                    Toast.makeText(getContext(), "non sei in nessun cantiere", Toast.LENGTH_SHORT).show();


            }
        });

        refreshAttrezzi.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ItemsManager itemsManager = ItemsManager.getIstance(getContext());
                itemsManager.scaricaListArticoliFromDB(getContext(), listaCestino, recyclerViewAttrezzi, lm1, ATTREZZI);


                refreshAttrezzi.setRefreshing(false);
            }
        });

        refreshMateriali.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ItemsManager itemsManager = ItemsManager.getIstance(getContext());
                itemsManager.scaricaListArticoliFromDB(getContext(), listaCestino, recyclerViewAttrezzi, lm1, MATERIALI);
                refreshMateriali.setRefreshing(false);
            }
        });

        bAggiungiNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        eDataConsegna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDataDialog selectDataDialog = new SelectDataDialog(eDataConsegna);
                selectDataDialog.show(getFragmentManager(), Constants.SELECT_DATA_DIALOG);
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("alimaaref", "onViewCreated " + this.getClass());
        super.onViewCreated(view, savedInstanceState);
        check = 0;
        spinnerRichieste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (true) {
                    if (spinnerRichieste.getSelectedItem().toString().equalsIgnoreCase(ATTREZZI)) {
                        ItemsManager itemsManager = ItemsManager.getIstance(getContext());
                        itemsManager.scaricaListArticoliFromDB(getContext(), listaCestino, recyclerViewAttrezzi, lm1, ATTREZZI);

                        refreshMateriali.setVisibility(View.GONE);
                        refreshAttrezzi.setVisibility(View.VISIBLE);
                    } else if (spinnerRichieste.getSelectedItem().toString().equalsIgnoreCase(MATERIALI)) {
                        ItemsManager itemsManager = ItemsManager.getIstance(getContext());
                        itemsManager.scaricaListArticoliFromDB(getContext(), listaCestino, recyclerViewMateriali, lm2, MATERIALI);

                        refreshAttrezzi.setVisibility(View.GONE);
                        refreshMateriali.setVisibility(View.VISIBLE);
                        recyclerViewAttrezzi.setVisibility(View.GONE);

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
