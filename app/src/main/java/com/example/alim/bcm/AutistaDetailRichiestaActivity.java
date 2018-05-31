package com.example.alim.bcm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.alim.bcm.adapters.ArticoloAdapter;
import com.example.alim.bcm.model.Articolo;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.utilities.SwipeController;
import com.example.alim.bcm.utilities.SwipeControllerActions;

import static com.example.alim.bcm.model.Constants.TIPO_UTENTE_ATTIVO;

public class AutistaDetailRichiestaActivity extends AppCompatActivity {

    LinearLayoutManager lm ;
    RecyclerView recyclerView;
    Richiesta richiesta;
    ArticoloAdapter articoloAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autista_detail_richiesta);

        Intent intent = getIntent();
        richiesta = (Richiesta) intent.getSerializableExtra("richiesta");

        lm = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerArticolo);


        if (richiesta.getListaAttrezzi().size() > 0) {
            articoloAdapter = new ArticoloAdapter(this, richiesta.getListaAttrezzi());
        } else  {
            articoloAdapter = new ArticoloAdapter(this, richiesta.getListaMateriali());
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(articoloAdapter);


        // gestione swipe
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {

            }

            @Override
            public void onRightClicked(int position) {
                articoloAdapter.getListaArticoli().remove(position);
                articoloAdapter.notifyItemRemoved(position);
                articoloAdapter.notifyItemRangeChanged(position,articoloAdapter.getItemCount());
            }
        },sharedPreferences.getString(TIPO_UTENTE_ATTIVO,""));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

    }
}
