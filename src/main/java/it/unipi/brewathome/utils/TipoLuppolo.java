package it.unipi.brewathome.utils;


public enum TipoLuppolo {
    
    PELLET("Pellet"),
    CONI("Coni"),
    CRYO("Cryo");
    
    private String label;

    TipoLuppolo(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
    
    public static int indexOf(String name) {
        switch (name) {
            case "Pellet":
                return 0;
            case "Coni":
                return 1;
            case "Cryo":
                return 2;
            default:
                throw new AssertionError("Tipo non esistente");
        }
    }
}
