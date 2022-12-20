/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome.controllers;

import it.unipi.brewathome.App;
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
                
        String url = App.BASE_URL + "/auth/register";

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        String urlParameters = "email=" + email.getText() + "&password=" + password.getText();
        //add reuqest header
        httpClient.setRequestMethod("POST");

        // Send post request
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }

        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        
        if (200 <= responseCode && responseCode <= 299)
            App.setRoot("accedi");
    }
    
    @FXML
    private void openAccedi() throws IOException {
        App.setRoot("accedi");
    }
}
