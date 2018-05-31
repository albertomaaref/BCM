package com.example.alim.bcm.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alim.bcm.AutistaDetailRichiestaActivity;
import com.example.alim.bcm.GestioneRichiestaActivity;
import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.RichiestaAutistaAdapter;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.utilities.SwipeController;
import com.example.alim.bcm.utilities.SwipeControllerActions;

import java.util.List;

import static com.example.alim.bcm.model.Constants.TIPO_UTENTE_ATTIVO;


public class DashboardAutistaFragment extends Fragment {


    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Richiesta> listaRichieste;
    RichiestaAutistaAdapter richiestaAutistaAdapter;
    SharedPreferences preferences;
    public DashboardAutistaFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaRichieste = (List<Richiesta>) getArguments().getSerializable("LISTA_RICHIESTE_BY_AUTISTA");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_autista, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerAutista);
        linearLayoutManager = new LinearLayoutManager(getContext());
        richiestaAutistaAdapter = new RichiestaAutistaAdapter(listaRichieste, getContext(), new RichiestaAutistaAdapter.OnClickCardListener() {
            @Override
            public void onclickCard(Richiesta richiesta) {
                Intent intent = new Intent(getContext(), AutistaDetailRichiestaActivity.class);
                intent.putExtra("richiesta", richiesta);
                startActivity(intent);            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(richiestaAutistaAdapter);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // swipe
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }

            @Override
            public void onRightClicked(int position) {
                richiestaAutistaAdapter.getListarichieste().remove(position);
                richiestaAutistaAdapter.notifyItemRemoved(position);
                richiestaAutistaAdapter.notifyItemRangeChanged(position, richiestaAutistaAdapter.getItemCount());
            }
        },preferences.getString(TIPO_UTENTE_ATTIVO,""));
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
