package com.example.alim.bcm.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alim on 27-Mar-18.
 */

public class Richiesta implements Serializable{
    private int id;
    private String cantiere;
    private String dataConesgna;
    private List<Materiale> listaMateriali;
    private List<Attrezzo> listaAttrezzi;
    private String testoLibero ;
    private StatoRichiesta stato;
    private String autista;

    public Richiesta() {
        listaAttrezzi = new ArrayList<>();
        listaMateriali = new ArrayList<>();
        autista = "NON ASSEGNATO";
        testoLibero="";
    }

    public List<Materiale> getListaMateriali() {
        return listaMateriali;
    }

    public void setListaMateriali(List<Materiale> listaMateriali) {
        this.listaMateriali = listaMateriali;
    }

    public List<Attrezzo> getListaAttrezzi() {
        return listaAttrezzi;
    }

    public void setListaAttrezzi(List<Attrezzo> listaAttrezzi) {
        this.listaAttrezzi = listaAttrezzi;
    }

    public void addMateriale(Materiale materiale){
        listaMateriali.add(materiale);
    }

    public void addAttrezzo(Attrezzo attrezzo){
        listaAttrezzi.add(attrezzo);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataConesgna() {
        return dataConesgna;
    }

    public void setDataConesgna(String dataConesgna) {
        this.dataConesgna = dataConesgna;
    }

    public String getCantiere() {
        return cantiere;
    }

    public void setCantiere(String cantiere) {
        this.cantiere = cantiere;
    }

    public String getTestoLibero() {
        return testoLibero;
    }

    public void setTestoLibero(String testoLibero) {
        this.testoLibero = testoLibero;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public String getAutista() {
        return autista;
    }

    public void setAutista(String autista) {
        this.autista = autista;
    }
}
