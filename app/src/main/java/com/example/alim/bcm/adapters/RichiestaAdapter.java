package com.example.alim.bcm.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alim.bcm.GestioneRichiestaActivity;
import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Richiesta;

import java.util.List;

public class RichiestaAdapter extends RecyclerView.Adapter<RichiestaAdapter.RichiestaHolder> {

    private Context context;
    private List<Richiesta> listaRichieste;
    private OnClickCardListener onClickCardListener;

    public RichiestaAdapter(Context context, List<Richiesta> listaRichieste, @NonNull OnClickCardListener onClickCardListener) {
        this.context = context;
        this.listaRichieste = listaRichieste;
        this.onClickCardListener = onClickCardListener;
    }

    public interface OnClickCardListener {
        public void onclickCard(Richiesta richiesta);
    }

    @Override
    public RichiestaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.richiesta_card,parent,false);
        RichiestaHolder richiestaHolder = new RichiestaHolder(v);
        return richiestaHolder;
    }

    @Override
    public void onBindViewHolder(RichiestaHolder holder, int position) {
            final Richiesta richiesta = listaRichieste.get(position);
            holder.tCantiere.setText(richiesta.getCantiere());
            holder.tStatoRichiesta.setText(richiesta.getStato().toString());
            holder.cardRichiesta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onClickCardListener.onclickCard(richiesta);


                }
            });

    }

    @Override
    public int getItemCount() {
        return listaRichieste.size();
    }

    public class RichiestaHolder extends RecyclerView.ViewHolder {

        CardView cardRichiesta;
        TextView tCantiere;
        TextView tStatoRichiesta;

        public RichiestaHolder(View itemView) {
            super(itemView);

            cardRichiesta = itemView.findViewById(R.id.card_richiesta);
            tCantiere = itemView.findViewById(R.id.tCantiere);
            tStatoRichiesta = itemView.findViewById(R.id.tStatoRichiesta);
        }
    }
}
