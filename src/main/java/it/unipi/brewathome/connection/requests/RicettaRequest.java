package it.unipi.brewathome.connection.requests;

import java.io.Serializable;

public class RicettaRequest implements Serializable {
    
    private int id;
    private String nome;
    private String autore; 
    private String tipo;
    private int attrezzaturaId;
    private String stileId;
    private int abv;
    private int og;
    private int fg;
    private int ebc;
    private int ibu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAttrezzaturaId() {
        return attrezzaturaId;
    }

    public void setAttrezzaturaId(int attrezzaturaId) {
        this.attrezzaturaId = attrezzaturaId;
    }

    public String getStileId() {
        return stileId;
    }

    public void setStileId(String stileId) {
        this.stileId = stileId;
    }

    public int getAbv() {
        return abv;
    }

    public void setAbv(int abv) {
        this.abv = abv;
    }

    public int getOg() {
        return og;
    }

    public void setOg(int og) {
        this.og = og;
    }

    public int getFg() {
        return fg;
    }

    public void setFg(int fg) {
        this.fg = fg;
    }

    public int getEbc() {
        return ebc;
    }

    public void setEbc(int ebc) {
        this.ebc = ebc;
    }

    public int getIbu() {
        return ibu;
    }

    public void setIbu(int ibu) {
        this.ibu = ibu;
    }

    public RicettaRequest(int id, String nome, String autore, String tipo, int attrezzaturaId, String stileId, int abv, int og, int fg, int ebc, int ibu) {
        this.id = id;
        this.nome = nome;
        this.autore = autore;
        this.tipo = tipo;
        this.attrezzaturaId = attrezzaturaId;
        this.stileId = stileId;
        this.abv = abv;
        this.og = og;
        this.fg = fg;
        this.ebc = ebc;
        this.ibu = ibu;
    }
}
