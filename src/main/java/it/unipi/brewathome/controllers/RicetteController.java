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
import it.unipi.brewathome.http.HttpConnector;
import it.unipi.brewathome.http.HttpResponse;
import java.io.IOException;
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
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class RicetteController implements Initializable {

    private static final Logger logger =LogManager.getLogger(RicetteController.class);
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
        
        HttpResponse response = HttpConnector.getRequestWithToken("/recipes/all", "", App.getToken());
        String responseBody = response.getResponseBody();
        
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(responseBody, JsonElement.class);
        JsonArray recipes = json.getAsJsonArray();
        
        for(JsonElement recipe : recipes) {
            System.out.println(recipe);
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("card_ricetta.fxml"));
            Parent card = fxmlLoader.load();

            JsonObject recipeObj = recipe.getAsJsonObject();

            String nomeText = recipeObj.get("nome").getAsString();
            Text nome = (Text) card.lookup(".nome");
            nome.setText(nomeText);

            if(!recipeObj.get("stileId").isJsonNull()) {
                String stileText = recipeObj.get("stileId").getAsString();
                Text stile = (Text) card.lookup(".stile");
                stile.setText(stileText);
            }

            String abv = recipeObj.get("abv").getAsString();
            String og = recipeObj.get("og").getAsString();
            String fg = recipeObj.get("fg").getAsString();      
            String ibu = recipeObj.get("ibu").getAsString();    
            Text stile = (Text) card.lookup(".parametri");
            stile.setText("ABV: " + abv + "%, OG: " + og + ", FG: " + fg + ", IBU: " + ibu);

            flow.getChildren().add(card);
        }
    }
    
    @FXML
    private void creaRicetta() throws IOException { 
        
        HttpResponse response = HttpConnector.putRequestWithToken("/recipes/add", "recipe={\"ciao\":\"ciao\"}", App.getToken());

        App.setRoot("modifica_ricetta");
    }
}
