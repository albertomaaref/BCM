package com.example.alim.bcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import butterknife.BindView;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.rUsername)
    EditText username;

    @BindView(R.id.rPassword)
    EditText password;

    @BindView(R.id.rConferma)
    EditText conferma;

    @BindView(R.id.tipoUtenza)
    Spinner tipoutenza;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }
}
