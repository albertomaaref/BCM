package com.example.alim.bcm.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by alim on 21-Mar-18.
 */

public class Cantiere implements Serializable {
    private HashMap<String,Materiale> listaMateriali;
    private HashMap<String,Attrezzo> listaAttrezzi;
    private String indirizzo;
    private Integer statoDiAvanzamento;
    private HashMap<String,Personale> listaPersonale;


    public Cantiere() {
    }

    public Cantiere(HashMap<String, Materiale> listaMateriali, HashMap<String, Attrezzo> listaAttrezzi) {
        this.listaMateriali = listaMateriali;
        this.listaAttrezzi = listaAttrezzi;
    }

    public HashMap<String, Materiale> getListaMateriali() {
        return listaMateriali;
    }

    public HashMap<String, Attrezzo> getListaAttrezzi() {
        return listaAttrezzi;
    }

    public Integer getStatoDiAvanzamento() {

        return statoDiAvanzamento;
    }

    public void setStatoDiAvanzamento(Integer statoDiAvanzamento) {
        this.statoDiAvanzamento = statoDiAvanzamento;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public void addPersonaleToCantiere(Personale personale){
        listaPersonale.put(personale.getNome(),personale);
    }
}
