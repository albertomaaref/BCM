package com.example.alim.bcm.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alim.bcm.R;
import com.example.alim.bcm.data.model.Richiesta;

import java.util.List;

public class RichiestaAutistaAdapter extends RecyclerView.Adapter<RichiestaAutistaAdapter.RichiestaHolder> {

    private List<Richiesta> listarichieste;
    private Context context;
    private OnClickCardListener onClickCardListener;

    public RichiestaAutistaAdapter() {
    }

    public interface OnClickCardListener {
        public void onclickCard(Richiesta richiesta);
    }

    public List<Richiesta> getListarichieste() {
        return listarichieste;
    }

    public void setListarichieste(List<Richiesta> listarichieste) {
        this.listarichieste = listarichieste;
    }

    public RichiestaAutistaAdapter(List<Richiesta> listarichieste, Context context, @NonNull OnClickCardListener onClickCardListener) {
        this.onClickCardListener = onClickCardListener;
        this.listarichieste = listarichieste;
        this.context = context;
    }

    @Override
    public RichiestaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.richiesta_card,parent,false);
        RichiestaHolder richiestaHolder = new RichiestaHolder(v);
        return richiestaHolder;
    }

    @Override
    public void onBindViewHolder(RichiestaHolder holder, int position) {
        final Richiesta richiesta = listarichieste.get(position);
        holder.tStatoRichiesta.setVisibility(View.GONE);
        holder.tDataConsegna.setVisibility(View.VISIBLE);
        holder.tDataConsegna.setText(richiesta.getDataConesgna());
        holder.tCantiere.setText(richiesta.getCantiere().toUpperCase());
        holder.cardRichiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCardListener.onclickCard(richiesta);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listarichieste.size();
    }

    public class RichiestaHolder extends RecyclerView.ViewHolder {

        CardView cardRichiesta;
        TextView tCantiere;
        TextView tDataConsegna;
        TextView tStatoRichiesta;

        public RichiestaHolder(View itemView) {
            super(itemView);
            cardRichiesta = itemView.findViewById(R.id.card_richiesta);
            tCantiere = itemView.findViewById(R.id.tCantiere);
            tDataConsegna = itemView.findViewById(R.id.tDataConsegna);
            tStatoRichiesta = itemView.findViewById(R.id.tStatoRichiesta);
        }
    }
}
