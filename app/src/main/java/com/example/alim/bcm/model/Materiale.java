package com.example.alim.bcm.model;

import java.io.Serializable;

/**
 * Created by alim on 21-Mar-18.
 */

public class Materiale extends Articolo implements Serializable {
    private String modello;
    private String tipo;
    private String nome;
    private String marca;
    private String id;
    private int quantita;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Materiale() {
    }

    public Materiale(String modello, String tipo, String nome, String marca) {
        this.modello = modello;
        this.tipo = tipo;
        this.nome = nome;
        this.marca = marca;
    }

    public String getModello() {
        return modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
