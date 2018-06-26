package com.example.alim.bcm.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.alim.bcm.R;

/**
 * A simple {@link Fragment} subclass.
*/
public class CantiereFragment extends Fragment {

    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private SeekBar seekBar3;
    private SeekBar seekBar4;
    private SeekBar seekBartot;
    private Button bAggiorna;
    private Button bSalva;
    private TextView tIndirizzo;
    private Spinner spStatoCantiere;


    public CantiereFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // layout statoCantiere
        tIndirizzo = view.findViewById(R.id.tIndirizzoCantiere);
        seekBar1 = view.findViewById(R.id.seekBar1);
        seekBar2 = view.findViewById(R.id.seekBar2);
        seekBar3 = view.findViewById(R.id.seekBar3);
        seekBar4 = view.findViewById(R.id.seekBar4);
        seekBartot = view.findViewById(R.id.seekBarTot);
        seekBartot.setEnabled(false);
        bAggiorna = view.findViewById(R.id.bAggiornaCantiere);
        bSalva = view.findViewById(R.id.bSalvaCantiere);
        bSalva.setEnabled(false);
        setSeekValue(40,2,10,6);

        bAggiorna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abilitaCampi();

            }
        });


    }

    private void abilitaCampi() {
        bAggiorna.setEnabled(false);
        bSalva.setEnabled(true);
        seekBar1.setEnabled(true);
        seekBar2.setEnabled(true);
        seekBar3.setEnabled(true);
        seekBar4.setEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_cantiere, container, false);

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void setSeekValue(int v1, int v2, int v3, int v4){
        seekBar1.setProgress(v1);
        seekBar1.setEnabled(false);
        seekBar2.setProgress(v2);
        seekBar2.setEnabled(false);
        seekBar3.setProgress(v3);
        seekBar3.setEnabled(false);
        seekBar4.setProgress(v4);
        seekBar4.setEnabled(false);
        seekBartot.setProgress((int)(v1+v2+v3+v4)/4);
    }





}
