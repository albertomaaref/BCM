package com.example.alim.bcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AutistaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autista);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
