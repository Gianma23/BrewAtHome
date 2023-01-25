package it.unipi.brewathome.connection.data;

import java.io.Serializable;

public class Luppolo implements Serializable {
    
    private int id;
    private int ricettaId;
    private String nome;
    private int tempo;
    private int quantita; 
    private String fornitore;
    private String provenienza;
    private String tipo;
    private double alpha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRicettaId() {
        return ricettaId;
    }

    public void setRicettaId(int ricettaId) {
        this.ricettaId = ricettaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public String getProvenienza() {
        return provenienza;
    }

    public void setProvenienza(String provenienza) {
        this.provenienza = provenienza;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public Luppolo(int id, int ricettaId, String nome, int tempo, int quantita, String fornitore, String provenienza, String tipo, double alpha) {
        this.id = id;
        this.ricettaId = ricettaId;
        this.nome = nome;
        this.tempo = tempo;
        this.quantita = quantita;
        this.fornitore = fornitore;
        this.provenienza = provenienza;
        this.tipo = tipo;
        this.alpha = alpha;
    }

        @Override
    public boolean equals(Object obj) {
        if(obj.getClass()!=Luppolo.class)
            return false;
        
        Luppolo lup = (Luppolo) obj;
        return (lup.id == this.id);
    }
}
