package com.example.alim.bcm.data.model;

import java.io.Serializable;

/**
 * Created by alim on 25-Mar-18.
 */

public class Impiegato extends Personale implements Serializable {
    public Impiegato(int matricola, float stipendio, String nome, String cognome) {
        super(matricola, stipendio, nome, cognome);
    }

    public Impiegato() {
    }
}
