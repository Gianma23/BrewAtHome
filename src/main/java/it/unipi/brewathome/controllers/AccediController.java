/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome.controllers;

import it.unipi.brewathome.App;
import it.unipi.brewathome.http.HttpConnector;
import it.unipi.brewathome.http.HttpResponse;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
    private void login() throws IOException, InterruptedException {
        
        HttpResponse response = HttpConnector.postRequest("/auth/login", "email=" + email.getText() + "&password=" + password.getText());
        String responseBody = response.getResponseBody();
        int responseCode = response.getResponseCode();
        
        message.setText("");
        
        if (200 <= responseCode && responseCode <= 299) {
            App.setToken(responseBody);
            App.setRoot("ricette");
        }
        else 
            message.setText(responseBody);
    }
    
    @FXML
    private void openRegistrati() throws IOException {
        App.setRoot("registrati");
    }
}
