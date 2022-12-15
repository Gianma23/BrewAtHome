/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome;

import java.io.IOException;
import java.net.URLEncoder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class AccediController {

    @FXML private Button buttonAccedi;
    
    @FXML 
    private void login() throws IOException {

        App.setRoot("ricette");
    }
    
    @FXML
    private void openRegistrati() throws IOException {
        App.setRoot("registrati");
    }
}
