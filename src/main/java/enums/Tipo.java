/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enums;

/**
 *
 * @author Utente
 */
public enum Tipo {
    
    ALL_GRAIN("All Grain"),
    AMMOSTAMENTO_PARZIALE("Ammostamento parziale"),
    ESTRATTO("Estratto");
    
    
    private String label;

    Tipo(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
}
