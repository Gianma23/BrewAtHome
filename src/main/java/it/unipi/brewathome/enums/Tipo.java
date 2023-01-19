package it.unipi.brewathome.enums;

/**
 *
 * @author Utente
 */
public enum Tipo {
    
    ALL_GRAIN("All Grain"),
    AMMOSTAMENTO_PARZIALE("Ammostamento Parziale"),
    ESTRATTO("Estratto");
    
    private String label;

    Tipo(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
    
    public static int indexOf(String name) {
        switch (name) {
            case "All Grain":
                return 0;
            case "Ammostamento Parziale":
                return 1;
            case "Estratto":
                return 2;
            default:
                throw new AssertionError("Tipo non esistente");
        }
    }
}
