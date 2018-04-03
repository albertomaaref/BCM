package com.example.alim.bcm.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.alim.bcm.CapoCantiereActivity;
import com.example.alim.bcm.ImpiegatoActivity;
import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Richiesta;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by alim on 29-Mar-18.
 */

public class RequestManager {


    public static void sendRequest(final Context context, final Richiesta richiesta, final String type, final android.support.v4.app.FragmentManager fragmentManager, final Fragment fragment) {
        final DatabaseReference ref;
        FirebaseDatabase database;
        database = FirebaseDatabase.getInstance();
        ref = database.getReferenceFromUrl(FireBaseConnection.BASE_URL);
        // controllo se è stato scelto un cantiere
        if (richiesta.getCantiere().equalsIgnoreCase("seleziona cantiere")) {
            Toast.makeText(context, "SELEZIONARE CANTIERE", Toast.LENGTH_SHORT).show();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Procedere con la richiesta ?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Activity activity = (Activity) context;
                    int idContainer = 0;
                    if (activity instanceof ImpiegatoActivity) {
                        idContainer = R.id.fragmentImpiegato;
                    } else if (activity instanceof CapoCantiereActivity) {
                        idContainer = R.id.fragmentCapo;
                    }
                    ref.child("richieste/" + richiesta.getId() + "/id").setValue(richiesta.getId());
                    ref.child("richieste/" + richiesta.getId() + "/cantiere").setValue(richiesta.getCantiere());

                //int lunghezza = 0;
                if (type.equals(Constants.ATTREZZI)) {
                    ref.child("richieste/"+richiesta.getId()+"/lista"+type).setValue(richiesta.getListaAttrezzi());
                    //lunghezza = richiesta.getListaAttrezzi().size();
                    /*for (int i = 0 ; i< lunghezza; i++){
                        ref.child("richieste/"+richiesta.getId()+"/lista"+type+"/"+(i+1)).setValue(richiesta.getListaAttrezzi().get(i));
                    }*/
                }
                else if (type.equals(Constants.MATERIALI)) {
                    ref.child("richieste/"+richiesta.getId()+"/lista"+type).setValue(richiesta.getListaMateriali());

                }
                    //lunghezza  = richiesta.getListaMateriali().size();
                /*for (int i = 0 ; i<= lunghezza; i++){

                }*/

                    ItemsManager itemsManager = ItemsManager.getDownloadItems();
                    itemsManager.removeAll();
                    fragmentManager.beginTransaction().replace(idContainer, fragment).commit();

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

}
