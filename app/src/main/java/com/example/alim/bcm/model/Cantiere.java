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
    private String indirizzo = "";
    private String citta = "";
    private int oreLavorate;
    private float costoLavoratori;
    private String capoCantiere="";
    private String dataInizio="";
    private String dataFine="";
    private float valoreAppalto;


    public Cantiere() {
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public Cantiere(int statoFase1, int statoFase2, int statoFase3, int statoFase4, String indirizzo, String citta, int oreLavorate, float valoreAppalto, float costoLavoratori, String capoCantiere, String dataInizio, String dataFine) {
        this.statoFase1 = statoFase1;
        this.statoFase2 = statoFase2;
        this.statoFase3 = statoFase3;
        this.statoFase4 = statoFase4;
        this.indirizzo = indirizzo;
        this.citta = citta;

        this.valoreAppalto = valoreAppalto;
        this.oreLavorate = oreLavorate;
        this.costoLavoratori = costoLavoratori;
        this.capoCantiere = capoCantiere;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
    }

    public String getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public int getStatoFase1() {
        return statoFase1;
    }

    public void setStatoFase1(int statoFase1) {
        this.statoFase1 = statoFase1;
    }

    public int getStatoFase2() {
        return statoFase2;
    }

    public void setStatoFase2(int statoFase2) {
        this.statoFase2 = statoFase2;
    }

    public int getStatoFase3() {
        return statoFase3;
    }

    public void setStatoFase3(int statoFase3) {
        this.statoFase3 = statoFase3;
    }

    public int getStatoFase4() {
        return statoFase4;
    }

    public void setStatoFase4(int statoFase4) {
        this.statoFase4 = statoFase4;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int getOreLavorate() {
        return oreLavorate;
    }

    public void setOreLavorate(int oreLavorate) {
        this.oreLavorate = oreLavorate;
    }

    public float getCostoLavoratori() {
        return costoLavoratori;
    }

    public void setCostoLavoratori(float costoLavoratori) {
        this.costoLavoratori = costoLavoratori;
    }

    public String getCapoCantiere() {
        return capoCantiere;
    }

    public void setCapoCantiere(String capoCantiere) {
        this.capoCantiere = capoCantiere;
    }

    public float getValoreAppalto() {
        return valoreAppalto;
    }

    public void setValoreAppalto(float valoreAppalto) {
        this.valoreAppalto = valoreAppalto;
    }
}
