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
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the


 */
public class MaterialiFragment extends Fragment implements TaskCompletion{

    private LinearLayoutManager lm;
    private RecyclerView recyclerViewMateriale;
    private MaterialeAdapter materialeAdapter;
    private TaskCompletion delegato;
    private ProgressDialog progressDialog;
    private List<Materiale> listaMateriali;


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

        restCall(delegato);
    }

    private void restCall(final TaskCompletion delegato) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        FireBaseConnection.get(Constants.MATERIALI, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                delegato.taskToDo(Constants.SUCCESSO,s);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String s = new String(responseBody);
                delegato.taskToDo(Constants.ERROR,s);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void taskToDo(String esito, String bodyResponse) {
        progressDialog.dismiss();
        progressDialog.cancel();

        if (esito.equals(Constants.ERROR)){
            Toast.makeText(getContext(),"ERRORE NEL CARICAMENTO DEI MATERIALI", Toast.LENGTH_SHORT).show();
        }
        else {
            listaMateriali = JsonParser.getMateriali(bodyResponse);
            recyclerViewMateriale.setLayoutManager(lm);
            materialeAdapter = new MaterialeAdapter(listaMateriali,getContext());
            recyclerViewMateriale.setAdapter(materialeAdapter);
        }
    }

    @Override
    public void taskToDo(String esito, String bodyResponse, String param1) {

    }
}
