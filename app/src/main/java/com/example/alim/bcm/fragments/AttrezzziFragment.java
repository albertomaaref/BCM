package com.example.alim.bcm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.AttrezzoAdapter;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Cantiere;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.model.Personale;
import com.example.alim.bcm.utilities.ImpiegatoTasks;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AttrezzziFragment extends Fragment implements ImpiegatoTasks {


    Button bAggiungiCestino;
    RecyclerView recyclerViewAttrezzi;
    LinearLayoutManager lm;
    ListView listViewCestinoA;
    final List<Attrezzo> listaAtrezzi = new ArrayList<>();



    public AttrezzziFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        downloadAttrezzi();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lm = new LinearLayoutManager(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_attrezzi, container, false);

        recyclerViewAttrezzi = (RecyclerView) view.findViewById(R.id.ListViewAttrezzi);
        bAggiungiCestino = (Button) view.findViewById(R.id.bAggiungiAttrezzo);
        listViewCestinoA = (ListView) view.findViewById(R.id.listViewCestinoA);

        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void comunicateObjectsToDriver(List<Object> objects, Autista autista) {

    }

    private void downloadAttrezzi (){
        boolean result = false;

        for (int i=0 ; i<10; i++){
            Attrezzo attrezzo = new Attrezzo("hitaki","bomber","A"+i);
            listaAtrezzi.add(attrezzo);
        }
        AttrezzoAdapter attrezzoAdapter =new AttrezzoAdapter(getContext(),listaAtrezzi);
        recyclerViewAttrezzi.setLayoutManager(lm);
        recyclerViewAttrezzi.setAdapter(attrezzoAdapter);
    }
}
