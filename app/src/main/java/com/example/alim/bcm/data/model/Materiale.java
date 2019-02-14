package com.example.alim.bcm.data.model;

import java.io.Serializable;

/**
 * Created by alim on 21-Mar-18.
 */

public class Materiale extends Articolo implements Serializable {

    private String tipo;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Materiale() {
    }





    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }




}
