package it.unipi.brewathome.connection.responses;

import java.io.Serializable;
import java.util.Date;

public class FermentabileResponse implements Serializable{
    
    private String nome;
    private int quantita; 
    private int colore;
    private String categoria;

    public String getNome() {
        return nome;
    }

    public int getQuantita() {
        return quantita;
    }

    public int getColore() {
        return colore;
    }
    
    public String getCategoria() {
        return categoria;
    }

    public FermentabileResponse(String nome, int quantita, int colore, String categoria) {
        this.nome = nome;
        this.quantita = quantita;
        this.colore = colore;
        this.categoria = categoria;
    }
}
