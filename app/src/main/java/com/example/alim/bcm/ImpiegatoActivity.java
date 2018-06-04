package com.example.alim.bcm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alim.bcm.fragments.AddProductFragment;
import com.example.alim.bcm.fragments.AddSiteFragment;
import com.example.alim.bcm.fragments.AttrezzziFragment;
import com.example.alim.bcm.fragments.MaterialiFragment;
import com.example.alim.bcm.fragments.RichiesteFragment;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Cantiere;
import com.example.alim.bcm.model.CapoCantiere;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.utilities.ManagerSiteAndPersonal;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.TaskCompletion;

import java.util.ArrayList;
import java.util.List;

import static com.example.alim.bcm.model.Constants.ADD_PRODUCT_FRAGMENT;
import static com.example.alim.bcm.model.Constants.ADD_SITE_FRAGMENT;
import static com.example.alim.bcm.model.Constants.ATTREZZI;
import static com.example.alim.bcm.model.Constants.ATTREZZI_FRAGMENT;
import static com.example.alim.bcm.model.Constants.CANTIERI;
import static com.example.alim.bcm.model.Constants.CAPOCANTIERE;
import static com.example.alim.bcm.model.Constants.LISTA_AUTISTI;
import static com.example.alim.bcm.model.Constants.MATERIALI;
import static com.example.alim.bcm.model.Constants.MATERIALI_FRAGMENT;
import static com.example.alim.bcm.model.Constants.RICHIESTE_FRAGMENT;
import static com.example.alim.bcm.model.Constants.SUCCESSO;
import static com.example.alim.bcm.model.Constants.TIPO_UTENTE_ATTIVO;
import static com.example.alim.bcm.model.Constants.UTENTE_ATTIVO;

public class ImpiegatoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    SharedPreferences preferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impiegato);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //taskCompletion = this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // attach the fragment
        if (savedInstanceState == null) {
            Fragment richiesteFragment = new RichiesteFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragmentImpiegato, richiesteFragment, RICHIESTE_FRAGMENT).commit();

        }

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        init();
    }

    private void init() {


        // carico autisti
        ManagerSiteAndPersonal managerSiteAndPersonal = ManagerSiteAndPersonal.getInstance();
        managerSiteAndPersonal.getAutistiInternal(new TaskCompletion() {
            @Override
            public void taskToDo(String esito, String bodyResponse) {
                if (esito.equalsIgnoreCase(Constants.SUCCESSO)) {
                    List<Autista> listaAutisti = new ArrayList<>();
                    listaAutisti = JsonParser.getAutisti(bodyResponse);
                    Log.i(Constants.TAG, this.getClass() + "   caricati atuisti");
                    InternalStorage.writeObject(getApplicationContext(), LISTA_AUTISTI, listaAutisti);

                } else {
                    Log.i(Constants.TAG, this.getClass() + "   errore caricamento autisti, errorCode " + bodyResponse);
                }
            }


            @Override
            public void taskToDo(String esito, String bodyResponse, String param1) {

            }
        });


        // carico cantieri
        managerSiteAndPersonal.getCantieri(new TaskCompletion() {
            @Override
            public void taskToDo(String esito, String bodyResponse) {
                if (esito.equalsIgnoreCase(Constants.SUCCESSO)) {
                    List<Cantiere> listaCantieri = new ArrayList<>();
                    listaCantieri = JsonParser.getCantieri(bodyResponse);
                    Log.i(Constants.TAG, this.getClass() + "   caricati cantieri");
                    InternalStorage.writeObject(getApplicationContext(), CANTIERI, listaCantieri);
                } else {
                    Log.i(Constants.TAG, this.getClass() + "   errore caricamento cantieri, errorCode " + bodyResponse);

                }
            }

            @Override
            public void taskToDo(String esito, String bodyResponse, String param1) {

            }
        });


        // carico i bosses
        managerSiteAndPersonal.getBosses(new TaskCompletion() {
            @Override
            public void taskToDo(String esito, String bodyResponse) {
                if (esito.equalsIgnoreCase(SUCCESSO)){
                    List<CapoCantiere> listaBosses = new ArrayList<>();
                    listaBosses = JsonParser.getBosses(bodyResponse);
                    Log.i(Constants.TAG, this.getClass() + "   caricati bosses");
                    InternalStorage.writeObject(getApplicationContext(),CAPOCANTIERE,listaBosses);
                }
                else  Log.i(Constants.TAG, this.getClass() + "   errore caricamento Bosses, errorCode " + bodyResponse);

            }

            @Override
            public void taskToDo(String esito, String bodyResponse, String param1) {

            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.impiegato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_AddSite) {
            AddSiteFragment addSiteFragment = new AddSiteFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentImpiegato, addSiteFragment, ADD_SITE_FRAGMENT).commit();
        }

        if (id == R.id.action_AddProduct) {
            AddProductFragment addProductFragment = new AddProductFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentImpiegato,addProductFragment,ADD_PRODUCT_FRAGMENT).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragMgr = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragMgr.beginTransaction();
        AttrezzziFragment myAttreziFragment = (AttrezzziFragment) getSupportFragmentManager().findFragmentByTag(ATTREZZI_FRAGMENT);
        MaterialiFragment myMaterialiFragment = (MaterialiFragment) getSupportFragmentManager().findFragmentByTag(MATERIALI_FRAGMENT);
        if (id == R.id.nav_materiale) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            MaterialiFragment materialiFragment = new MaterialiFragment();
            fragTrans.add(R.id.fragmentImpiegato, materialiFragment, MATERIALI_FRAGMENT);
            fragTrans.commit();


            /*if (myAttreziFragment != null && myAttreziFragment.isVisible()) {
                MaterialiFragment materialiFragment = new MaterialiFragment();
                fragTrans.replace(R.id.fragmentImpiegato, materialiFragment, MATERIALI_FRAGMENT);
                fragTrans.commit();
            } else if (myMaterialiFragment != null && myMaterialiFragment.isVisible()) {
                // sono già nel fragment
            } else {
                MaterialiFragment materialiFragment = new MaterialiFragment();
                fragTrans.add(R.id.fragmentImpiegato, materialiFragment, MATERIALI_FRAGMENT);
                fragTrans.commit();
            }*/

        } else if (id == R.id.nav_attrezzi) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            AttrezzziFragment attreziFragment = new AttrezzziFragment();
            fragTrans.add(R.id.fragmentImpiegato, attreziFragment, ATTREZZI_FRAGMENT);
            fragTrans.commit();


            /*if (myMaterialiFragment != null && myMaterialiFragment.isVisible()) {
                AttrezzziFragment impiegatoAttrezziFragmentTaskImpl = new AttrezzziFragment();
                fragTrans.replace(R.id.fragmentImpiegato, impiegatoAttrezziFragmentTaskImpl, ATTREZZI_FRAGMENT);
                fragTrans.commit();
            } else if (myAttreziFragment != null && myAttreziFragment.isVisible()) {
                // sono già nel fragment
            } else {
                fragTrans.add(R.id.fragmentImpiegato, myAttreziFragment, ATTREZZI_FRAGMENT);
                fragTrans.commit();
            }*/

        } else if (id == R.id.nav_richieste) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
            RichiesteFragment richiesteFragment = new RichiesteFragment();
            fragTrans.add(R.id.fragmentImpiegato, richiesteFragment, RICHIESTE_FRAGMENT);
            fragTrans.commit();

        }

        else if (id == R.id.nav_logout){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(UTENTE_ATTIVO,"");
            editor.putString(TIPO_UTENTE_ATTIVO,"");
            editor.commit();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*public void loadInitialData(){
        FireBaseConnection.get(Constants.UTENTI+"/"+Constants.AUTISTA + ".json", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                taskCompletion.taskToDo(Constants.SUCCESSO,s);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                taskCompletion.taskToDo(Constants.ERROR,""+statusCode);
            }
        });
    }


    @Override
    public void taskToDo(String esito, String bodyResponse) {
        if (esito.equalsIgnoreCase(Constants.SUCCESSO)){
            List<Autista> listaAutisti = new ArrayList<>();
            listaAutisti = JsonParser.getAutisti(bodyResponse);
            Log.i(Constants.TAG, this.getClass()+"   caricati atuisti");
            InternalStorage.writeObject(getApplicationContext(),Constants.LISTA_AUTISTI, listaAutisti);

        }
        else {
            Log.i(Constants.TAG,this.getClass()+"   errore caricamento autisti");
        }

    }

    @Override
    public void taskToDo(String esito, String bodyResponse, String param1) {

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        RichiesteFragment fragment = new RichiesteFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentImpiegato, fragment).commit();
        progressDialog.dismiss();
        progressDialog.cancel();
    }
}
