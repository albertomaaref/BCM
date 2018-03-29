package com.example.alim.bcm.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import com.example.alim.bcm.adapters.AttrezzoAdapter;
import com.example.alim.bcm.model.Articolo;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.utilities.DownloadItems;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class RichiesteFragment extends Fragment  {

    private Button bApprovaRichiesta;
    private RecyclerView recyclerViewAttrezzi;
    private LinearLayoutManager lm;
    private List<Attrezzo> listaCestino = new ArrayList<>();
    private DatabaseReference ref;
    private FirebaseDatabase database;

    public RichiesteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        lm = new LinearLayoutManager(getContext());
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
        View view = inflater.inflate(R.layout.fragment_richieste, container, false);
        recyclerViewAttrezzi = view.findViewById(R.id.recyclerViewAttrezzi);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);

        bApprovaRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(listaCestino);
            }
        });

        return view;
    }

    private void sendRequest(List<Attrezzo> listaCestino) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Procedere con la richiesta ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadItems downloadItems = DownloadItems.getDownloadItems();
                downloadItems.removeAll();

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DownloadItems downloadItems = DownloadItems.getDownloadItems();
        downloadItems.scaricaListFromDB(getContext(),listaCestino,recyclerViewAttrezzi,lm,Constants.ATTREZZI);
    }






    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
