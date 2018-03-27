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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.AttrezzoAdapter;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.ImpiegatoTasks;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AttrezzziFragment extends Fragment implements ImpiegatoTasks, TaskCompletion {


    Button bApprovaRichiesta;
    Button bAggiungiCestino;
    RecyclerView recyclerViewAttrezzi;
    LinearLayoutManager lm;
    ListView listViewCestinoA;
    List<Attrezzo> listaAtrezzi = new ArrayList<>();
    private List<Attrezzo> listaCestino = new ArrayList<>();
    private TaskCompletion delegato;
    private ProgressDialog progressDialog;
    private DatabaseReference ref;
    private FirebaseDatabase database;




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
        delegato = this;
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_attrezzi, container, false);

        recyclerViewAttrezzi = (RecyclerView) view.findViewById(R.id.ListViewAttrezzi);
        bAggiungiCestino = (Button) view.findViewById(R.id.bAggiungiAttrezzo);
        listViewCestinoA = (ListView) view.findViewById(R.id.listViewCestinoA);
        bApprovaRichiesta = view.findViewById(R.id.bApprovaRichiesta);

        bApprovaRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(listaCestino);
            }
        });

        bAggiungiCestino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbAggiungiCestino(listaCestino);
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

        restCallAttrezzi(delegato);


    }

    private void restCallAttrezzi(final TaskCompletion delegato) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        FireBaseConnection.get(Constants.ATTREZZI, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                delegato.taskToDo(Constants.SUCCESSO,s);
                AttrezzoAdapter attrezzoAdapter =new AttrezzoAdapter(getContext(), listaAtrezzi, new AttrezzoAdapter.OnAttrezzoClickListener() {
                    @Override
                    public void onAttrezzoCheck(Attrezzo attrezzo) {
                        listaCestino.add(attrezzo);
                    }

                    @Override
                    public void onAttrezzoUncheck(Attrezzo attrezzo) {
                        listaCestino.remove(attrezzo);
                    }
                });
                recyclerViewAttrezzi.setLayoutManager(lm);
                recyclerViewAttrezzi.setAdapter(attrezzoAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                delegato.taskToDo(Constants.ERROR,new String(responseBody));
            }
        });
    }

    public void setbAggiungiCestino (List<Attrezzo> lista){
        List<String> listanome = new ArrayList<>();
        for (Attrezzo a : lista){
            listanome.add(a.getNome());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.cestino_item,R.id.tCestinoItem,listanome);
        listViewCestinoA.setAdapter(arrayAdapter);
    }

    @Override
    public void taskToDo(String esito, String bodyResponse) {
        progressDialog.dismiss();
        progressDialog.cancel();
        if (esito.equals(Constants.SUCCESSO)){
            listaAtrezzi = JsonParser.getAttrezzi(bodyResponse);
        }
        else if (esito.equals(Constants.ERROR)){
            Toast.makeText(getContext(),"ERROE NEL DOWNLOAD DEGLI ATTREZZI", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void taskToDo(String esito, String bodyResponse, String param1) {

    }
}
