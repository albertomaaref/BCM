package com.example.alim.bcm.model;

import java.io.Serializable;

/**
 * Created by alim on 25-Mar-18.
 */


public class CapoCantiere extends Personale implements Serializable {

    private String cantiere;



    public CapoCantiere() {
    }

    public CapoCantiere(int matricola, float stipendio, String nome, String cognome) {
        super(matricola, stipendio, nome, cognome);
    }

    public String getCantiere() {
        return cantiere;
    }

    public void setCantiere(String cantiere) {
        this.cantiere = cantiere;
    }

}
