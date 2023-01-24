package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.responses.HttpResponse;
import it.unipi.brewathome.connection.responses.Stile;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StiliController implements Initializable{
    
    private static final Logger logger =LogManager.getLogger(StiliController.class);
    private static ModificaRicettaController ricettaController;
    private ObservableList<Stile> stili;
    private List<Stile> copiaStili;
    
    @FXML private ListView listaStili;
    @FXML private TextField barraRicerca;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
        listaStili.setCellFactory(param -> new ListCell<Stile>() {
            @Override
            protected void updateItem(Stile item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getNome() == null) {
                    setText(null);
                } else {
                    setText(item.getNome());
                }
            }
        });
        stili = FXCollections.observableArrayList();
        listaStili.setItems(stili);
        copiaStili = new ArrayList();
        
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    HttpResponse response = HttpConnector.getRequest("/categories/all", "");
                    String responseBody = response.getResponseBody();

                    if(responseBody.equals(""))
                        return null;

                    Gson gson = new Gson();
                    JsonArray stiliArray = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
                    
                    Platform.runLater(() -> {
                        for(JsonElement fermentabile : stiliArray) {
                            Stile stileElem = gson.fromJson(fermentabile, Stile.class);
                            copiaStili.add(stileElem);
                            stili.add(stileElem);
                        }
                    });
                }
                catch (IOException ioe) {
                    logger.error(ioe.getMessage());
                }
                return null;
            }
        };
        new Thread(task).start();
    }
    
    @FXML
    private void seleziona(MouseEvent event) {
        if(event.getClickCount()==2) {
            Stile stile = (Stile) listaStili.getSelectionModel().getSelectedItem();
            ricettaController.setStile(stile);
            ricettaController.aggiornaStats();
        }
    }
    
    @FXML
    private void cerca() {
        String nome = barraRicerca.getText();
        ObservableList<Stile> nuovaLista = FXCollections.observableArrayList();
        for (Stile stile : copiaStili){
            if(stile.getNome().toLowerCase().contains(nome.toLowerCase())) 
                nuovaLista.add(stile);
        }
        stili.setAll(nuovaLista);
    }
    
    public static void setRicettaController(ModificaRicettaController ricettaController) {
        StiliController.ricettaController = ricettaController;
    }
}
