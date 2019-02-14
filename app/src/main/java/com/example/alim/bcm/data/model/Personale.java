package com.example.alim.bcm.model;

import java.io.Serializable;

/**
 * Created by alim on 21-Mar-18.
 */

public class Personale implements Serializable {
    private int matricola;
    private float stipendio;
    private String nome;
    private String cognome;

    public Personale() {
    }

    public int getMatricola() {
        return matricola;
    }

    public void setMatricola(int matricola) {
        this.matricola = matricola;
    }

    public float getStipendio() {
        return stipendio;
    }

    public void setStipendio(float stipendio) {
        this.stipendio = stipendio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Personale(int matricola, float stipendio, String nome, String cognome) {

        this.matricola = matricola;
        this.stipendio = stipendio;
        this.nome = nome;
        this.cognome = cognome;
    }
}
