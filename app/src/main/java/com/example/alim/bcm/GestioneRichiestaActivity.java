package com.example.alim.bcm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alim.bcm.adapters.ArticoloAdapter;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.utilities.ManagerSiteAndPersonal;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.SwipeController;
import com.example.alim.bcm.utilities.SwipeControllerActions;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.alim.bcm.model.Constants.TIPO_UTENTE_ATTIVO;

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
    DatabaseReference ref;
    ArticoloAdapter articoloAdapter;
    SharedPreferences preferences;
    FirebaseDatabase database;
    private Button bAssegnaAutista;
    private boolean aggiornato = false;// se ho aggiornato la richiesta con un autista aggiorno il fragment richieste nell onbackPressed

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

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
        fIsAssigned = findViewById(R.id.frameAutistaAssegnato);
        fNotAssigned = findViewById(R.id.frameAggiungiAutista);
        tCorriere = findViewById(R.id.tCorriere);
        bAssegnaAutista = findViewById(R.id.bAssegnaAutista);

        lm = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.recyclerArticolo);
        tNota = findViewById(R.id.tNota);
        tNota.setText(richiesta.getTestoLibero());
        recyclerView.setLayoutManager(lm);

        if (richiesta.getListaAttrezzi().size() > 0) {
            articoloAdapter = new ArticoloAdapter(getApplication(), richiesta.getListaAttrezzi());
        } else {
            articoloAdapter = new ArticoloAdapter(getApplication(), richiesta.getListaMateriali());
        }

        recyclerView.setAdapter(articoloAdapter);

        //getsione swipe
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }

            @Override
            public void onRightClicked(int position) {
                articoloAdapter.getListaArticoli().remove(position);
                articoloAdapter.notifyItemRemoved(position);
                articoloAdapter.notifyItemRangeChanged(position, articoloAdapter.getItemCount());
            }
        }, preferences.getString(TIPO_UTENTE_ATTIVO, ""));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


        showFrame(richiesta.getAutista());

        bAssegnaAutista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aggiornaDB()){

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("aggiornato", aggiornato);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setSpinnerAutisti();

    }

    private boolean aggiornaDB() {
        if (sAutisti.getSelectedItem().toString().equalsIgnoreCase("non ci sono autisti")){
            Toast.makeText(getApplicationContext(),"selezzionare autista",Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            richiesta.setAutista(sAutisti.getSelectedItem().toString());
            ref.child("richieste/" + richiesta.getId() + "/corriere").setValue(richiesta.getAutista());
            ref.child(Constants.UTENTI + "/" + Constants.AUTISTA + "/" + richiesta.getAutista().toLowerCase() + "/listaRichieste/" + richiesta.getId() + "/id").setValue(richiesta.getId());
            return true;
        }

    }

    private void showFrame(String autista) {
        if (autista.equalsIgnoreCase("NON ASSEGNATO")) {

            fIsAssigned.setVisibility(View.GONE);
            fNotAssigned.setVisibility(View.VISIBLE);

        } else {

            tCorriere.setText(richiesta.getAutista());
            fNotAssigned.setVisibility(View.GONE);
            fIsAssigned.setVisibility(View.VISIBLE);

        }
    }

    private void setSpinnerAutisti() {

        sAutisti = findViewById(R.id.sAutisti);
        listaAutisti = (List<Autista>) InternalStorage.readObject(getApplicationContext(), Constants.LISTA_AUTISTI);
        if (listaAutisti == null || listaAutisti.size() == 0) {
            ManagerSiteAndPersonal managerSiteAndPersonal = ManagerSiteAndPersonal.getInstance();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            managerSiteAndPersonal.getAutistiInternal(new TaskCompletion() {
                @Override
                public void taskToDo(String esito, String bodyResponse) {
                    if (esito.equalsIgnoreCase(Constants.SUCCESSO)) {
                        listaAutisti = JsonParser.getAutisti(bodyResponse);

                        setSpinner();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error caricamento autisti", Toast.LENGTH_SHORT).show();
                        Log.i(Constants.TAG, this.getClass() + "   errore caricamento autisti");

                    }
                    progressDialog.dismiss();
                    progressDialog.cancel();
                }


                @Override
                public void taskToDo(String esito, String bodyResponse, String param1) {

                }
            });
        } else setSpinner();


    }

    private void setSpinner() {
        List<String> listaNomi = new ArrayList<>();
        if (listaAutisti != null && listaAutisti.size() > 0) {
            listaNomi.add("Seleziona Autista");
            for (Autista a : listaAutisti
                    ) {
                listaNomi.add(a.getNome().toUpperCase());
            }


        } else {
            listaNomi.add("non ci sono autisti");
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.tSpinner, listaNomi);
        sAutisti.setAdapter(spinnerAdapter);

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
