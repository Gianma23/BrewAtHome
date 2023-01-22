package it.unipi.brewathome.connection.responses;

import java.io.Serializable;

public class Stile implements Serializable {
    
    private String nome; 
    private double abvMin;
    private double abvMax;
    private double ogMin;
    private double ogMax;
    private double fgMin;
    private double fgMax;
    private int srmMin;
    private int srmMax;
    private int ibuMin;
    private int ibuMax;
    
    public Stile() {
    }

    public Stile(String nome, double abvMin, double abvMax, double ogMin, double ogMax, double fgMin, double fgMax, int srmMin, int srmMax, int ibuMin, int ibuMax) {
        this.nome = nome;
        this.abvMin = abvMin;
        this.abvMax = abvMax;
        this.ogMin = ogMin;
        this.ogMax = ogMax;
        this.fgMin = fgMin;
        this.fgMax = fgMax;
        this.srmMin = srmMin;
        this.srmMax = srmMax;
        this.ibuMin = ibuMin;
        this.ibuMax = ibuMax;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getAbvMin() {
        return abvMin;
    }

    public void setAbvMin(double abvMin) {
        this.abvMin = abvMin;
    }

    public double getAbvMax() {
        return abvMax;
    }

    public void setAbvMax(double abvMax) {
        this.abvMax = abvMax;
    }

    public double getOgMin() {
        return ogMin;
    }

    public void setOgMin(double ogMin) {
        this.ogMin = ogMin;
    }

    public double getOgMax() {
        return ogMax;
    }

    public void setOgMax(double ogMax) {
        this.ogMax = ogMax;
    }

    public double getFgMin() {
        return fgMin;
    }

    public void setFgMin(double fgMin) {
        this.fgMin = fgMin;
    }

    public double getFgMax() {
        return fgMax;
    }

    public void setFgMax(double fgMax) {
        this.fgMax = fgMax;
    }

    public int getSrmMin() {
        return srmMin;
    }

    public void setSrmMin(int srmMin) {
        this.srmMin = srmMin;
    }

    public int getSrmMax() {
        return srmMax;
    }

    public void setSrmMax(int srmMax) {
        this.srmMax = srmMax;
    }

    public int getIbuMin() {
        return ibuMin;
    }

    public void setIbuMin(int ibuMin) {
        this.ibuMin = ibuMin;
    }

    public int getIbuMax() {
        return ibuMax;
    }

    public void setIbuMax(int ibuMax) {
        this.ibuMax = ibuMax;
    }
}
