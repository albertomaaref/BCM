package com.example.alim.bcm.model;

import java.io.Serializable;

/**
 * Created by alim on 21-Mar-18.
 */

public class Attrezzo extends Articolo implements Serializable {
    private int quantita;


    private String id;
    private String modello;
    private String marca;
    private String nome;

    public Attrezzo() {
    }

    public Attrezzo(String modello, String marca, String nome) {
        this.modello = modello;
        this.marca = marca;
        this.nome = nome;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
