package com.example.alim.bcm.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataIn extends DataInBase {

    Map<String,Attrezzo> attrezzi = null;
    Map<String,Materiale> materiali=  null;
    Map<String,Cantiere> cantieri = null;
    Map<String,Richiesta> richieste = null;
    HashMap<String,HashMap<String,Personale>> utenti= null;

    public DataIn() {
    }
}
