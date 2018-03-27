package com.example.alim.bcm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alim on 27-Mar-18.
 */

public class Richiesta {
    private int id;
    private String Titolo;
    private Date dataArrivo;
    private List<Materiale> listaMateriali;
    private List<Attrezzo> listaAttrezzi;

    public Richiesta() {
        listaAttrezzi = new ArrayList<>();
        listaMateriali = new ArrayList<>();
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

    public Date getDataArrivo() {
        return dataArrivo;
    }

    public void setDataArrivo(Date dataArrivo) {
        this.dataArrivo = dataArrivo;
    }

    public String getTitolo() {
        return Titolo;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }
}
