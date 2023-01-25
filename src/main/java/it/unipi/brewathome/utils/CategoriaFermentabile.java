package it.unipi.brewathome.utils;


public enum CategoriaFermentabile {
    
    BASE("Base"),
    CRYSTAL_CARAMEL("Crystal/Caramel"),
    TOSTATO("Tostato"),
    ACIDULATO("Acidulato");
    
    private String label;

    CategoriaFermentabile(String label) {
        this.label = label;
    }

    public String toString() {
        return label;
    }
    
    public static int indexOf(String name) {
        switch (name) {
            case "Base":
                return 0;
            case "Crystal/Caramel":
                return 1;
            case "Tostato":
                return 2;
            case "Acidulato":
                return 3;
            default:
                throw new AssertionError("Categoria non esistente");
        }
    }
}
