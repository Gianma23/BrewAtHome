/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome.controllers;

import it.unipi.brewathome.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class ModificaRicettaController implements Initializable {

    @FXML private GridPane grid;
    @FXML private FlowPane flow;
    @FXML private ScrollPane scroll;
    private int ricettaId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {   
            FXMLLoader loadTopBar = new FXMLLoader(App.class.getResource("topBar.fxml"));
            Parent topBar = loadTopBar.load();
            grid.add(topBar, 0, 0, 25, 1);
            FXMLLoader loadLeftBar = new FXMLLoader(App.class.getResource("leftBar.fxml"));
            Parent leftBar = loadLeftBar.load();
            grid.add(leftBar, 0, 1, 1, 1);
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }
}
