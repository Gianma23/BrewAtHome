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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class AccediController {
    
    private static final Logger logger =LogManager.getLogger(AccediController.class);
    @FXML private VBox inputsContainer;
    @FXML private Button buttonAccedi;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private Text message;
    
    @FXML 
    private void login() {
        
        Gson gson = new Gson();
        AuthRequest request = new AuthRequest(email.getText(), password.getText());
        
        try {
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
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }  
    }
    
    @FXML
    private void openRegistrati() {
        try {
            App.setRoot("registrati");
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }      
    }
}
