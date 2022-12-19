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
        
        String url = App.BASE_URL + "/auth/login";

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        String urlParameters = "email=" + email.getText() + "&password=" + password.getText();
        httpClient.setRequestMethod("POST");

        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
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
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null) 
            response.append(currentLine);

        in.close();
        
        message.setText("");
        
        if (200 <= responseCode && responseCode <= 299) {
            App.setToken(response.toString());
            App.setRoot("ricette");
            System.out.println(App.getToken());
        }
        else 
            message.setText(response.toString());
    }
    
    @FXML
    private void openRegistrati() throws IOException {
        App.setRoot("registrati");
    }
}
