package com.example.alim.bcm.utilities;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static com.example.alim.bcm.data.model.Constants.ATTREZZI;
import static com.example.alim.bcm.data.model.Constants.CANTIERI;
import static com.example.alim.bcm.data.model.Constants.CAPOCANTIERE;
import static com.example.alim.bcm.data.model.Constants.LISTA_AUTISTI;
import static com.example.alim.bcm.data.model.Constants.MATERIALI;
import static com.example.alim.bcm.data.model.Constants.UTENTE_ATTIVO;

public class InternalStorage {

    private InternalStorage() {
    }

    public static void writeObject(Context context, String key, Object object) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(key, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(Context context, String key) {
        FileInputStream fis = null;
        ObjectInput ois = null;
        Object object = null;
        try {
            fis = context.openFileInput(key);
            ois = new ObjectInputStream(fis);
            object = ois.readObject();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;

    }

    public static void resetDB(Context context, String flag) {
        if (flag.equalsIgnoreCase("")) {

            InternalStorage.writeObject(context, ATTREZZI, null);
            InternalStorage.writeObject(context, MATERIALI, null);
            InternalStorage.writeObject(context, CANTIERI, null);
            InternalStorage.writeObject(context, CAPOCANTIERE, null);
            InternalStorage.writeObject(context, LISTA_AUTISTI, null);
            InternalStorage.writeObject(context, UTENTE_ATTIVO, null);
            InternalStorage.writeObject(context,"id"+ATTREZZI,null);
            InternalStorage.writeObject(context,"id"+MATERIALI,null);
        } else if (flag.equalsIgnoreCase(ATTREZZI)) {
            InternalStorage.writeObject(context, ATTREZZI, null);
        } else if (flag.equalsIgnoreCase(MATERIALI)) {
            InternalStorage.writeObject(context, MATERIALI, null);
        } else if (flag.equalsIgnoreCase(CANTIERI)) {
            InternalStorage.writeObject(context, CANTIERI, null);
        } else if (flag.equalsIgnoreCase(CAPOCANTIERE)) {
            InternalStorage.writeObject(context, CAPOCANTIERE, null);
        } else if (flag.equalsIgnoreCase(LISTA_AUTISTI)) {
            InternalStorage.writeObject(context, LISTA_AUTISTI, null);
        } else if (flag.equalsIgnoreCase(UTENTE_ATTIVO)) {
            InternalStorage.writeObject(context, UTENTE_ATTIVO, null);
        }


    }

}
