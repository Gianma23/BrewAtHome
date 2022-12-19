/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
        String url = "http://localhost:8080/auth/login";

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        String urlParameters = "email=pippo&password=password12";
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
            InputStream inputStream;
    if (200 <= responseCode && responseCode <= 299) {
        inputStream = httpClient.getInputStream();
    } else {
        inputStream = httpClient.getErrorStream();
    }
        BufferedReader in = new BufferedReader(
        new InputStreamReader(
            inputStream));

    StringBuilder response = new StringBuilder();
    String currentLine;

    while ((currentLine = in.readLine()) != null) 
        response.append(currentLine);

    in.close();

    System.out.println(response.toString());
       
        App.setRoot("ricette");
    }
    
    @FXML
    private void openRegistrati() throws IOException {
        App.setRoot("registrati");
    }
}
