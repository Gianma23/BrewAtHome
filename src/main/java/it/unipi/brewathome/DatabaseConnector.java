/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.brewathome;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.ibatis.jdbc.ScriptRunner;

/**
 *
 * @author Utente
 */
public class DatabaseConnector {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_NAME = "gianmaria_saggini";
    private static boolean isPopulated = false;
    
    
    private DatabaseConnector() {
    }
    
    
    public static void createTables() {
        try (Connection co = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            BufferedReader reader = new BufferedReader(new FileReader(DatabaseConnector.class.getResource("/mysql/tables.sql").getPath()));) {
            
            ScriptRunner sr = new ScriptRunner(co);
            sr.runScript(reader);
        }
        catch (IOException | SQLException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public static void populateTables() {
        if(isPopulated)
            return;
        
        isPopulated = true;
    }
}