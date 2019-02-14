package com.example.alim.bcm.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alim.bcm.R;
import com.example.alim.bcm.data.model.Materiale;

import java.util.List;

/**
 * Created by alim on 26-Mar-18.
 */

public class MaterialeAdapter extends RecyclerView.Adapter<MaterialeAdapter.Materialeholder> {
    private List<Materiale> listaMataeriali ;
    private Context context;
    private OnMaterialeClickListener onMaterialeClickListener;

    public interface OnMaterialeClickListener{
        void onMaterialeCheck(Materiale materiale);
        void onMaterialeUncheck(Materiale materiale);
    }

    public MaterialeAdapter(List<Materiale> listaMataeriali, Context context, @NonNull OnMaterialeClickListener onMaterialeClickListener) {
        this.listaMataeriali = listaMataeriali;
        this.context = context;
        this.onMaterialeClickListener = onMaterialeClickListener;
    }

    @Override
    public Materialeholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(parent.getContext()).inflate(R.layout.materiale_card,parent,false);
        Materialeholder materialeholder = new Materialeholder(v);
        return materialeholder;
    }

    @Override
    public void onBindViewHolder(final Materialeholder holder, int position) {

        final Materiale materiale= listaMataeriali.get(position);
        holder.textMateriale.setText(materiale.getNome().toUpperCase());

        holder.checkMateriale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (holder.quantitaMateriale.getText().toString().equals("") || Integer.parseInt(holder.quantitaMateriale.getText().toString())<1) {
                        holder.checkMateriale.setChecked(false);
                        Toast.makeText(context, "CONTROLLARE QUANTITA", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        materiale.setQuantita(Integer.parseInt(holder.quantitaMateriale.getText().toString()));
                        onMaterialeClickListener.onMaterialeCheck(materiale);

                    }

                }
                else {
                    holder.quantitaMateriale.setText("");
                    onMaterialeClickListener.onMaterialeUncheck(materiale);
                }
            }
        });
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
