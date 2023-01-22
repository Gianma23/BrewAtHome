package it.unipi.brewathome.utils;

// classe statica, simile a java.lang.Math

import java.util.Arrays;

public class BeerMath {
    
    
    private BeerMath() {}
    
    // peso in grammi
    static public int CalcolaGU(double PPG, int peso) {
        double res = (PPG - 1)*1000*peso/453.6;
        return (int) Math.round(res);
    }
    
    // volume in litri e rendimento in %
    static public double CalcolaOG(int sumGU, double rendimento, double volume) {
        double OG = Math.round(sumGU*rendimento*0.04546/volume);
        return OG/1000 + 1;
    }
    
    static public double CalcolaFG(double OG, double attenuazione) {
        double FG = Math.round((OG - 1) * (1 - attenuazione/100) * 1000);
        return FG/1000 + 1; 
    }
    
    static public double CalcolaABV(double OG, double FG) {
        double ABV = Math.round((OG - FG)*10000/7.6);
        return ABV/10;
    }
    
    // volumne in litri
    static public double CalcolaEBC(int[] arrayEBC, int [] arrayPeso, double volume) {
        if(arrayEBC.length==0 || arrayPeso.length==0)
            return 0;
        if(arrayEBC.length!=arrayPeso.length || volume==0)
            return 0;
        
        double totMCUs = 0;
        for(int i = 0; i < arrayEBC.length; i++) {
            totMCUs += ((arrayEBC[i]*0.375+0.5611) * arrayPeso[i]/453.6);
        }
        double SRM = totMCUs/volume*4.546;
        return Math.round(1.49*Math.pow(SRM, 0.69)*1.97*10)/10.0;
    }
    
    static public double RagerIBU(double[] arrayAlpha, int[] arrayPeso, int[] arrayMinuti, double volume) {
        if(arrayAlpha.length==0 || arrayPeso.length==0 || arrayMinuti.length==0)
            return 0;
        if(arrayAlpha.length!=arrayPeso.length || volume==0)
            return 0;
        
        int tot = 0;
        for(int i = 0; i < arrayAlpha.length; i++) {
            double util = 18.11 + 13.86 * Math.tanh((arrayMinuti[i]-31.32) / 18.27);
            tot += Math.round(arrayAlpha[i]*arrayPeso[i]*util/(volume*10));
        }
        return tot;
    } 
}