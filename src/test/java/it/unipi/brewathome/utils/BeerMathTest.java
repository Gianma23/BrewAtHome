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
        int result = BeerMath.calcolaGU(PPG, peso);
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
        double result = BeerMath.calcolaOG(arrayGU, rendimento, volume);
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
        double result = BeerMath.calcolaFG(OG, attenuazione);
        assertEquals(expResult, result);
    }
}
