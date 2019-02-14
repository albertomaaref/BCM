package com.example.alim.bcm.data.model;

import java.io.Serializable;

/**
 * Created by alim on 25-Mar-18.
 */

public class Operaio extends Personale implements Serializable {
    public Operaio() {
    }

    public Operaio(int matricola, float stipendio, String nome, String cognome) {
        super(matricola, stipendio, nome, cognome);
    }
}
