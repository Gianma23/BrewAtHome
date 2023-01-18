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

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class RegistratiController {

    @FXML private Button buttonAccedi;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private TextField confirmPassword;
    
    @FXML 
    private void register() throws IOException {
        
        if(!password.getText().equals(confirmPassword.getText())) {
            //TODO set error message
            return;
        }
                
        Gson gson = new Gson();
        AuthRequest request = new AuthRequest(email.getText(), password.getText());
        
        HttpResponse response = HttpConnector.postRequest("/auth/register", "request="+gson.toJson(request));
        String responseBody = response.getResponseBody();
        int responseCode = response.getResponseCode();
        
        if (200 <= responseCode && responseCode <= 299)
            App.setRoot("accedi");
    }
    
    @FXML
    private void openAccedi() throws IOException {
        App.setRoot("accedi");
    }
}
