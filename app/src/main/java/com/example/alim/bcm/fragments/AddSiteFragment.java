package com.example.alim.bcm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Cantiere;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.services.SelectDataDialog;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;


public class AddSiteFragment extends Fragment {


   Button bAggiungiCantiere;
   EditText tIndirizzo;
   EditText tValore;
   EditText tCapoCantiere;
   EditText tDataInizio;
   EditText tDataFine;
   DatabaseReference ref;
   FirebaseDatabase database;


    public AddSiteFragment() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_site, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tDataFine = view.findViewById(R.id.editDataFine);
        tDataInizio = view.findViewById(R.id.editDataInizio);
        tIndirizzo = view.findViewById(R.id.editIndirizzo);
        tValore = view.findViewById(R.id.editValore);
        tCapoCantiere = view.findViewById(R.id.editCapoCantiere);
        bAggiungiCantiere = view.findViewById(R.id.bAggiungiCantiere);

        bAggiungiCantiere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCampo(tIndirizzo)|| !checkCampo(tCapoCantiere) || !checkCampo(tValore)|| !checkCampo(tDataFine) || !checkCampo(tDataInizio)){
                    Toast.makeText(getContext(),"CONTROLLARE I CAMPI", LENGTH_SHORT).show();
                }
                else {

                    insertCantierToDB(new Cantiere(0,0,0,0,tIndirizzo.getText().toString(),0,Float.parseFloat(tValore.getText().toString()),0,tCapoCantiere.getText().toString(),tDataInizio.getText().toString(),tDataFine.getText().toString()));
                    RichiesteFragment richiesteFragment = new RichiesteFragment();
                    getFragmentManager().beginTransaction().replace(R.id.fragmentImpiegato,richiesteFragment,Constants.RICHIESTE_FRAGMENT).commit();
                }
            }
        });

        tDataInizio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDataDialog dataDialog = new SelectDataDialog(tDataInizio);
                dataDialog.show(getFragmentManager(), Constants.SELECT_DATA_DIALOG);
            }
        });

        tDataFine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectDataDialog dataDialog = new SelectDataDialog(tDataFine);
                dataDialog.show(getFragmentManager(), Constants.SELECT_DATA_DIALOG);
            }
        });

    }

    private void insertCantierToDB(Cantiere cantiere) {
        ref.child("cantieri/"+tIndirizzo.getText().toString()).setValue(cantiere);
    }

    public boolean checkCampo (EditText editText){

        if (editText.getText().toString().equals(""))
            return false;
        return true;
    }



    /*public boolean isThisDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }*/
}
