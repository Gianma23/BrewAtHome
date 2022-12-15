/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome;

import java.io.DataOutputStream;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class RegistratiController {


    @FXML private Button buttonAccedi;
    
    @FXML 
    private void register() throws IOException {
        
                
        String url = "http://localhost:8080/account/add";

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        String urlParameters = "email=pippo&password=password123";
        //add reuqest header
        httpClient.setRequestMethod("POST");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpClient.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpClient.setRequestProperty("Content-Length", Integer.toString(urlParameters.length()));
        httpClient.setUseCaches(false);

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


        
        App.setRoot("accedi");
    }
    
    @FXML
    private void openAccedi() throws IOException {
        App.setRoot("accedi");
    }
}
