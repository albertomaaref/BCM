package com.example.alim.bcm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alim.bcm.fragments.AttrezzziFragment;
import com.example.alim.bcm.fragments.MaterialiFragment;

public class ImpiegatoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String ATTREZZI_FRAGMENT = "attrezi_fragment";
    private static String MATERIALI_FRAGMENT = "materiali_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impiegato);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // attach the fragment
        if (savedInstanceState == null) {
            Fragment attrezziFragment = new AttrezzziFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragmentImpiegato, attrezziFragment, ATTREZZI_FRAGMENT).commit();

        }
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
        if (id == R.id.action_settings) {
            return true;
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
            if (myAttreziFragment != null && myAttreziFragment.isVisible()) {
                MaterialiFragment materialiFragment = new MaterialiFragment();
                fragTrans.replace(R.id.fragmentImpiegato, materialiFragment, MATERIALI_FRAGMENT);
                fragTrans.commit();
            } else if (myMaterialiFragment != null && myMaterialiFragment.isVisible()) {
                // sono già nel fragment
            } else {
                fragTrans.add(R.id.fragmentImpiegato, myMaterialiFragment, MATERIALI_FRAGMENT);
                fragTrans.commit();
            }

        } else if (id == R.id.nav_attrezzi) {
            if (myMaterialiFragment != null && myMaterialiFragment.isVisible()) {
                AttrezzziFragment impiegatoAttrezziFragmentTaskImpl = new AttrezzziFragment();
                fragTrans.replace(R.id.fragmentImpiegato, impiegatoAttrezziFragmentTaskImpl, ATTREZZI_FRAGMENT);
                fragTrans.commit();
            } else if (myAttreziFragment != null && myMaterialiFragment.isVisible()) {
                // sono già nel fragment
            } else {
                fragTrans.add(R.id.fragmentImpiegato, myMaterialiFragment, ATTREZZI_FRAGMENT);
                fragTrans.commit();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
