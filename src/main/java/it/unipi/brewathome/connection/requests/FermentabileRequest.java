package it.unipi.brewathome.connection.requests;

import java.io.Serializable;

public class FermentabileRequest implements Serializable {
    
    private int id;
    private int ricettaId;
    private String nome; 
    private int quantita; 
    private String categoria; 
    private String fornitore;
    private String provenienza;
    private String tipo;
    private int colore;
    private int potenziale;
    private int rendimento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getRicettaId() {
        return ricettaId;
    }

    public void setRicetta_id(int ricetta_id) {
        this.ricettaId = ricetta_id;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public int getColore() {
        return colore;
    }

    public void setColore(int colore) {
        this.colore = colore;
    }

    public int getPotenziale() {
        return potenziale;
    }

    public void setPotenziale(int potenziale) {
        this.potenziale = potenziale;
    }

    public int getRendimento() {
        return rendimento;
    }

    public void setRendimento(int rendimento) {
        this.rendimento = rendimento;
    }

    public FermentabileRequest(int id, int ricetta_id, String nome, int quantita, String categoria, String fornitore, String provenienza, String tipo, int colore, int potenziale, int rendimento) {
        this.id = id;
        this.ricettaId = ricetta_id;
        this.nome = nome;
        this.quantita = quantita;
        this.categoria = categoria;
        this.fornitore = fornitore;
        this.provenienza = provenienza;
        this.tipo = tipo;
        this.colore = colore;
        this.potenziale = potenziale;
        this.rendimento = rendimento;
    }
}
