package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.RicettaRequest;
import it.unipi.brewathome.connection.responses.HttpResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
            App.addBars(grid);

            caricaRicette();        
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }    
    
    @FXML
    private void creaRicetta() { 
        try {
            HttpResponse response = HttpConnector.postRequestWithToken("/recipes/add", "", App.getToken());
            int ricettaId = Integer.parseInt(response.getResponseBody());

            FXMLLoader loader = new FXMLLoader(App.class.getResource("modifica_ricetta.fxml"));
            ModificaRicettaController.setRicettaId(ricettaId); 
            grid.getScene().setRoot(loader.load());
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
        
    }
    
    private void caricaRicette() throws IOException {
        
        HttpResponse response = HttpConnector.getRequestWithToken("/recipes/all", "", App.getToken());
        String responseBody = response.getResponseBody();
        
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(responseBody, JsonElement.class);
        JsonArray recipes = json.getAsJsonArray();
        
        for(JsonElement recipe : recipes) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("card_ricetta.fxml"));
            VBox card = (VBox) fxmlLoader.load();

            RicettaRequest ricetta = gson.fromJson(recipe, RicettaRequest.class);

            String nomeText = ricetta.getNome();
            Text nome = (Text) card.lookup(".nome");
            nome.setText(nomeText);

            String stileText = ricetta.getStileId();
            Text stile = (Text) card.lookup(".stile");
            stile.setText(stileText);

            //TODO: decidere se tenere i parametri
            //String abv = recipeObj.get("abv").getAsString();
            //String og = recipeObj.get("og").getAsString();
            //String fg = recipeObj.get("fg").getAsString();      
            //String ibu = recipeObj.get("ibu").getAsString();    
            String descText = ricetta.getDescrizione();
            Text descrizione = (Text) card.lookup(".descrizione");
            descrizione.setText(descText);
            
            String ricettaId = String.valueOf(ricetta.getId());
            card.setId(ricettaId);
            
            flow.getChildren().add(card);
        }
    }
}
