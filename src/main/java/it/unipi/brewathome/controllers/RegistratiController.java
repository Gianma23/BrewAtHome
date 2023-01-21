/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.requests.AuthRequest;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.responses.HttpResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class RegistratiController {
    
    private static final Logger logger =LogManager.getLogger(RegistratiController.class.getName());
    @FXML private Button buttonAccedi;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private TextField confirmPassword;
    
    @FXML 
    private void register() {
        
        if(!password.getText().equals(confirmPassword.getText())) {
            //TODO set error message
            return;
        }
                
        Gson gson = new Gson();
        AuthRequest request = new AuthRequest(email.getText(), password.getText());
        try {
            HttpResponse response = HttpConnector.postRequest("/auth/register", gson.toJson(request));
            String responseBody = response.getResponseBody();
            int responseCode = response.getResponseCode();

            if (200 <= responseCode && responseCode <= 299)
                App.setRoot("accedi");
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }  
    }
    
    @FXML
    private void openAccedi() {
        try {
            App.setRoot("accedi");  
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }  
    }
}
