package com.example.alim.bcm.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Materiale;

import java.util.List;

/**
 * Created by alim on 26-Mar-18.
 */

public class MaterialeAdapter extends RecyclerView.Adapter<MaterialeAdapter.Materialeholder> {
    private List<Materiale> listaMataeriali ;
    private Context context;

    public MaterialeAdapter(List<Materiale> listaMataeriali, Context context) {
        this.listaMataeriali = listaMataeriali;
        this.context = context;
    }

    @Override
    public Materialeholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.materiale_card,parent,false);
        Materialeholder materialeholder = new Materialeholder(v);
        return materialeholder;
    }

    @Override
    public void onBindViewHolder(Materialeholder holder, int position) {
        holder.textMateriale.setText(listaMataeriali.get(position).getNome());
    }

    @Override
    public int getItemCount() {
        return listaMataeriali.size();
    }

    public class Materialeholder extends RecyclerView.ViewHolder {

        CardView cardMateriale;
        CheckBox checkMateriale;
        EditText quantitaMateriale;
        TextView textMateriale;

        public Materialeholder(View itemView) {
            super(itemView);

            textMateriale = itemView.findViewById(R.id.tnomeMateriale);
            cardMateriale = itemView.findViewById(R.id.card_materiale);
            checkMateriale = itemView.findViewById(R.id.checkBoxMateriale);
            quantitaMateriale = itemView.findViewById(R.id.eQuantita);

        }
    }
}
