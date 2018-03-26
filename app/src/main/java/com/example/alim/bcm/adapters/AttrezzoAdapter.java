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
import android.widget.TextView;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Attrezzo;

import java.util.List;

/**
 * Created by alim on 25-Mar-18.
 */

public class AttrezzoAdapter extends RecyclerView.Adapter<AttrezzoAdapter.AttrezzoHolder> {

    private Context context;
    private List<Attrezzo> listaAttrezzi;

    public interface OnAttrezzoClickListener {
        void onAttrezzoCheck(Attrezzo attrezzo);
        void onAttrezzoUncheck(Attrezzo attrezzo);
    }

    private OnAttrezzoClickListener onAttrezzoClickListener;


    public AttrezzoAdapter(Context context, List<Attrezzo> listaAttrezzi, @NonNull OnAttrezzoClickListener onAttrezzoClickListener) {
        this.context = context;
        this.listaAttrezzi = listaAttrezzi;
        this.onAttrezzoClickListener = onAttrezzoClickListener;
    }

    @Override
    public AttrezzoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attrezzo_card, parent, false);
        AttrezzoHolder attrezzoHolder = new AttrezzoHolder(v);
        return attrezzoHolder;
    }

    @Override
    public void onBindViewHolder(final AttrezzoHolder holder, int position) {
        holder.textAttrezzo.setText(listaAttrezzi.get(position).getNome());
        final Attrezzo attrezzo = listaAttrezzi.get(position);

        holder.checkBoxCestino.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    onAttrezzoClickListener.onAttrezzoCheck(attrezzo);

                }
                else {
                    onAttrezzoClickListener.onAttrezzoUncheck(attrezzo);

                }
            }
        });

//        holder.attrezzo_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (holder.checkBoxCestino.isChecked()){
//                    onAttrezzoClickListener.onAttrezzoCheck(attrezzo);
//                }
//                else{
//                    onAttrezzoClickListener.onAttrezzoUncheck(attrezzo);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listaAttrezzi.size();
    }

    public class AttrezzoHolder extends RecyclerView.ViewHolder {

        CardView attrezzo_card;
        TextView textAttrezzo;
        CheckBox checkBoxCestino;

        public AttrezzoHolder(View itemView) {
            super(itemView);
            textAttrezzo = itemView.findViewById(R.id.text_attrezzo);
            checkBoxCestino = itemView.findViewById(R.id.checkBoxCestino);
            attrezzo_card = itemView.findViewById(R.id.card_attrezzo);
        }
    }
}
