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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.data.model.Attrezzo;
import com.example.alim.bcm.data.model.Autista;
import com.example.alim.bcm.data.model.Cantiere;
import com.example.alim.bcm.data.model.Constants;
import com.example.alim.bcm.data.model.Richiesta;
import com.example.alim.bcm.data.model.StatoRichiesta;
import com.example.alim.bcm.services.ConfermaDatiDialog;
import com.example.alim.bcm.services.SelectDataDialog;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.ItemsManager;
import com.example.alim.bcm.utilities.ImpiegatoTasks;
import com.example.alim.bcm.utilities.RequestManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.alim.bcm.data.model.Constants.CANTIERI;
import static com.example.alim.bcm.data.model.Constants.CONFERMA_DATI_DIALOG;
import static com.example.alim.bcm.data.model.Constants.TAG;
import static com.example.alim.bcm.services.ConfermaDatiDialog.GENERIC_MESSAGE;

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
    final private Richiesta richiesta = new Richiesta();
    private EditText eDataConsegna;
    private SwipeRefreshLayout refreshLayout;


    public AttrezzziFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ItemsManager itemsManager = ItemsManager.getIstance(getContext());
        itemsManager.scaricaListArticoliFromDB(getContext(), listaCestino, recyclerViewAttrezzi, lm, Constants.ATTREZZI);
        bApprovaRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCampi()) {

                    richiesta.setCantiere(spinnerCantieri.getSelectedItem().toString());
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    richiesta.setId(sharedPreferences.getInt(Constants.ID_RICHIESTA, 404) + 1);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.ID_RICHIESTA, sharedPreferences.getInt(Constants.ID_RICHIESTA, 404) + 1).commit();
                    richiesta.setDataConesgna(eDataConsegna.getText().toString());
                    richiesta.setListaAttrezzi(listaCestino);
                    richiesta.setStato(StatoRichiesta.IN_ATTESA);


                    ConfermaDatiDialog confermaDatiDialog = new ConfermaDatiDialog(GENERIC_MESSAGE, "stai per confermare una richiesta", new ConfermaDatiDialog.onButtonClickListener() {
                        @Override
                        public void onCheckClick() {
                            AttrezzziFragment fr = (AttrezzziFragment) new AttrezzziFragment();
                            RequestManager requestManager = RequestManager.getIstance();
                            requestManager.sendRequest(getContext(), richiesta, Constants.ATTREZZI, getFragmentManager(), fr);

                        }

                        @Override
                        public void onCloseClick() {

                        }
                    });
                    confermaDatiDialog.show(getFragmentManager(), CONFERMA_DATI_DIALOG);

                } else {
                    Toast.makeText(getContext(), "Controllare i campi", Toast.LENGTH_LONG).show();
                }
            }
        });

        bAggiungiNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });

        setSpinnerCantieri();

        eDataConsegna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDataDialog selectDataDialog = new SelectDataDialog(eDataConsegna);
                selectDataDialog.show(getFragmentManager(), Constants.SELECT_DATA_DIALOG);
            }
        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemsManager.scaricaListArticoliFromDB(getContext(), listaCestino, recyclerViewAttrezzi, lm, Constants.ATTREZZI);
                refreshLayout.setRefreshing(false);

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

        View view = inflater.inflate(R.layout.fragment_attrezzi, container, false);
        lm = new LinearLayoutManager(getContext());
        recyclerViewAttrezzi = view.findViewById(R.id.recyclerViewAttrezzi);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);
        spinnerCantieri = view.findViewById(R.id.sCantieri);
        bAggiungiNota = view.findViewById(R.id.bAggiungiNota);
        eDataConsegna = view.findViewById(R.id.eDataConsegna);
        refreshLayout = view.findViewById(R.id.refreshAttrezzi);


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

    public void setSpinnerCantieri() {
        List<Cantiere> cantiereList = (List<Cantiere>) InternalStorage.readObject(getContext(), CANTIERI);
        if (cantiereList != null && cantiereList.size() > 0) {

            List<String> lista = new ArrayList<>();
            lista.add("Seleziona Cantiere");
            for (Cantiere cantiere : cantiereList
                    ) {
                lista.add(cantiere.getIndirizzo());
            }
            if (cantiereList == null) {
                Log.i(TAG, this.getClass() + " errore settaggio spinner cantieri");
            } else {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.tSpinner, lista);
                spinnerCantieri.setAdapter(arrayAdapter);
            }
        } else {
            Toast.makeText(getContext(), "LA LAISTA DEI CANTIEREI E VUOTA", Toast.LENGTH_SHORT).show();
        }
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

    private boolean checkCampi() {
        if (spinnerCantieri.getCount() == 0 || listaCestino == null || listaCestino.size() < 1 || eDataConsegna.getText().toString().equalsIgnoreCase("")) {
            return false;
        } else if (spinnerCantieri.getSelectedItem().toString().equalsIgnoreCase("Seleziona Cantiere"))
            return false;
        else return true;
    }


}
