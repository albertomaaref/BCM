package com.example.alim.bcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.alim.bcm.fragments.CantiereFragment;
import com.example.alim.bcm.fragments.NotificheFragment;
import com.example.alim.bcm.fragments.CapoDemandFragment;
import com.example.alim.bcm.utilities.InternalStorage;

import static com.example.alim.bcm.model.Constants.TIPO_UTENTE_ATTIVO;
import static com.example.alim.bcm.model.Constants.UTENTE_ATTIVO;

public class CapoCantiereActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FragmentManager fm;
    private static String NOTIFICHE_FRAGMENT = "notificheFragment";
    private static String CANTIERE_FRAGMENT = "cantiereFragment";
    private static String RICHIESTE_FRAGMENT = "richiesteFragmente";

    private SharedPreferences preferences ;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_cantiere:
                    CantiereFragment cantiereFragment = (CantiereFragment) fm.findFragmentByTag(CANTIERE_FRAGMENT);
                    if (cantiereFragment != null )
                        ft.replace(R.id.fragmentCapo, cantiereFragment, CANTIERE_FRAGMENT).commit();
                    else {
                        cantiereFragment = new CantiereFragment();
                        ft.replace(R.id.fragmentCapo, cantiereFragment, CANTIERE_FRAGMENT).commit();
                    }
                    return true;
                case R.id.navigation_richieste:
                    CapoDemandFragment capoDemandFragment = (CapoDemandFragment) fm.findFragmentByTag(RICHIESTE_FRAGMENT);
                    if (capoDemandFragment != null)
                        ft.replace(R.id.fragmentCapo, capoDemandFragment, RICHIESTE_FRAGMENT).commit();
                    else {
                        capoDemandFragment = new CapoDemandFragment();
                        ft.replace(R.id.fragmentCapo, capoDemandFragment, RICHIESTE_FRAGMENT).commit();
                    }
                    return true;

                case R.id.navigation_notifiche:
                    NotificheFragment notificheFragment = (NotificheFragment) fm.findFragmentByTag(NOTIFICHE_FRAGMENT);
                    if (notificheFragment != null )
                        ft.replace(R.id.fragmentCapo, notificheFragment, NOTIFICHE_FRAGMENT).commit();
                    else {

                        notificheFragment = new NotificheFragment();
                        ft.replace(R.id.fragmentCapo, notificheFragment, NOTIFICHE_FRAGMENT).commit();
                    }
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capo_cantiere);


        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_logout);

            getSupportActionBar().setTitle(null);
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm = getSupportFragmentManager();
        navigation.setSelectedItemId(R.id.navigation_cantiere);
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
        return true;
    }
}
