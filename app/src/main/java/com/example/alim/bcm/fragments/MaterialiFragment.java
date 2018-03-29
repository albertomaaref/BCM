package com.example.alim.bcm.fragments;

import android.app.ProgressDialog;
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
import com.example.alim.bcm.adapters.MaterialeAdapter;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.utilities.DownloadItems;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the


 */
public class MaterialiFragment extends Fragment {

    private LinearLayoutManager lm;
    private RecyclerView recyclerViewMateriale;

    private List<Materiale> listaCestino = new ArrayList<>();


    public MaterialiFragment() {
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
        return inflater.inflate(R.layout.fragment_materiali, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lm = new LinearLayoutManager(getContext());
        recyclerViewMateriale = view.findViewById(R.id.recyclerMateriali);
        DownloadItems downloadItems = DownloadItems.getDownloadItems();
        downloadItems.scaricaListFromDB(getContext(),listaCestino,recyclerViewMateriale,lm,Constants.MATERIALI);


    }





    @Override
    public void onDetach() {
        super.onDetach();

    }



}
