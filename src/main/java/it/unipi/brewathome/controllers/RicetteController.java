/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.brewathome.App;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class RicetteController implements Initializable {

    @FXML private GridPane grid;
    @FXML private FlowPane flow;
    @FXML private ScrollPane scroll;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        try {   
            App.setCanResize(true);
            
            FXMLLoader loadTopBar = new FXMLLoader(App.class.getResource("topBar.fxml"));
            Parent topBar = loadTopBar.load();
            grid.add(topBar, 0, 0, 25, 1);
            FXMLLoader loadLeftBar = new FXMLLoader(App.class.getResource("leftBar.fxml"));
            Parent leftBar = loadLeftBar.load();
            grid.add(leftBar, 0, 1, 1, 1);
            
            flow.setVgap(24);
            flow.setHgap(24);
            flow.setAlignment(Pos.CENTER_LEFT);

            caricaRicette();
            
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }    
    
    private void caricaRicette() throws IOException {
        String url = App.BASE_URL + "/recipes/all";

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        String urlParameters = "token=" + App.getToken();
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
        
        System.err.println(response.toString());
        
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(response.toString(), JsonElement.class);
        JsonArray recipes = json.getAsJsonArray();
        
        for(JsonElement recipe : recipes) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("card_ricetta.fxml"));
                Parent card = fxmlLoader.load();
                
                JsonObject recipeObj = recipe.getAsJsonObject();
                
                String nomeText = recipeObj.get("nome").getAsString();
                Text nome = (Text) card.lookup(".nome");
                nome.setText(nomeText);
                        
                flow.getChildren().add(card);
        }
    }
    
    public Rectangle generateRectangle() {

        Rectangle rect2 = new Rectangle(10, 10, 10, 10);
        rect2.setId("app");
        rect2.setArcHeight(8);
        rect2.setArcWidth(8);
        rect2.setStrokeWidth(1);
        rect2.setStroke(Color.WHITE);
        rect2.setWidth(320);
        rect2.setHeight(180);

        return rect2;
    }
    
}
