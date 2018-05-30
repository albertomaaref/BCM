package com.example.alim.bcm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.RichiestaAutistaAdapter;
import com.example.alim.bcm.model.Richiesta;

import java.util.List;


public class DashboardAutistaFragment extends Fragment {


    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Richiesta> listaRichieste;

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
        RichiestaAutistaAdapter richiestaAutistaAdapter = new RichiestaAutistaAdapter(listaRichieste, getContext(), new RichiestaAutistaAdapter.OnClickCardListener() {
            @Override
            public void onclickCard(Richiesta richiesta) {
                Toast.makeText(getContext(),"cliccato elemento",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(richiestaAutistaAdapter);

    }


}
