package it.unipi.brewathome.connection.responses;

import java.io.Serializable;

public class LuppoloResponse implements Serializable{
    
    private String nome;
    private int quantita; 
    private double alpha;
    private int tempo;

    public String getNome() {
        return nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public double getAlpha() {
        return alpha;
    }

    public int getTempo() {
        return tempo;
    }

    public LuppoloResponse(String nome, int quantita, double alpha, int tempo) {
        this.nome = nome;
        this.quantita = quantita;
        this.alpha = alpha;
        this.tempo = tempo;
    }
}
