package it.unipi.brewathome.utils;

// classe statica, simile a java.lang.Math

import java.util.Arrays;

public class BeerMath {
    
    
    private BeerMath() {}
    
    // peso in grammi
    static public int calcolaGU(double PPG, int peso) {
        double res = (PPG - 1)*1000*peso/453.6;
        return (int) Math.round(res);
    }
    
    // volume in litri e rendimento in %
    static public double calcolaOG(int sumGU, double rendimento, double volume) {
        double OG = Math.round(sumGU*rendimento*0.04546/volume);
        return OG/1000 + 1;
    }
    
    static public double calcolaFG(double OG, double attenuazione) {
        
        double FG = Math.round((OG - 1) * (1 - attenuazione/100) * 1000);
        return FG/1000 + 1; 
    }
}
