package com.example.alim.bcm.services;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alim.bcm.R;

import java.util.Date;

/**
 * Created by alim on 04-Apr-18.
 */

public class SelectDataDialog extends DialogFragment {

    private DatePicker datePicker;
    private Button bSelectData;

    private EditText editText;

    public SelectDataDialog() {
    }

    @SuppressLint("ValidFragment")
    public SelectDataDialog(EditText editText) {
        this.editText = editText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_data_dialog,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
         return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        datePicker = view.findViewById(R.id.datePicker);
        bSelectData = view.findViewById(R.id.bSelectData);

        bSelectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(""+datePicker.getDayOfMonth()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getYear());
                getDialog().dismiss();
                getDialog().cancel();

            }
        });

    }


}
