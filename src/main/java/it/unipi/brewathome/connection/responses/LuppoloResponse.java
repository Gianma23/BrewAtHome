package it.unipi.brewathome.connection.responses;

public class LuppoloResponse {
    
    private String nome;
    private int tempo;
    private int quantita; 
    private double alpha;

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

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public LuppoloResponse(String nome, int tempo, int quantita, double alpha) {
        this.nome = nome;
        this.tempo = tempo;
        this.quantita = quantita;
        this.alpha = alpha;
    }
}
