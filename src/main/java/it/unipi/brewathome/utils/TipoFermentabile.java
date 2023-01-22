package it.unipi.brewathome.utils;

/**
 *
 * @author Utente
 */
public enum TipoFermentabile {
    
    GRANI("Grani"),
    ZUCCHERO("Zucchero"),
    ESTRATTO_LIQUIDO("Estratto liquido"),
    ESTRATTO_SECCO("Estratto secco"),
    AGGIUNTA("Aggiunta"),
    ALTRO("Altro");
    
    private String label;

    TipoFermentabile(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
    
    public static int indexOf(String name) {
        switch (name) {
            case "Grani":
                return 0;
            case "Zucchero":
                return 1;
            case "Estratto liquido":
                return 2;
            case "Estratto secco":
                return 3;
            case "Aggiunta":
                return 4;
            case "Altro":
                return 5;
            default:
                throw new AssertionError("Tipo non esistente");
        }
    }
}
