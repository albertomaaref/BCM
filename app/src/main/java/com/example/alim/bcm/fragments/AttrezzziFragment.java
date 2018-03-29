package com.example.alim.bcm.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.utilities.DownloadItems;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.ImpiegatoTasks;
import com.example.alim.bcm.utilities.RequestManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AttrezzziFragment extends Fragment implements ImpiegatoTasks {


    private Button bApprovaRichiesta;
    private RecyclerView recyclerViewAttrezzi;
    private LinearLayoutManager lm;
    private List<Attrezzo> listaCestino = new ArrayList<>();





    public AttrezzziFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final DownloadItems downloadItems = DownloadItems.getDownloadItems();
        downloadItems.scaricaListFromDB(getContext(),listaCestino,recyclerViewAttrezzi,lm, Constants.ATTREZZI);
        bApprovaRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestManager.sendRequest(getContext(),(List<Attrezzo>) downloadItems.getListaCestino(),Constants.ATTREZZI);
                AttrezzziFragment fr = (AttrezzziFragment) getFragmentManager().findFragmentById(R.id.fragmentImpiegato);
                getFragmentManager().beginTransaction().detach(fr).attach(fr).commit();
            }
        });

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
        lm = new LinearLayoutManager(getContext());
        recyclerViewAttrezzi = view.findViewById(R.id.recyclerViewAttrezzi);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);

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







}
