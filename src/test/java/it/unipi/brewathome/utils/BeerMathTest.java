/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
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
        int[] arrayGU = {112,97,77,18,6};
        double rendimento = 71.9;
        double volume = 23;
        double expResult = 1.044;
        double result = BeerMath.calcolaOG(arrayGU, rendimento, volume);
        assertEquals(expResult, result);
    }
    
}
