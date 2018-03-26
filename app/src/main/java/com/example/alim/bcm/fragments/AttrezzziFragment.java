package com.example.alim.bcm.fragments;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.AttrezzoAdapter;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Cantiere;
import com.example.alim.bcm.model.Costants;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.model.Personale;
import com.example.alim.bcm.utilities.FireBaseConnection;
import com.example.alim.bcm.utilities.ImpiegatoTasks;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.TaskCompletion;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class AttrezzziFragment extends Fragment implements ImpiegatoTasks, TaskCompletion {


    Button bMandaAutista;
    Button bAggiungiCestino;
    RecyclerView recyclerViewAttrezzi;
    LinearLayoutManager lm;
    ListView listViewCestinoA;
    List<Attrezzo> listaAtrezzi = new ArrayList<>();
    private List<Attrezzo> listaCestino = new ArrayList<>();
    private TaskCompletion delegato;
    private ProgressDialog progressDialog;



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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_attrezzi, container, false);

        recyclerViewAttrezzi = (RecyclerView) view.findViewById(R.id.ListViewAttrezzi);
        bAggiungiCestino = (Button) view.findViewById(R.id.bAggiungiAttrezzo);
        listViewCestinoA = (ListView) view.findViewById(R.id.listViewCestinoA);
        bMandaAutista = view.findViewById(R.id.bMandaAutista);

        bMandaAutista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // To Do : mandare notifica all'autista con la lista degli elementi con relativo cantiere
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
        FireBaseConnection.get("attrezzi.json", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                delegato.taskToDo(Costants.SUCCESSO,s);
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
                delegato.taskToDo(Costants.ERROR,new String(responseBody));
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
        if (esito.equals(Costants.SUCCESSO)){
            listaAtrezzi = JsonParser.getAttrezzi(bodyResponse);
        }
        else if (esito.equals(Costants.ERROR)){
            Toast.makeText(getContext(),"ERROE NEL DOWNLOAD DEGLI ATTREZZI", Toast.LENGTH_SHORT).show();
        }
    }
}
