/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.brewathome.connection.responses;


public class FermentabileResponse {
    
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
