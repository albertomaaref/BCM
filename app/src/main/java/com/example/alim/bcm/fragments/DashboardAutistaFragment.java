package com.example.alim.bcm.fragments;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alim.bcm.AutistaDetailRichiestaActivity;
import com.example.alim.bcm.R;
import com.example.alim.bcm.adapters.RichiestaAutistaAdapter;
import com.example.alim.bcm.data.model.Autista;
import com.example.alim.bcm.data.model.Richiesta;
import com.example.alim.bcm.utilities.InternalStorage;
import com.example.alim.bcm.utilities.JsonParser;
import com.example.alim.bcm.utilities.RequestManager;
import com.example.alim.bcm.utilities.SwipeController;
import com.example.alim.bcm.utilities.SwipeControllerActions;
import com.example.alim.bcm.utilities.TaskCompletion;

import java.util.List;

import static com.example.alim.bcm.data.model.Constants.AUTISTA;
import static com.example.alim.bcm.data.model.Constants.SUCCESSO;
import static com.example.alim.bcm.data.model.Constants.TIPO_UTENTE_ATTIVO;


public class DashboardAutistaFragment extends Fragment {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Richiesta> listaRichieste;
    private RichiestaAutistaAdapter richiestaAutistaAdapter;
    private SharedPreferences preferences;
    private SwipeRefreshLayout refreshLayout;
    private Autista autista;



    public DashboardAutistaFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaRichieste = (List<Richiesta>) getArguments().getSerializable("LISTA_RICHIESTE");
        autista = (Autista) InternalStorage.readObject(getContext(),AUTISTA);
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
        if (listaRichieste != null && listaRichieste.size()>0)        setRecycler(RequestManager.getIstance().getRichiesteByAutista(autista.getNome(),listaRichieste));

        else {}// nessuna richiesta


        refreshLayout = view.findViewById(R.id.refreshAutista);


        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // swipe
        final SwipeController swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }

            @Override
            public void onRightClicked(int position) {
                richiestaAutistaAdapter.getListarichieste().remove(position);
                richiestaAutistaAdapter.notifyItemRemoved(position);
                richiestaAutistaAdapter.notifyItemRangeChanged(position, richiestaAutistaAdapter.getItemCount());
            }
        },preferences.getString(TIPO_UTENTE_ATTIVO,""));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestManager.getIstance().downloadRequests(new TaskCompletion() {
                    @Override
                    public void taskToDo(String esito, String bodyResponse) {
                        if (esito.equalsIgnoreCase(SUCCESSO))
                        listaRichieste = JsonParser.getRichieste(bodyResponse);
                        refreshLayout.setRefreshing(false);
                        if (listaRichieste!= null && listaRichieste.size()>0)
                            setRecycler(RequestManager.getIstance().getRichiesteByAutista(autista.getNome(),listaRichieste));
                    }

                    @Override
                    public void taskToDo(String esito, String bodyResponse, String param1) {

                    }
                });


            }
        });

    }

    private void setRecycler(List<Richiesta> listaRichieste) {
        richiestaAutistaAdapter = new RichiestaAutistaAdapter(listaRichieste, getContext(), new RichiestaAutistaAdapter.OnClickCardListener() {
            @Override
            public void onclickCard(Richiesta richiesta) {
                Intent intent = new Intent(getContext(), AutistaDetailRichiestaActivity.class);
                intent.putExtra("richiesta", richiesta);
                startActivity(intent);            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(richiestaAutistaAdapter);
    }


}
