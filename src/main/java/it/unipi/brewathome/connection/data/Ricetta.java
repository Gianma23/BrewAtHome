package it.unipi.brewathome.connection.data;

import java.io.Serializable;

public class Ricetta implements Serializable {
    
    private int id;
    private String nome;
    private String descrizione;
    private String autore; 
    private String tipo;
    private String stileId;
    private double volume;
    private double rendimento;
    private String ultimaModifica;

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

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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

    public String getStileId() {
        return stileId;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getRendimento() {
        return rendimento;
    }

    public void setRendimento(double rendimento) {
        this.rendimento = rendimento;
    }
    
    public void setStileId(String stileId) {
        this.stileId = stileId;
    }

    public String getUltimaModifica() {
        return ultimaModifica;
    }

    public void setUltimaModifica(String ultimaModifica) {
        this.ultimaModifica = ultimaModifica;
    }

    public Ricetta(int id, String nome, String descrizione, String autore, String tipo, String stileId, double volume, double rendimento) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.autore = autore;
        this.tipo = tipo;
        this.stileId = stileId;
        this.volume = volume;
        this.rendimento = rendimento;
    }
}
