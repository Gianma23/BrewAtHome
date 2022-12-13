/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class RegistratiController {


    @FXML private Button buttonAccedi;
    
    @FXML 
    private void register() throws IOException {
        App.setRoot("accedi");
    }
    
    @FXML
    private void openAccedi() throws IOException {
        App.setRoot("accedi");
    }
}
