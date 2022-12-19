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
import org.apache.ibatis.jdbc.ScriptRunner;

/**
 *
 * @author Utente
 */
public class DatabaseConnector {
    
    private final Connection connection;
    
    public DatabaseConnector(String dbName) {
        Connection temp_co;
        try {
            Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root","");
            temp_co = co;
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            temp_co = null;
        }
        connection = temp_co;
    }
    
    public void createTables() {
        try {
            ScriptRunner sr = new ScriptRunner(connection);
            BufferedReader reader = new BufferedReader(new FileReader(getClass().getResource("/mysql/tables.sql").getPath()));
            sr.runScript(reader);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}