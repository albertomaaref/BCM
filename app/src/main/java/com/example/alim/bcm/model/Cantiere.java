package com.example.alim.bcm.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by alim on 21-Mar-18.
 */

public class Cantiere implements Serializable {

    private int statoFase1;
    private int statoFase2;
    private int statoFase3;
    private int statoFase4;
    private String indirizzo="";
    private int oreLavorate;
    private float costoLavoratori;
    private String capoCantiere;



    public Cantiere() {
    }


}
