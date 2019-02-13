package com.example.alim.bcm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.CapoCantiere;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Impiegato;
import com.example.alim.bcm.model.Operaio;
import com.example.alim.bcm.model.Personale;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.concurrent.CompletionService;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.example.alim.bcm.model.Constants.AUTISTA;
import static com.example.alim.bcm.model.Constants.CAPOCANTIERE;
import static com.example.alim.bcm.model.Constants.IMPIEGATO;
import static com.example.alim.bcm.model.Constants.OPERAIO;
import static com.example.alim.bcm.model.Constants.UTENTE_ATTIVO;

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
    private Personale personle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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

        setRadioButtonColor();
    }

    private void tryLogin(EditText username, EditText password) {

        if (isEditTextEmpty(username) || isEditTextEmpty(password) || rQualifica.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), "CONTROLLARE I CAMPI", Toast.LENGTH_SHORT).show();
        } else {
            int id = rQualifica.getCheckedRadioButtonId();
            String type = "";
            if (id == rAutista.getId()) {
                type = AUTISTA;
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
                if (s.equals("null")) {
                    delegation.taskToDo(Constants.ERROR, "connessione fallita", qualifica);

                } else {
                    if (qualifica.equalsIgnoreCase(AUTISTA)) {
                        personle = (Autista) JsonParser.getPersonale(s, qualifica);
                        InternalStorage.writeObject(getApplicationContext(), AUTISTA, personle);
                    } else if (qualifica.equalsIgnoreCase(IMPIEGATO)) {
                        personle = (Impiegato) JsonParser.getPersonale(s, qualifica);
                        InternalStorage.writeObject(getApplicationContext(), IMPIEGATO, personle);
                    } else if (qualifica.equalsIgnoreCase(CAPOCANTIERE)) {
                        personle = (CapoCantiere) JsonParser.getPersonale(s, qualifica);
                        InternalStorage.writeObject(getApplicationContext(), CAPOCANTIERE, personle);
                    } else if (qualifica.equalsIgnoreCase(OPERAIO)) {
                        personle = (Operaio) JsonParser.getPersonale(s, qualifica);
                        InternalStorage.writeObject(getApplicationContext(), OPERAIO, personle);
                    } else personle = null;
                    if (personle != null) {

                        String psw = JsonParser.getPassword(s);
                        delegation.taskToDo(Constants.SUCCESSO, psw, qualifica);
                    }
                }
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
    }

    @Override
    public void taskToDo(String statusCode, String psw) {

    }

    @Override
    public void taskToDo(String esito, String bodyResponse, String qualifica) {
        progressDialog.dismiss();
        progressDialog.cancel();
        if (esito.equals(Constants.ERROR)) {
            Toast.makeText(getApplicationContext(), "USER/PASSWORD ERRATA", Toast.LENGTH_SHORT).show();

        } else if (esito.equals(Constants.SUCCESSO)) {
            if (bodyResponse.equals(password.getText().toString())) {

                // salvare tipo_utenza attiva
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Constants.TIPO_UTENTE_ATTIVO, qualifica);
                editor.commit();
                InternalStorage.writeObject(getApplicationContext(), UTENTE_ATTIVO, personle);


                if (qualifica.equals(Constants.OPERAIO)) {
                    Log.i(Constants.TAG, "" + this.getClass() + " go to activity for" + qualifica);
                    Intent i = new Intent(LoginActivity.this, OperaioActivity.class);
                    startActivity(i);
                    this.finish();
                } else if (qualifica.equals(AUTISTA)) {
                    Log.i(Constants.TAG, "" + this.getClass() + " go to activity for" + qualifica);

                    Intent i = new Intent(LoginActivity.this, AutistaActivity.class);
                    i.putExtra(AUTISTA, username.getText().toString());
                    startActivity(i);
                    this.finish();
                } else if (qualifica.equals(Constants.IMPIEGATO)) {
                    Log.i(Constants.TAG, "" + this.getClass() + " go to activity for" + qualifica);

                    Intent i = new Intent(LoginActivity.this, ImpiegatoActivity.class);
                    startActivity(i);
                    this.finish();

                } else if (qualifica.equals(Constants.CAPOCANTIERE)) {
                    Log.i(Constants.TAG, "" + this.getClass() + " go to activity for" + qualifica);

                    Intent i = new Intent(LoginActivity.this, CapoCantiereActivity.class);
                    startActivity(i);
                    this.finish();
                } else {

                    return;
                }
            } else {
                Toast.makeText(getApplicationContext(), "USER/PASSWORD ERRATA", Toast.LENGTH_SHORT).show();
                return;
            }


        }


    }

    private boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) return true;
        return false;
    }

    @SuppressLint("RestrictedApi")
    private void setRadioButtonColor(){
        if(Build.VERSION.SDK_INT<21){
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_checked},
                            new int[]{android.R.attr.state_checked}
                    },
                    new int[]{

                            Color.rgb(255,255,255)
                            , Color.rgb (255,255,255),
                    }
            );

            AppCompatRadioButton rb = (AppCompatRadioButton) rAutista;
            rb.setSupportButtonTintList(colorStateList);
            rb = (AppCompatRadioButton) rImpiegato;
            rb.setSupportButtonTintList(colorStateList);
            rb = (AppCompatRadioButton) rCapocantiere;
            rb.setSupportButtonTintList(colorStateList);
            rb = (AppCompatRadioButton) rOperaio;
            rb.setSupportButtonTintList(colorStateList);
        }
    }
}
