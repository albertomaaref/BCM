package com.example.alim.bcm.utilities;

import com.example.alim.bcm.model.Attrezzo;
import com.example.alim.bcm.model.Materiale;
import com.example.alim.bcm.model.Richiesta;

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
                    if (chiave.equalsIgnoreCase("listaattrezzi")){
                        JSONObject objet = oggetto.getJSONObject(chiave);
                        Iterator cles = objet.keys();
                        List<Attrezzo> attrezzoList = new ArrayList<>();
                        while (cles.hasNext()){
                            String cle = (String) cles.next();
                            Attrezzo attrezzo = new Attrezzo();
                            attrezzo = (Attrezzo) object.get(cle);
                            attrezzoList.add(attrezzo);
                        }

                    }
                }
                list.add(richiesta);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

}
