package com.example.alim.bcm.utilities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.example.alim.bcm.R;
import com.example.alim.bcm.fragments.AttrezzziFragment;
import com.example.alim.bcm.model.Articolo;
import com.example.alim.bcm.model.Richiesta;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by alim on 29-Mar-18.
 */

public class RequestManager {





    public static void sendRequest(final Context context, final Richiesta  richiesta, final String type, final android.support.v4.app.FragmentManager fragmentManager, final Fragment fragment){
        final DatabaseReference ref;
        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Procedere con la richiesta ?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownloadItems downloadItems = DownloadItems.getDownloadItems();
                downloadItems.removeAll();
                fragmentManager.beginTransaction().replace(R.id.fragmentCapo,fragment).commit();
                ref.child("richieste/"+richiesta.getId()+"/").setValue(richiesta);
                //ref.child("richieste/"+richiesta.getId()+"/lista"+type).setValue(richiesta.getListaAttrezzi());
                // sono arrivato qui



            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
