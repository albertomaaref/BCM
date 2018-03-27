package com.example.alim.bcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.alim.bcm.model.CapoCantiere;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.concurrent.CompletionService;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity implements TaskCompletion {

    @BindView(R.id.eUsername)
    EditText username;

    @BindView(R.id.ePassword)
    EditText password;

    @BindView(R.id.bLogin)
    Button login;

    @BindView(R.id.bRegistrati)
    FloatingActionButton registrati;

    @BindView(R.id.qualifica)
    RadioGroup rQualifica;

    @BindView(R.id.rAutista)
    RadioButton rAutista;

    @BindView(R.id.rImpiegato)
    RadioButton rImpiegato;

    @BindView(R.id.rCapoCantiere)
    RadioButton rCapocantiere;

    @BindView(R.id.rOperaio)
    RadioButton rOperaio;

    private SharedPreferences preferences;
    private TaskCompletion delegation;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        delegation = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ButterKnife.bind(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin(username, password);
            }
        });

        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToregister();
            }
        });


    }

    private void tryLogin(EditText username, EditText password) {

        if (isEditTextEmpty(username) || isEditTextEmpty(password) || rQualifica.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "CONTROLLARE I CAMPI", Toast.LENGTH_SHORT).show();
        } else {
            int id = rQualifica.getCheckedRadioButtonId();
            String type = "";
            if (id == rAutista.getId()) {
                type = Constants.AUTISTA;
            } else if (id == rCapocantiere.getId()) {
                type = Constants.CAPOCANTIERE;
            } else if (id == rImpiegato.getId()) {
                type = Constants.IMPIEGATO;
            } else if (id == rOperaio.getId()) {
                type = Constants.OPERAIO;
            }

            restCallForLogin(delegation, username.getText().toString(), type);
        }


    }

    private void restCallForLogin(final TaskCompletion delegation, String username, final String qualifica) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("CARICAMENTO IN CORSO");
        progressDialog.show();
        String url = Constants.UTENTI + "/" + qualifica + "/" + username + ".json";
        FireBaseConnection.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                String psw = JsonParser.getPassword(s);
                delegation.taskToDo(Constants.SUCCESSO, psw, qualifica);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegation.taskToDo(Constants.ERROR, "connessione fallita", qualifica);
            }
        });
    }

    private void goToregister() {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void taskToDo(String statusCode, String psw) {

    }

    @Override
    public void taskToDo(String esito, String bodyResponse, String qualifica) {
        progressDialog.dismiss();
        progressDialog.cancel();
        if (esito.equals(Constants.ERROR)) {

        } else if (esito.equals(Constants.SUCCESSO)) {
            if (bodyResponse.equals(password.getText().toString())) {

                // salvare utenza attiva
                /*SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.UTENTE_ATTIVO,username.getText().toString());
                editor.putString(Constants.TIPO_UTENTE_ATTIVO,psw);
                editor.commit();*/

                switch (qualifica) {
                    case Constants.AUTISTA: {
                        Intent i = new Intent(LoginActivity.this, AutistaActivity.class);
                        startActivity(i);
                        this.finish();
                    }
                    case Constants.IMPIEGATO: {
                        Intent i = new Intent(LoginActivity.this, ImpiegatoActivity.class);
                        startActivity(i);
                        this.finish();

                    }
                    case Constants.CAPOCANTIERE: {
                        Intent i = new Intent(LoginActivity.this, CapoCantiereActivity.class);
                        startActivity(i);
                        this.finish();
                    }
                    case Constants.OPERAIO: {
                        Intent i = new Intent(LoginActivity.this, OperaioActivity.class);
                        startActivity(i);
                        this.finish();
                    }
                    default:
                        return;
                }


            } else {
                Toast.makeText(getApplicationContext(), "PASSWORD ERRATA", Toast.LENGTH_SHORT);
            }
        }

    }

    private boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) return true;
        return false;
    }


}
