package com.example.alim.bcm.services;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.alim.bcm.R;

import butterknife.BindView;

public class ConfermaDatiDialog extends DialogFragment  {

    private onButtonClickListener onButtonClickListener;


    TextView tNomeRegister;
    TextView tCognomeRegister;
    TextView tQualificaRegister;
    TextView tDialog;
    ImageButton bCheck;
    ImageButton bClose;
    public static final String GENERIC_MESSAGE ="messaggioGenerico";
    private String nome;
    private String cognome;
    private String qualifica;
    private String modalita = "";
    private String message = "";




    public interface onButtonClickListener{
        public void onCheckClick();
        public void onCloseClick();
    }

    public ConfermaDatiDialog() {
    }

    @SuppressLint("ValidFragment")
    public ConfermaDatiDialog(@NonNull String modalita,@NonNull String message, @NonNull onButtonClickListener onButtonClickListener){
        this.onButtonClickListener = onButtonClickListener;
        this.modalita = modalita;
        this.message = message;

    }

    @SuppressLint("ValidFragment")
    public ConfermaDatiDialog(String nome, String cognome, String qualifica, final onButtonClickListener onButtonClickListener){
        this.nome = nome;
        this.cognome = cognome;
        this.qualifica = qualifica;
        this.onButtonClickListener = onButtonClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.conferma_dati,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tNomeRegister = view.findViewById(R.id.tNomeRegister);
        tCognomeRegister = view.findViewById(R.id.tCognomeRegister);
        tQualificaRegister = view.findViewById(R.id.tQualificaRegister);
        tDialog = view.findViewById(R.id.tDialog);
        bCheck = view.findViewById(R.id.bCheck);
        bClose = view.findViewById(R.id.bClose);



        if (modalita.equals(GENERIC_MESSAGE)){
            tCognomeRegister.setVisibility(View.GONE);
            tNomeRegister.setVisibility(View.GONE);
            tQualificaRegister.setVisibility(View.GONE);
            tDialog.setText(message.toUpperCase());
            tDialog.setVisibility(View.VISIBLE);

        }
        else {
            tNomeRegister.setText(nome.toUpperCase());
            tCognomeRegister.setText(cognome.toUpperCase());
            tQualificaRegister.setText(qualifica.toUpperCase());
            tDialog.setVisibility(View.GONE);
        }

        bCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                getDialog().cancel();
                onButtonClickListener.onCheckClick();
            }
        });

        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonClickListener.onCloseClick();
                getDialog().dismiss();
                getDialog().cancel();
            }
        });



    }

    public void holdData(String nome, String cognome, String qualifica, final onButtonClickListener onButtonClickListener){

    }


}
