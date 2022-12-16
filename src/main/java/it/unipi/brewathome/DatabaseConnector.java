/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.brewathome;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
            Statement st = connection.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS `account` (" +
                             "    email VARCHAR(45) NOT NULL," +
                             "    password VARCHAR(128) NOT NULL," +
                             "    PRIMARY KEY(email)" +
                             ")ENGINE = InnoDB DEFAULT CHARSET = latin1;");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}