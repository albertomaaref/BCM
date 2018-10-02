package com.example.alim.bcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.alim.bcm.fragments.DashboardAutistaFragment;
import com.example.alim.bcm.fragments.MapFragment;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.utilities.Geocode;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.RequestManager;
import com.example.alim.bcm.utilities.TaskCompletion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.alim.bcm.model.Constants.AUTISTA;
import static com.example.alim.bcm.model.Constants.DASHBOARD_AUTISTA_FRAGMENT;
import static com.example.alim.bcm.model.Constants.SUCCESSO;
import static com.example.alim.bcm.model.Constants.TIPO_UTENTE_ATTIVO;
import static com.example.alim.bcm.model.Constants.UTENTE_ATTIVO;

public class AutistaActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    List<Richiesta> listaRichieste;
    String autista=null;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autista);



        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       // setSupportActionBar Logout
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_logout);
            getSupportActionBar().setTitle(null);

        }

        Intent i = getIntent();
        autista = i.getStringExtra(AUTISTA);
        final RequestManager requestManager = RequestManager.getIstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        requestManager.downloadRequests(new TaskCompletion() {

            @Override
            public void taskToDo(String esito, String bodyResponse) {
                if (esito.equalsIgnoreCase(SUCCESSO)){
                   listaRichieste = JsonParser.getRichieste(bodyResponse);
                    Geocode geocode = new Geocode(AutistaActivity.this);


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("LISTA_RICHIESTE", (Serializable) listaRichieste);
                    DashboardAutistaFragment dashboardAutistaFragment = new DashboardAutistaFragment();
                    dashboardAutistaFragment.setArguments(bundle);
                    MapFragment mapFragment = new MapFragment();
                    geocode.asyncResponse = mapFragment;
                    geocode.execute(requestManager.getRichiesteByAutista(autista,listaRichieste));
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentAutista,dashboardAutistaFragment,DASHBOARD_AUTISTA_FRAGMENT).commit();
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentLocation,mapFragment).commit();

                }

                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void taskToDo(String esito, String bodyResponse, String param1) {

            }
        });


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(UTENTE_ATTIVO,"");
        editor.putString(TIPO_UTENTE_ATTIVO,"");
        editor.commit();
        InternalStorage.resetDB(getApplicationContext(),"");
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
        return true;
    }
}
