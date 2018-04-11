package com.example.alim.bcm.model;

import java.io.Serializable;

/**
 * Created by alim on 29-Mar-18.
 */

public class Articolo implements Serializable{
    private boolean conseganto ;

    private String nome;
    private String marca;
    private String modello;
    private int quantita;


    public Articolo() {
        this.conseganto = false;
    }

    public boolean isConseganto() {
        return conseganto;
    }

    public void setConseganto(boolean conseganto) {
        this.conseganto = conseganto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
