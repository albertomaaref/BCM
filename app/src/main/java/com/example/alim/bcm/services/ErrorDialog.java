package com.example.alim.bcm.services;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alim.bcm.R;

public class ErrorDialog extends DialogFragment {

    private static ErrorDialog instance = null;
    private TextView tErrorDialog;

    @SuppressLint("ValidFragment")
    private ErrorDialog() {
        super();
    }

    public static ErrorDialog getInstance() {
        if (instance == null) {
            instance = new ErrorDialog();
        }

        return instance;
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error_dialog, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tErrorDialog = view.findViewById(R.id.tErrorDialog);

        super.onViewCreated(view, savedInstanceState);


    }

    public void show(FragmentManager manager, String tag, String text) {
        tErrorDialog.setText(text);
        super.show(manager, tag);
    }
}
