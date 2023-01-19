/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.requests.AuthRequest;
import it.unipi.brewathome.connection.requests.AuthRequest;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.responses.HttpResponse;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class AccediController {

    @FXML private VBox inputsContainer;
    @FXML private Button buttonAccedi;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private Text message;
    
    @FXML 
    private void login() throws IOException {
        
        Gson gson = new Gson();
        AuthRequest request = new AuthRequest(email.getText(), password.getText());
        
        HttpResponse response = HttpConnector.postRequest("/auth/login", gson.toJson(request));
        String responseHeader = response.getResponseHeader();
        String responseBody = response.getResponseBody();
        int responseCode = response.getResponseCode();
        
        message.setText("");
        
        if (200 <= responseCode && responseCode <= 299) {        
            Stage stage = (Stage) email.getScene().getWindow();
            
            App.setToken(responseHeader);
            App.setRoot("ricette");
            
            stage.setWidth(1480);
            stage.setHeight(900);
            stage.centerOnScreen();
        }
        else 
            message.setText(responseBody);
    }
    
    @FXML
    private void openRegistrati() throws IOException {
        App.setRoot("registrati");
    }
}
