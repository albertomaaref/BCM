package com.example.alim.bcm.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alim.bcm.GestioneRichiestaActivity;
import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.RichiestaImpiegatoAdapter;
import com.example.alim.bcm.data.model.Cantiere;
import com.example.alim.bcm.data.model.Constants;
import com.example.alim.bcm.data.model.Richiesta;
import com.example.alim.bcm.data.model.StatoRichiesta;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.ManagerSiteAndPersonal;
import com.example.alim.bcm.utilities.RequestManager;
import com.example.alim.bcm.utilities.SwipeController;
import com.example.alim.bcm.utilities.SwipeControllerActions;
import com.example.alim.bcm.utilities.TaskCompletion;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.alim.bcm.data.model.Constants.CANTIERI;
import static com.example.alim.bcm.data.model.Constants.TAG;
import static com.example.alim.bcm.data.model.Constants.TIPO_UTENTE_ATTIVO;


public class RichiesteFragment extends Fragment {

    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    Spinner sCantiere;
    SwipeRefreshLayout refreshLayout;
    TextView tNumRichieste;
    TextView tNumAttesa;
    TextView tNumConsegna;
    List<Richiesta> richiestaList = new ArrayList<>();
    RichiestaImpiegatoAdapter richiestaImpiegatoAdapter;
    SharedPreferences preferences;
    boolean firsTime = false;
    private List<Cantiere> cantiereList;


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
        View view = inflater.inflate(R.layout.fragment_richieste, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerRichieste);
        layoutManager = new LinearLayoutManager(getContext());
        refreshLayout = view.findViewById(R.id.refreshRichieste);
        progressDialog = new ProgressDialog(getContext());

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        tNumRichieste = view.findViewById(R.id.tNumRichieste);
        tNumAttesa = view.findViewById(R.id.tNumAttesa);
        tNumConsegna = view.findViewById(R.id.tNumConsegna);

        sCantiere = view.findViewById(R.id.sCantiere);

        setSpinnerCantieri();
        sCantiere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!firsTime){
                    filtroLista(sCantiere.getSelectedItem().toString());
                    firsTime=false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RequestManager requestManager = RequestManager.getIstance();
        requestManager.downloadRequests(new TaskCompletion() {
            @Override
            public void taskToDo(String esito, String bodyResponse) {
                if (esito.equals(Constants.SUCCESSO)) {

                    richiestaList = JsonParser.getRichieste(bodyResponse);

                    Log.i(Constants.TAG, "" + this.getClass());
                    if (richiestaList != null && richiestaList.size() > 0) {
                        richiestaImpiegatoAdapter = new RichiestaImpiegatoAdapter(getContext(), richiestaList, new RichiestaImpiegatoAdapter.OnClickCardListener() {
                            @Override
                            public void onclickCard(Richiesta richiesta) {
                                Intent intent = new Intent(getContext(), GestioneRichiestaActivity.class);
                                intent.putExtra("richiesta", richiesta);
                                startActivityForResult(intent, 200);
                            }
                        });
                        setRecycler(richiestaList);

                    } else {
                        Toast.makeText(getContext(), "Lista richieste vuota", Toast.LENGTH_LONG).show();
                    }

                } else if (esito.equals(Constants.ERROR)) {

                }

                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void taskToDo(String esito, String bodyResponse, String param1) {

            }
        });
        //RequestManager.getIstance().downloadRequests(getContext(),layoutManager,recyclerView);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
                RequestManager.getIstance().downloadRequests(new TaskCompletion() {
                    @Override
                    public void taskToDo(String esito, String bodyResponse) {
                        if (esito.equals(Constants.SUCCESSO)) {

                            richiestaList = JsonParser.getRichieste(bodyResponse);

                            Log.i(Constants.TAG, "" + this.getClass());
                            if (richiestaList != null && richiestaList.size() > 0) {
                                richiestaImpiegatoAdapter = new RichiestaImpiegatoAdapter(getContext(), richiestaList, new RichiestaImpiegatoAdapter.OnClickCardListener() {
                                    @Override
                                    public void onclickCard(Richiesta richiesta) {
                                        Intent intent = new Intent(getContext(), GestioneRichiestaActivity.class);
                                        intent.putExtra("richiesta", richiesta);
                                        startActivityForResult(intent, 200);
                                    }
                                });
                                setRecycler(richiestaList);

                            } else {
                                Toast.makeText(getContext(), "Lista richieste Vuota", Toast.LENGTH_LONG).show();
                            }

                        } else if (esito.equals(Constants.ERROR)) {

                        }

                        progressDialog.dismiss();
                        progressDialog.cancel();
                        refreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void taskToDo(String esito, String bodyResponse, String param1) {
                        refreshLayout.setRefreshing(false);

                    }
                });
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void filtroLista(String cantiere) {
        if (richiestaList !=null && richiestaList.size()>0){

            List<Richiesta> listaFiltrata = new ArrayList<>();
            if (!cantiere.equalsIgnoreCase("Tutte")) {


                for (Richiesta richiesta : richiestaList
                        ) {
                    if (richiesta.getCantiere().equalsIgnoreCase(cantiere)) {
                        listaFiltrata.add(richiesta);
                    }
                }
            } else listaFiltrata = richiestaList;


            if (listaFiltrata.size() > 0) {

                richiestaImpiegatoAdapter = new RichiestaImpiegatoAdapter(getContext(), listaFiltrata, new RichiestaImpiegatoAdapter.OnClickCardListener() {
                    @Override
                    public void onclickCard(Richiesta richiesta) {
                        Intent intent = new Intent(getContext(), GestioneRichiestaActivity.class);
                        intent.putExtra("richiesta", richiesta);
                        startActivityForResult(intent, 200);
                    }
                });

                setRecycler(listaFiltrata);

                recyclerView.setAdapter(richiestaImpiegatoAdapter);
            }
        }


    }

    public void setRecycler(List<Richiesta> list) {
        int inAttesa = 0;
        int inConesgna = 0;
        for (Richiesta richiesta : list
                ) {
            if (richiesta.getStato().equals(StatoRichiesta.IN_ATTESA)) inAttesa++;
            else if (richiesta.getStato().equals(StatoRichiesta.IN_CONSEGNA)) inConesgna++;
        }
        tNumAttesa.setText("" + inAttesa);
        tNumConsegna.setText("" + inConesgna);
        tNumRichieste.setText("" + list.size());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(richiestaImpiegatoAdapter);

        //gestione swipe
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }

            @Override
            public void onRightClicked(int position) {
                super.onRightClicked(position);
            }
        }, preferences.getString(TIPO_UTENTE_ATTIVO, ""));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


    }

    public void setSpinnerCantieri() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

        cantiereList = (List<Cantiere>) InternalStorage.readObject(getContext(), CANTIERI);
        if (cantiereList != null && cantiereList.size() > 0) {

            List<String> lista = new ArrayList<>();
            lista.add("Seleziona Cantiere");
            for (Cantiere cantiere : cantiereList
                    ) {
                lista.add(cantiere.getIndirizzo());
            }
            if (cantiereList == null) {
                Log.i(TAG, this.getClass() + " errore settaggio spinner cantieri");
            } else {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_white_item, R.id.spinnerRegistration, lista);
                sCantiere.setAdapter(arrayAdapter);
            }

            progressDialog.dismiss();
            progressDialog.cancel();
        } else ManagerSiteAndPersonal.getInstance().getCantieri(new TaskCompletion() {
            @Override
            public void taskToDo(String esito, String bodyResponse) {
                String s = new String(bodyResponse);
                cantiereList = JsonParser.getCantieri(s);
                if (cantiereList != null && cantiereList.size() > 0) {
                    List<String> lista = new ArrayList<>();
                    lista.add("Seleziona Cantiere");
                    for (Cantiere cantiere : cantiereList
                            ) {
                        lista.add(cantiere.getIndirizzo());
                    }
                    if (cantiereList == null) {
                        Log.i(TAG, this.getClass() + " errore settaggio spinner cantieri");
                    } else {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_white_item, R.id.spinnerRegistration, lista);
                        sCantiere.setAdapter(arrayAdapter);
                    }
                } else {
                    List<String> lista = new ArrayList<>();
                    lista.add("Non ci sono cantieri");


                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_white_item, R.id.spinnerRegistration, lista);
                    sCantiere.setAdapter(arrayAdapter);

                }

                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void taskToDo(String esito, String bodyResponse, String param1) {

            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Constants.TAG, "onActivity result fragment");

        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                boolean aggiornato = data.getBooleanExtra("aggiornato", false);
                if (aggiornato) {
                    // ricarico fragment x aver aggiornato l'autista
                    RichiesteFragment fragment = new RichiesteFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentImpiegato, fragment).commit();
                }
            }
        }
    }
}
