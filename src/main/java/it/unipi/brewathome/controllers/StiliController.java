package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.responses.HttpResponse;
import it.unipi.brewathome.connection.responses.StileResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class StiliController implements Initializable{
    
    private static final Logger logger =LogManager.getLogger(StiliController.class);
    private static ModificaRicettaController ricettaController;
    private ObservableList<String> stili;
    private List<StileResponse> stiliList;
    
    @FXML private ListView listaStili;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        stili = FXCollections.observableArrayList();
        listaStili.setItems(stili);
        stiliList = new ArrayList();
        
        try {
            HttpResponse response = HttpConnector.getRequestWithToken("/categories/all", "", App.getToken());
            String responseBody = response.getResponseBody();
        
            if(responseBody.equals(""))
                return;

            Gson gson = new Gson();
            JsonArray stiliArray = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
            for(JsonElement fermentabile : stiliArray) {
                StileResponse stileElem = gson.fromJson(fermentabile, StileResponse.class);
                stiliList.add(stileElem);
                stili.add(stileElem.getNome());
            }
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }
    
    @FXML
    private void seleziona(MouseEvent event) {
        if(event.getClickCount()==2)
            ricettaController.setTextStile(listaStili.getSelectionModel().getSelectedItem().toString());
    }
    
    public static void setRicettaController(ModificaRicettaController ricettaController) {
        StiliController.ricettaController = ricettaController;
    }
}