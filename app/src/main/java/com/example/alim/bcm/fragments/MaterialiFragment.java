package com.example.alim.bcm.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Cantiere;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.model.StatoRichiesta;
import com.example.alim.bcm.services.ConfermaDatiDialog;
import com.example.alim.bcm.services.SelectDataDialog;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.ItemsManager;
import com.example.alim.bcm.utilities.RequestManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.alim.bcm.model.Constants.CANTIERI;
import static com.example.alim.bcm.model.Constants.TAG;
import static com.example.alim.bcm.services.ConfermaDatiDialog.GENERIC_MESSAGE;

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
    private EditText eDataConsegna;


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

        eDataConsegna = view.findViewById(R.id.eDataConsegna);
        spinnerCantieri = view.findViewById(R.id.sCantieri);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);
        bAggiungiNota = view.findViewById(R.id.bAggiungiNota);
        lm = new LinearLayoutManager(getContext());
        recyclerViewMateriale = view.findViewById(R.id.recyclerMateriali);
        ItemsManager itemsManager = ItemsManager.getIstance();
        itemsManager.scaricaListArticoliFromDB(getContext(),listaCestino,recyclerViewMateriale,lm,Constants.MATERIALI);

        setSpinnerCantieri();

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
                // controllo formato data

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                richiesta.setId(sharedPreferences.getInt(Constants.ID_RICHIESTA,404)+1);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Constants.ID_RICHIESTA,sharedPreferences.getInt(Constants.ID_RICHIESTA,404)+1).commit();
                richiesta.setListaMateriali(listaCestino);
                richiesta.setDataConesgna(eDataConsegna.getText().toString());
                richiesta.setStato(StatoRichiesta.IN_ATTESA);

                // mettodialogConferma
                ConfermaDatiDialog confermaDatiDialog = new ConfermaDatiDialog(GENERIC_MESSAGE, "stai per confermare una richiesta", new ConfermaDatiDialog.onButtonClickListener() {
                    @Override
                    public void onCheckClick() {

                        MaterialiFragment fr = new MaterialiFragment();
                        RequestManager requestManager = RequestManager.getIstance();
                        requestManager.sendRequest(getContext(),richiesta,Constants.MATERIALI,getFragmentManager(),fr);
                    }

                    @Override
                    public void onCloseClick() {

                    }
                });
                confermaDatiDialog.show(getFragmentManager(),Constants.CONFERMA_DATI_DIALOG);

            }
        });

        eDataConsegna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDataDialog selectDataDialog = new SelectDataDialog(eDataConsegna);
                selectDataDialog.show(getFragmentManager(),Constants.SELECT_DATA_DIALOG);
            }
        });

    }

    public void setSpinnerCantieri (){
        List<Cantiere> cantiereList = (List<Cantiere>) InternalStorage.readObject(getContext(),CANTIERI);
        List<String> lista = new ArrayList<>();
        lista.add("Seleziona Cantiere");
        for (Cantiere cantiere: cantiereList
             ) {
            lista.add(cantiere.getIndirizzo());
        }
        if (cantiereList == null){
            Log.i(TAG,this.getClass()+" errore settaggio spinner cantieri");
        }
        else {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,R.id.tSpinner,lista);
            spinnerCantieri.setAdapter(arrayAdapter);
        }
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
