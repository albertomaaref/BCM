package com.example.alim.bcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.utilities.TaskCompletion;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements TaskCompletion {

    @BindView(R.id.eUsername)
    EditText username;

    @BindView(R.id.ePassword)
    EditText password;

    @BindView(R.id.bLogin)
    Button login;

    @BindView(R.id.bRegistrati)
    FloatingActionButton registrati;

    private SharedPreferences preferences;
    private TaskCompletion delegation;


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

        if (isEditTextEmpty(username) && isEditTextEmpty(password)){
            Toast.makeText(getApplicationContext(),"CONTROLLARE I CAMPI",Toast.LENGTH_SHORT);
        }
        else {
            
        }


        Intent i = new Intent(LoginActivity.this, ImpiegatoActivity.class);
        startActivity(i);

    }

    private void goToregister() {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void taskToDo(String string) {

    }

    private boolean isEditTextEmpty (EditText editText){
        if (editText.getText().toString().equals("")) return true;
        return false;
    }


}
