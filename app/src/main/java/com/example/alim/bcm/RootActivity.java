package com.example.alim.bcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alim.bcm.data.model.Constants;
import com.example.alim.bcm.data.model.Personale;
import com.example.alim.bcm.services.InternetController;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.ItemsManager;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.TaskCompletion;

import java.util.ArrayList;
import java.util.List;

import static com.example.alim.bcm.data.model.Constants.ATTREZZI;
import static com.example.alim.bcm.data.model.Constants.ERROR;
import static com.example.alim.bcm.data.model.Constants.MATERIALI;
import static com.example.alim.bcm.data.model.Constants.SUCCESSO;
import static com.example.alim.bcm.data.model.Constants.TIPO_UTENTE_ATTIVO;
import static com.example.alim.bcm.data.model.Constants.UTENTE_ATTIVO;
import static com.example.alim.bcm.data.model.Constants.TAG;

public class RootActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private Personale utenteAttivo = null;
    private String tipoUtenteAttivo = null;

    private final static String AUTISTA = "autista";
    private final static String activity = "RootActivity";
    private Button bRiprova;
    private TaskCompletion taskCompletion;
    private ProgressDialog progressDialog;
    private List<String> listaIdArticoli;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(activity, " onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        bRiprova = findViewById(R.id.bRiprova);

        // leggo l'utente salvato se esiste
        utenteAttivo= (Personale)InternalStorage.readObject(getApplicationContext(),UTENTE_ATTIVO);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       tipoUtenteAttivo = preferences.getString(TIPO_UTENTE_ATTIVO,"");
        int idRichiesta = preferences.getInt(Constants.ID_RICHIESTA,-1);
        if (idRichiesta == -1){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(Constants.ID_RICHIESTA,1);
            editor.commit();
        }
        controlInternetConnection();

        List<String> list = new ArrayList<>();
        list.add(ATTREZZI);
        list.add(MATERIALI);
        getIDArticoli(list);

        bRiprova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlInternetConnection();
            }
        });


    }

    private void controlInternetConnection() {
        boolean isConnected = false;
        // active session control
        isConnected = InternetController.getInsatance(getApplicationContext()).isOnline();
        if (isConnected) sessionControl();
        else Log.i(Constants.TAG," non c connessione ad Internet");
    }

    private void sessionControl() {

        if (utenteAttivo== null){
            // passo all'activity del login
            // resetto la memoria
            InternalStorage.resetDB(getApplicationContext(),"");
            Intent i = new Intent(RootActivity.this,LoginActivity.class);
            startActivity(i);
        }

        else {
            if (tipoUtenteAttivo.equals(Constants.OPERAIO)) {
                Log.i(Constants.TAG, "" + this.getClass() + " go to activity for" + tipoUtenteAttivo);
                Intent i = new Intent(RootActivity.this, OperaioActivity.class);
                startActivity(i);
                this.finish();
            } else if (tipoUtenteAttivo.equals(AUTISTA)) {
                Log.i(Constants.TAG, "" + this.getClass() + " go to activity for" + tipoUtenteAttivo);

                Intent i = new Intent(RootActivity.this, AutistaActivity.class);
                i.putExtra(AUTISTA, utenteAttivo.getNome());
                startActivity(i);
                this.finish();
            } else if (tipoUtenteAttivo.equals(Constants.IMPIEGATO)) {
                Log.i(Constants.TAG, "" + this.getClass() + " go to activity for" + tipoUtenteAttivo);

                Intent i = new Intent(RootActivity.this, ImpiegatoActivity.class);
                startActivity(i);
                this.finish();

            } else if (tipoUtenteAttivo.equals(Constants.CAPOCANTIERE)) {
                Log.i(Constants.TAG, "" + this.getClass() + " go to activity for" + tipoUtenteAttivo);

                Intent i = new Intent(RootActivity.this, CapoCantiereActivity.class);
                startActivity(i);
                this.finish();
            }

        }

        finish();

    }

    private void getIDArticoli(final List<String> lista){
        ItemsManager itemsManager = ItemsManager.getIstance(getApplicationContext());
        for (int i = 0; i<lista.size(); i++ ){
            final String tipo = lista.get(i);
            itemsManager.cariacIdArticoli(lista.get(i), new TaskCompletion() {
                @Override
                public void taskToDo(String esito, String bodyResponse) {
                    if (esito.equalsIgnoreCase(SUCCESSO)){
                        listaIdArticoli = JsonParser.getListIdArticoli(bodyResponse);
                        if (listaIdArticoli!= null && listaIdArticoli.size()>0){
                            if (tipo.equalsIgnoreCase(ATTREZZI))
                            InternalStorage.writeObject(getApplicationContext(),"id"+ATTREZZI,listaIdArticoli);
                            else if (tipo.equalsIgnoreCase(MATERIALI))
                                InternalStorage.writeObject(getApplicationContext(),"id"+MATERIALI,listaIdArticoli);
                        }
                        else Toast.makeText(getApplicationContext(),"non ci sono id articoli",Toast.LENGTH_SHORT).show();
                    }
                    else if (esito.equalsIgnoreCase(ERROR)){
                        Log.i(TAG,this.getClass()+" | getIdArticoli | problemi caricamento id");
                    }
                }

                @Override
                public void taskToDo(String esito, String bodyResponse, String param1) {

                }
            });
        }
    }





}
