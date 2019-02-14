package com.example.alim.bcm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alim on 25-Mar-18.
 */

public class Autista extends Personale implements Serializable {

    ArrayList<Integer> listaRichieste;

    public Autista() {
        listaRichieste = new ArrayList<>();
    }

    public Autista(int matricola, float stipendio, String nome, String cognome) {
        super(matricola, stipendio, nome, cognome);
    }

    public ArrayList<Integer> getListaRichieste() {
        return listaRichieste;
    }

    public void setListaRichieste(ArrayList<Integer> listaRichieste) {
        this.listaRichieste = listaRichieste;
    }
}
