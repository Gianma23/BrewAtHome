package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.data.Ricetta;
import it.unipi.brewathome.connection.data.HttpResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RicetteController implements Initializable {

    private static final Logger logger = LogManager.getLogger(RicetteController.class);
    @FXML private GridPane grid;
    @FXML private FlowPane flow;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        try {
            App.setCanResize(true);
            App.addLeftBar(grid);

            caricaRicette();        
        }
        catch (IOException ioe) {
            logger.error(ioe);
        }
    }    
    
    @FXML
    private void creaRicetta() { 
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    HttpResponse response = HttpConnector.getRequestWithToken("/recipes/add", "", App.getToken());
                    Gson gson = new Gson();
                    Ricetta ricetta = gson.fromJson(response.getResponseBody(), Ricetta.class);
                    
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(App.class.getResource("modifica_ricetta.fxml"));
                            ModificaRicettaController controller = new ModificaRicettaController();
                            controller.setRicetta(ricetta); 
                            loader.setController(controller);
                            grid.getScene().setRoot(loader.load());
                        }
                        catch (IOException ioe) {
                            throw new UncheckedIOException(ioe);
                        }
                    });
                }
                catch (IOException ioe) {
                    logger.error(ioe);
                }  
                return null;
            }
        };
        new Thread(task).start();
    }
    
    private void caricaRicette() throws IOException {
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    HttpResponse response = HttpConnector.getRequestWithToken("/recipes/all", "", App.getToken());
                    String responseBody = response.getResponseBody();

                    Gson gson = new Gson();
                    JsonElement json = gson.fromJson(responseBody, JsonElement.class);
                    JsonArray recipes = json.getAsJsonArray();

                    Platform.runLater(() -> {
                        for(JsonElement recipe : recipes) {
                            try {
                                Ricetta ricetta = gson.fromJson(recipe, Ricetta.class);
                                
                                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("card_ricetta.fxml"));
                                VBox card = (VBox) fxmlLoader.load();
                                CardRicettaController controller = (CardRicettaController) fxmlLoader.getController();
                                controller.setRicetta(ricetta);

                                String nomeText = ricetta.getNome();
                                Text nome = (Text) card.lookup(".nome");
                                nome.setText(nomeText);

                                String stileText = ricetta.getStileId();
                                Text stile = (Text) card.lookup(".stile");
                                stile.setText(stileText);

                                String descText = ricetta.getDescrizione();
                                if(descText != null && descText.length()>90)
                                    descText = descText.substring(0,90) + "...";
                                Text descrizione = (Text) card.lookup(".descrizione");
                                descrizione.setText(descText);

                                flow.getChildren().add(card);
                            }
                            catch (IOException ioe) {
                                throw new UncheckedIOException(ioe);
                            }
                        }
                    });
                }
                catch (IOException ioe) {
                    throw new UncheckedIOException(ioe);
                }
                return null;
            } 
        }; 
        new Thread(task).start();
    }
}