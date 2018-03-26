package com.example.alim.bcm.adapters;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Attrezzo;

import java.util.List;

import butterknife.BindView;

/**
 * Created by alim on 25-Mar-18.
 */

public class AttrezzoAdapter extends RecyclerView.Adapter<AttrezzoAdapter.AttrezzoHolder> {

    private Context context;
    private List<Attrezzo> listaAttrezzi;


    public AttrezzoAdapter(Context context, List<Attrezzo> listaAttrezzi) {
        this.context = context;
        this.listaAttrezzi = listaAttrezzi;
    }

    @Override
    public AttrezzoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attrezzo_card, parent, false);
        AttrezzoHolder attrezzoHolder = new AttrezzoHolder(v);
        return attrezzoHolder;
    }

    @Override
    public void onBindViewHolder(AttrezzoHolder holder, int position) {
        holder.textAttrezzo.setText(listaAttrezzi.get(position).getNome());
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
