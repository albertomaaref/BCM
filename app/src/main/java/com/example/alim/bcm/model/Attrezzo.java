package com.example.alim.bcm.model;

import java.io.Serializable;

/**
 * Created by alim on 21-Mar-18.
 */

public class Attrezzo extends Articolo implements Serializable {


    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private String id;


    public Attrezzo() {
    }







    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
