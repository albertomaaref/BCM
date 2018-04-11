package com.example.alim.bcm.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Articolo;

import java.util.List;

import static com.example.alim.bcm.R.color.green;

public class ArticoloAdapter extends RecyclerView.Adapter<ArticoloAdapter.Articoloholder> {

    private Context context;
    private List<? extends Articolo> listaArticoli;

    public ArticoloAdapter(Context context, List<? extends Articolo> listaArticoli) {
        this.context = context;
        this.listaArticoli = listaArticoli;
    }

    @Override
    public Articoloholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.articolo_card,parent,false);
        Articoloholder articoloholder = new Articoloholder(v);
        return articoloholder;
    }

    @Override
    public void onBindViewHolder(Articoloholder holder, int position) {
        Articolo articolo = listaArticoli.get(position);
        holder.tArticolo.setText(articolo.getNome());
        holder.tQuantita.setText(""+articolo.getQuantita());
        if (articolo.isConseganto()) {
            holder.tConsegnato.setText("CONSEGNATO");
            holder.tConsegnato.setTextColor(context.getResources().getColor(R.color.green));
        }
        else {
            holder.tConsegnato.setText("NON CONSEGNATO");
            holder.tConsegnato.setTextColor(context.getResources().getColor(R.color.red));
        }


    }

    @Override
    public int getItemCount() {
        return listaArticoli.size();
    }

    public class Articoloholder extends RecyclerView.ViewHolder {

        TextView tArticolo;
        TextView tQuantita;
        TextView tConsegnato;

        public Articoloholder(View itemView) {
            super(itemView);

            tArticolo = itemView.findViewById(R.id.tArticolo);
            tQuantita = itemView.findViewById(R.id.tQuantita);
            tConsegnato = itemView.findViewById(R.id.tConsegnato);
        }
    }
}
