package com.example.alim.bcm.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.alim.bcm.data.model.Cantiere;
import com.example.alim.bcm.data.model.CapoCantiere;
import com.example.alim.bcm.data.model.Constants;
import com.example.alim.bcm.services.ConfermaDatiDialog;
import com.example.alim.bcm.services.SelectDataDialog;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.ManagerSiteAndPersonal;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.alim.bcm.data.model.Constants.CANTIERI;
import static com.example.alim.bcm.data.model.Constants.CAPOCANTIERE;
import static com.example.alim.bcm.data.model.Constants.CONFERMA_DATI_DIALOG;
import static com.example.alim.bcm.data.model.Constants.ERROR;
import static com.example.alim.bcm.data.model.Constants.SUCCESSO;
import static com.example.alim.bcm.data.model.Constants.TAG;


public class AddSiteFragment extends Fragment {


    Button bAggiungiCantiere;
    EditText tIndirizzo;
    EditText tValore;
    EditText tDataInizio;
    EditText tDataFine;
    EditText tCitta;
    Spinner spinnerCapoCantiere;
    DatabaseReference ref;
    FirebaseDatabase database;
    private ProgressDialog progressDialog;
    private List<CapoCantiere> listCapocantiere;


    public AddSiteFragment() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
    }


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
        spinnerCapoCantiere = view.findViewById(R.id.spinnerCapoCantiere);
        tCitta = view.findViewById(R.id.editCitta);
        bAggiungiCantiere = view.findViewById(R.id.bAggiungiCantiere);

        setSpinnerCapoCantiere();

        bAggiungiCantiere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!checkCampo(tIndirizzo) || !checkCampo(tCitta) || !checkCampo(tValore) || !checkCampo(tDataFine) || !checkCampo(tDataInizio)) && !spinnerCapoCantiere.getSelectedItem().toString().equalsIgnoreCase("selezionare")) {
                    Toast.makeText(getContext(), "CONTROLLARE I CAMPI", LENGTH_SHORT).show();
                } else {

                    ConfermaDatiDialog confermaDatiDialog = new ConfermaDatiDialog(ConfermaDatiDialog.GENERIC_MESSAGE, "stai per inserire un nuovo cantiere", new ConfermaDatiDialog.onButtonClickListener() {
                        @Override
                        public void onCheckClick() {
                            Cantiere c = new Cantiere(0, 0, 0, 0, tIndirizzo.getText().toString(), tCitta.getText().toString(), 0, Float.parseFloat(tValore.getText().toString()), 0, spinnerCapoCantiere.getSelectedItem().toString(), tDataInizio.getText().toString(), tDataFine.getText().toString());
                            List<Cantiere> list = (List<Cantiere>) InternalStorage.readObject(getContext(),CANTIERI);
                            if (list!= null && list.size()>0){

                                InternalStorage.writeObject(getContext(),CANTIERI,list);
                            }
                            else {
                                list = new ArrayList<>();
                                list.add(c);
                                InternalStorage.writeObject(getContext(),CANTIERI,list);
                            }
                            insertCantierToDB(c);
                            RichiesteFragment richiesteFragment = new RichiesteFragment();
                            getFragmentManager().beginTransaction().replace(R.id.fragmentImpiegato, richiesteFragment, Constants.RICHIESTE_FRAGMENT).commit();
                        }

                        @Override
                        public void onCloseClick() {

                        }
                    });

                    confermaDatiDialog.show(getFragmentManager(), CONFERMA_DATI_DIALOG);
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
        ref.child("cantieri/" + tIndirizzo.getText().toString()).setValue(cantiere);
        ref.child("utenti/"+CAPOCANTIERE+"/"+spinnerCapoCantiere.getSelectedItem().toString()+"/"+CANTIERI).setValue(cantiere.getIndirizzo()+" "+cantiere.getCitta());
    }

    public boolean checkCampo(EditText editText) {

        if (editText.getText().toString().equals(""))
            return false;
        return true;
    }


    private void setSpinnerCapoCantiere() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        listCapocantiere = new ArrayList<>();
        listCapocantiere = (List<CapoCantiere>) InternalStorage.readObject(getContext(), CAPOCANTIERE);
        if (listCapocantiere == null || listCapocantiere.size() == 0) {
            ManagerSiteAndPersonal.getInstance().getBosses(new TaskCompletion() {
                @Override
                public void taskToDo(String esito, String bodyResponse) {
                    if (esito.equalsIgnoreCase(SUCCESSO)) {
                        String s = new String(bodyResponse);
                        listCapocantiere = JsonParser.getBosses(s);

                        if (listCapocantiere != null && listCapocantiere.size() > 0) {
                            List<String> listaNomi = new ArrayList<>();
                            listaNomi.add("Selezionare CapoCantiere");
                            for (CapoCantiere capo : listCapocantiere
                                    ) {
                                listaNomi.add(capo.getNome());
                            }
                            spinnerCapoCantiere.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_white_item, R.id.spinnerRegistration, listaNomi));
                        } else {
                            Log.i(TAG, "lista capi vuota");
                            List<String> listaNomi = new ArrayList<>();
                            listaNomi.add("non ci sono capi");
                            spinnerCapoCantiere.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.spinner_white_item, R.id.spinnerRegistration, listaNomi));

                        }
                    } else if (esito.equalsIgnoreCase(ERROR)) {
                        Log.i(TAG, this.getClass() + " setSpinnerCapoCantiere | errore caricamento capocantieri");
                        Toast.makeText(getContext(), "errore caricamento capo cantieri", LENGTH_SHORT).show();
                    }
                    if (progressDialog.isShowing()){

                        progressDialog.dismiss();
                        progressDialog.cancel();
                    }
                }

                @Override
                public void taskToDo(String esito, String bodyResponse, String param1) {

                }
            });
        }
        else {
            List<String> listaNomi = new ArrayList<>();
            listaNomi.add("Selezionare");
            for (CapoCantiere capo : listCapocantiere
                    ) {
                listaNomi.add(capo.getNome());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_white_item,R.id.spinnerRegistration, listaNomi);
            spinnerCapoCantiere.setAdapter(arrayAdapter);
            if (progressDialog.isShowing()){

                progressDialog.dismiss();
                progressDialog.cancel();
            }
        }
    }
}
