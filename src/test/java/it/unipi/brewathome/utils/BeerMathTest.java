package it.unipi.brewathome.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BeerMathTest {
    
    public BeerMathTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of calcolaGU method, of class BeerMath.
     */
    @Test
    public void testCalcolaGU() {
        System.out.println("calcolaGU");
        double PPG = 1.034;
        int peso = 1500;
        int expResult = 112;
        int result = BeerMath.CalcolaGU(PPG, peso);
        assertEquals(expResult, result);
    }

    /**
     * Test of calcolaOG method, of class BeerMath.
     */
    @Test
    public void testCalcolaOG() {
        System.out.println("calcolaOG");
        int arrayGU = 310;
        double rendimento = 71.9;
        double volume = 23;
        double expResult = 1.044;
        double result = BeerMath.CalcolaOG(arrayGU, rendimento, volume);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of calcolaFG method, of class BeerMath.
     */
    @Test
    public void testCalcolaFG() {
        System.out.println("calcolaFG");
        double OG = 1.044;
        double attenuazione = 75;
        double expResult = 1.011;
        double result = BeerMath.CalcolaFG(OG, attenuazione);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of calcolaABV method, of class BeerMath.
     */
    @Test
    public void testCalcolaABV() {
        System.out.println("calcolaABV");
        double OG = 1.044;
        double FG = 1.011;
        double expResult = 4.3;
        double result = BeerMath.CalcolaABV(OG, FG);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of calcolaEBC method, of class BeerMath.
     */
    @Test
    public void test1CalcolaEBC() {
        System.out.println("calcolaEBC");
        int[] EBC = {10,8,5,290,1300};
        int[] pesi = {1500,1000,1000,250,100};
        double volume = 23.0;
        double expResult = 37;
        double result = BeerMath.CalcolaEBC(EBC, pesi, volume);
        assertEquals(expResult, result, 1);
    }
}
