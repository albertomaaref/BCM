package com.example.alim.bcm;

import android.app.Application;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


import java.io.File;
import java.io.IOException;

public class Applicazione extends Application {

    public Applicazione() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public File getDatabasePath(String name) {

        String path = Environment.getExternalStorageDirectory().toString()+"/alimaaref/" + name;

        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return super.openOrCreateDatabase(name, mode, factory);
    }


    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return super.openOrCreateDatabase(name, mode, factory, errorHandler);
    }
}
