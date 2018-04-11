package com.example.alim.bcm.utilities;

import android.util.Log;

import com.example.alim.bcm.R;
import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Autista;
import com.example.alim.bcm.model.Constants;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.model.Richiesta;
import com.example.alim.bcm.model.StatoRichiesta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alim on 22-Mar-18.
 */

public final class JsonParser {

    public static String getPassword(String string) {

        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (key.toLowerCase().equals("password")) {
                    return object.getString(key);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static List<Attrezzo> getAttrezzi(String string) {
        List<Attrezzo> lista = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();
            while (keys.hasNext()){
                Attrezzo attrezzo= new Attrezzo();
                String key = (String) keys.next();
                attrezzo.setId(key);
                JSONObject oggetto = object.getJSONObject(key);
                Iterator chiavi = oggetto.keys();
                while (chiavi.hasNext()){
                    String chiave = (String) chiavi.next();
                    if (chiave.equals("nome")){
                        attrezzo.setNome(oggetto.getString(chiave));
                    }
                    else if (chiave.equals("marca")){
                        attrezzo.setMarca(oggetto.getString(chiave));
                    }
                    else if (chiave.equals("modello")){
                        attrezzo.setModello(oggetto.getString(chiave));
                    }
                    else return null;
                }

                lista.add(attrezzo);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return lista;
    }


    public static List<Materiale> getMateriali(String string){
        List<Materiale> lista = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();
            while (keys.hasNext()){
                Materiale materiale = new Materiale();
                String key = (String) keys.next();
                materiale.setId(key);
                JSONObject oggetto = object.getJSONObject(key);
                Iterator chiavi = oggetto.keys();
                while (chiavi.hasNext()){
                    String chiave = (String) chiavi.next();
                    if (chiave.equals("nome")){
                        materiale.setNome(oggetto.getString(chiave));
                    }
                    else if (chiave.equals("marca")){
                        materiale.setMarca(oggetto.getString(chiave));
                    }
                    else if (chiave.equals("modello")){
                        materiale.setModello(oggetto.getString(chiave));
                    }
                    else return null;
                }
                lista.add(materiale);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Richiesta> getRichieste(String string){
        List<Richiesta> list = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(string);
            Iterator keys = object.keys();
            while (keys.hasNext()){
                Richiesta richiesta = new Richiesta();
                String key =(String) keys.next();
                richiesta.setId(Integer.parseInt(key));
                JSONObject oggetto = object.getJSONObject(key);
                Iterator chiavi = oggetto.keys();
                while (chiavi.hasNext()){
                    String chiave = (String) chiavi.next();
                    if (chiave.equalsIgnoreCase("cantiere"))  richiesta.setCantiere(oggetto.getString(chiave));
                    else if (chiave.equalsIgnoreCase("listaattrezzi")){
                        JSONArray jArray = oggetto.getJSONArray(chiave);
                        List<Attrezzo> attrezzoList = new ArrayList<>();
                        if (jArray != null) {
                            for (int i=0;i<jArray.length();i++){
                                String s = jArray.getString(i);
                                JSONObject jsonObject = new JSONObject(s);
                                Attrezzo attrezzo = new Attrezzo();
                                Iterator clefs = jsonObject.keys();
                                while (clefs.hasNext()){
                                    String cle = (String) clefs.next();
                                    if (cle.equalsIgnoreCase("quantita")) attrezzo.setQuantita(Integer.parseInt(jsonObject.getString(cle)));
                                    else if (cle.equalsIgnoreCase("id")) attrezzo.setId(jsonObject.getString(cle));
                                    else if (cle.equalsIgnoreCase("nome")) attrezzo.setNome(jsonObject.getString(cle));
                                }
                                attrezzoList.add(attrezzo);

                            }

                        }

                        richiesta.setListaAttrezzi(attrezzoList);
                        /*Iterator cles = objet.keys();
                        List<Attrezzo> attrezzoList = new ArrayList<>();
                        while (cles.hasNext()){
                            String cle = (String) cles.next();
                            Attrezzo attrezzo = new Attrezzo();
                            attrezzo = (Attrezzo) object.get(cle);
                            attrezzoList.add(attrezzo);
                        }
                        richiesta.setListaAttrezzi(attrezzoList);*/

                    }
                    else if (chiave.equalsIgnoreCase("listamateriali")){
                        JSONArray jArray = oggetto.getJSONArray(chiave);
                        List<Materiale> materialeList = new ArrayList<>();
                        if (jArray != null) {

                            for (int i=0;i<jArray.length();i++){
                                String s = jArray.getString(i);
                                JSONObject jsonObject = new JSONObject(s);
                                Materiale materiale = new Materiale();
                                Iterator clefs = jsonObject.keys();
                                while (clefs.hasNext()){
                                    String cle = (String) clefs.next();
                                    if (cle.equalsIgnoreCase("quantita")) materiale.setQuantita(Integer.parseInt(jsonObject.getString(cle)));
                                    else if (cle.equalsIgnoreCase("id")) materiale.setId(jsonObject.getString(cle));
                                    else if (cle.equalsIgnoreCase("nome")) materiale.setNome(jsonObject.getString(cle));
                                }

                                materialeList.add(materiale);

                            }

                        }


                        richiesta.setListaMateriali(materialeList);
                        /*Iterator cles = objet.keys();
                        List<Materiale> materialeList = new ArrayList<>();
                        while (cles.hasNext()){
                            String cle = (String) cles.next();
                            Materiale materiale = new Materiale();
                            materiale = (Materiale) object.get(cle);
                            materialeList.add(materiale);
                        }
                        richiesta.setListaMateriali(materialeList);*/
                    }
                    else if (chiave.equalsIgnoreCase("dataconsegna")) {
                        richiesta.setDataConesgna(oggetto.getString(chiave));
                    }
                    else if (chiave.equalsIgnoreCase("nota")){
                        richiesta.setTestoLibero(oggetto.getString(chiave));
                    }
                    else if (chiave.equalsIgnoreCase("stato"))
                        richiesta.setStato(StatoRichiesta.valueOf(oggetto.getString(chiave)) );
                    else if (chiave.equalsIgnoreCase("corriere"))
                        richiesta.setAutista(oggetto.getString(chiave));
                }
                list.add(richiesta);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Autista> getAutisti(String string) {
        List<Autista> autisti = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()){
                    Autista autista = new Autista();
                    String key = (String) keys.next();
                    JSONObject oggetto = jsonObject.getJSONObject(key);
                    Iterator chiavi = oggetto.keys();
                    while (chiavi.hasNext()){
                        String chiave = (String) chiavi.next();
                        if (chiave.equalsIgnoreCase("nome")){
                            autista.setNome(oggetto.getString(chiave));
                        }
                        else if (chiave.equalsIgnoreCase("cognome")){
                            autista.setCognome(oggetto.getString(chiave));
                        }
                        else if (chiave.equalsIgnoreCase("stipendio")){
                            autista.setStipendio(Float.parseFloat(oggetto.getString(chiave)));
                        }
                        else if (chiave.equalsIgnoreCase("listarichieste")){
                            JSONObject objet = oggetto.getJSONObject(chiave);
                            Iterator clefs = objet.keys();
                            while (clefs.hasNext()){
                                String clef = (String) clefs.next();
                                Integer richiesta = (Integer) objet.get(clef);
                                autista.getListaRichieste().add(richiesta);
                            }
                        }


                    }
                    autisti.add(autista);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return autisti;
    }

}
