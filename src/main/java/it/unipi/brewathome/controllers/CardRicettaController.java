/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome.controllers;

import it.unipi.brewathome.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class CardRicettaController {

    @FXML private VBox cardBox;
    
    @FXML
    private void modificaRicetta() throws IOException {
        
        int ricettaId = Integer.parseInt(cardBox.getId());
        
        FXMLLoader loader = new FXMLLoader(App.class.getResource("modifica_ricetta.fxml"));
        ModificaRicettaController.setRicettaId(ricettaId); 
        cardBox.getScene().setRoot(loader.load());
    }
}