package com.example.alim.bcm;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alim.bcm.adapters.ArticoloAdapter;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.RequestManager;
import com.example.alim.bcm.utilities.TaskCompletion;

import java.util.ArrayList;
import java.util.List;

public class GestioneRichiestaActivity extends AppCompatActivity {


    private Richiesta richiesta;
    private LinearLayoutManager lm;
    private RecyclerView recyclerView;
    private TextView tNota;
    private List<Autista> listaAutisti;
    private Spinner sAutisti;
    private FrameLayout fIsAssigned;
    private FrameLayout fNotAssigned;
    private TextView tCorriere;
    private Button bAssegnaAutista;
    private boolean aggiornato= false;// se ho aggiornato la richiesta con un autista aggiorno il fragment richieste nell onbackPressed

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_richiesta);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icons8_back_arrow);

            getSupportActionBar().setTitle(null);
        }

        Intent i = getIntent();
        richiesta = (Richiesta) i.getSerializableExtra("richiesta");



        setSpinnerAutisti();

        fIsAssigned = findViewById(R.id.frameAutistaAssegnato);
        fNotAssigned = findViewById(R.id.frameAggiungiAutista);
        tCorriere = findViewById(R.id.tCorriere);
        bAssegnaAutista = findViewById(R.id.bAssegnaAutista);

        lm = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerArticolo);
        tNota = findViewById(R.id.tNota);
        tNota.setText(richiesta.getTestoLibero());
        recyclerView.setLayoutManager(lm);
        ArticoloAdapter articoloAdapter;
        if (richiesta.getListaAttrezzi().size() > 0) {
            articoloAdapter = new ArticoloAdapter(getApplication(), richiesta.getListaAttrezzi());
        } else  {
             articoloAdapter = new ArticoloAdapter(getApplication(), richiesta.getListaMateriali());
        }

        recyclerView.setAdapter(articoloAdapter);
        showFrame(richiesta.getAutista());

        bAssegnaAutista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aggiornaLayout();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("aggiornato",aggiornato);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }

    private void aggiornaLayout() {
        richiesta.setAutista(sAutisti.getSelectedItem().toString().toLowerCase());
        aggiornato = true;
        final RequestManager requestManager = RequestManager.getIstance();
        requestManager.downloadRequestfromAutista(richiesta, getApplicationContext(), new TaskCompletion() {
            @Override
            public void taskToDo(String esito, String bodyResponse) {
                // qui bisogna parsare il json e chiamare la funzione assegnaAutista

            }

            @Override
            public void taskToDo(String esito, String bodyResponse, String param1) {

            }
        });

    }

    private void showFrame(String autista) {
        if (autista.equalsIgnoreCase("NON ASSEGNATO")){

            fIsAssigned.setVisibility(View.GONE);
            fNotAssigned.setVisibility(View.VISIBLE);

        }
        else {

            tCorriere.setText(richiesta.getAutista());
            fNotAssigned.setVisibility(View.GONE);
            fIsAssigned.setVisibility(View.VISIBLE);

        }
    }

    private void setSpinnerAutisti() {

        ArrayList<String> listaNomi = new ArrayList<>();
        sAutisti = findViewById(R.id.sAutisti);
        listaAutisti = (List<Autista>) InternalStorage.readObject(getApplicationContext(),Constants.LISTA_AUTISTI);
        for (Autista a: listaAutisti
             ) {
            listaNomi.add(a.getNome().toUpperCase());
        }

        ArrayAdapter<String> spinnerAdapter =new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,R.id.tSpinner,listaNomi);
        sAutisti.setAdapter(spinnerAdapter);




    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
