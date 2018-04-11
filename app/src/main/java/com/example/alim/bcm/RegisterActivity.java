package com.example.alim.bcm;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.services.ConfermaDatiDialog;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;

public class RegisterActivity extends AppCompatActivity {

    EditText nome;
    EditText cognome;
    EditText password;
    EditText conferma;
    Spinner tipoutenza;
    Button bRegistrazione;

    DatabaseReference ref;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bRegistrazione = findViewById(R.id.bRegistrazione);
        tipoutenza = findViewById(R.id.tipoUtenza);
        conferma = findViewById(R.id.rConferma);
        cognome = findViewById(R.id.rCognome);
        nome = findViewById(R.id.rNome);
        password = findViewById(R.id.rPassword);

        bRegistrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserisciPersonale();
            }
        });




    }


    public boolean isCorrect (){
        Log.i(Constants.TAG,""+tipoutenza.getSelectedItem().toString());
        if (nome.getText().toString().length()<2){
            return false;
        }
        else if (cognome.getText().toString().length()<2)
            return false;
        else if (password.getText().toString().length()<1)
            return false;
        else if (!(password.getText().toString().equals(conferma.getText().toString())))
            return false;
        else if (tipoutenza.getSelectedItem().toString().equalsIgnoreCase("Selzionare tipologia"))
            return false;

        return true;
    }

    public void inserisciPersonale(){
        if (!isCorrect()){
            Toast.makeText(getApplicationContext(),"ERRORE INSERIMENTO DATI",Toast.LENGTH_SHORT).show();
        }
        else {
            database = FirebaseDatabase.getInstance();
            ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
            final String name = nome.getText().toString();
            final String surname = cognome.getText().toString();
            final String utenza = tipoutenza.getSelectedItem().toString();
            final String psw = password.getText().toString();
            final ConfermaDatiDialog confermaDatiDialog = new ConfermaDatiDialog(name, surname, utenza, new ConfermaDatiDialog.onButtonClickListener() {
                @Override
                public void onCheckClick() {
                    ref.child(Constants.UTENTI+"/"+utenza.toLowerCase()+"/"+name+"/nome").setValue(name);
                    ref.child(Constants.UTENTI+"/"+utenza.toLowerCase()+"/"+name+"/cognome").setValue(surname);
                    ref.child(Constants.UTENTI+"/"+utenza.toLowerCase()+"/"+name+"/password").setValue(psw);
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                    //Toast.makeText(getApplicationContext(),"checkClick",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCloseClick() {
                    //Toast.makeText(getApplicationContext(),"closeClick",Toast.LENGTH_SHORT).show();
                }
            });
            confermaDatiDialog.show(getSupportFragmentManager(),Constants.CONFERMA_DATI_DIALOG);



        }
    }
}
