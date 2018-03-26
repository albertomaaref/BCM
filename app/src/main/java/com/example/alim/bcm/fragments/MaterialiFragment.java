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

import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.MaterialeAdapter;
import com.example.alim.bcm.utilities.TaskCompletion;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the


 */
public class MaterialiFragment extends Fragment implements TaskCompletion{

    private LinearLayoutManager lm;
    private RecyclerView recyclerViewMateriale;
    private MaterialeAdapter materialeAdapter;
    private TaskCompletion delegato;


    public MaterialiFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lm = new LinearLayoutManager(getContext());
        delegato = this;


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
        recyclerViewMateriale = view.findViewById(R.id.recyclerMateriali);
        dowloadMateriali();

    }

    private void dowloadMateriali() {

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void taskToDo(String esito, String bodyResponse) {

    }
}
