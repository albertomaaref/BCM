package com.example.alim.bcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static com.example.alim.bcm.model.Constants.TIPO_UTENTE_ATTIVO;
import static com.example.alim.bcm.model.Constants.UTENTE_ATTIVO;

public class RootActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private String utenteAttivo;
    private String tipoUtenteAttivo;
    private final static String IMPIEGATO = "impiegato";
    private final static String OPERAIO = "operaio";
    private final static String CAPOCANTIERE = "capoCantiere";
    private final static String AUTISTA = "autista";
    private final static String activity = "RootActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(activity, " onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        utenteAttivo = preferences.getString(UTENTE_ATTIVO,"");
        tipoUtenteAttivo = preferences.getString(TIPO_UTENTE_ATTIVO,"");

        // active session control
        sessionControl();

    }

    private void sessionControl() {

        if (utenteAttivo.equals("")){
            // passo all'activity del login
            Intent i = new Intent(RootActivity.this,LoginActivity.class);
            startActivity(i);
        }

        else {
            switch (tipoUtenteAttivo){

                case IMPIEGATO:{

                }
                case OPERAIO:{

                }
                case CAPOCANTIERE:{

                }
                case AUTISTA:{

                }

                default: return;
            }

        }

        finish();

    }
}
