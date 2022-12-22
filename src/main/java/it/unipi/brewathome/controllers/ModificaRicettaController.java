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
import it.unipi.brewathome.FermentabileTable;
import it.unipi.brewathome.http.HttpConnector;
import it.unipi.brewathome.http.HttpResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class ModificaRicettaController implements Initializable {

    private static int ricettaId;
    private ObservableList<FermentabileTable> fermentabili;
    
    @FXML private GridPane grid;
    @FXML private ScrollPane scroll;
    @FXML private TableView tableFermentabili;
    @FXML private TableColumn columnPeso;
    @FXML private TableColumn columnNome;
    @FXML private TableColumn columnColore;
    @FXML private TableColumn columnCategoria;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {   
            FXMLLoader loadTopBar = new FXMLLoader(App.class.getResource("topBar.fxml"));
            Parent topBar = loadTopBar.load();
            grid.add(topBar, 0, 0, 25, 1);
            FXMLLoader loadLeftBar = new FXMLLoader(App.class.getResource("leftBar.fxml"));
            Parent leftBar = loadLeftBar.load();
            grid.add(leftBar, 0, 1, 1, 1);
            
            // carico i fermentabili e i luppoli nella tabella
            caricaFermentabili();
            caricaLuppoli();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        
        // setto la larghezza delle colonne
        columnPeso.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.2));
        columnNome.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.4));
        columnColore.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.1));
        columnCategoria.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.29));
        columnPeso.setReorderable(false);
        columnNome.setReorderable(false);
        columnColore.setReorderable(false);
        columnCategoria.setReorderable(false);
    }
  
    private void caricaFermentabili() throws IOException {
        // collego colonne con valori del JavaBean
        columnPeso.setCellValueFactory(new PropertyValueFactory<>("quantita"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        columnColore.setCellValueFactory(new PropertyValueFactory<>("colore"));
        columnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        fermentabili = FXCollections.observableArrayList();    
        tableFermentabili.setItems(fermentabili);
            
        
        HttpResponse response = HttpConnector.getRequestWithToken("/recipes/fermentables", "recipe=" + ricettaId, App.getToken());
        String responseBody = response.getResponseBody();

        Gson gson = new Gson();
        JsonArray ricette = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
        for(JsonElement ricetta : ricette) {
            JsonObject ricettaObj = ricetta.getAsJsonObject();
            System.out.println(ricettaObj);
            FermentabileTable fermentabileTable = new FermentabileTable(ricettaObj.get("nome").getAsString(),
                                                                        ricettaObj.get("quantita").getAsInt(),
                                                                        ricettaObj.get("colore").getAsInt(),
                                                                        ricettaObj.get("categoria").getAsString());
            System.out.println(fermentabileTable);
            fermentabili.add(fermentabileTable); 
        }
    }
    
    private void caricaLuppoli() throws IOException {
        
    }
    
    public static void setRicettaId(int ricettaId) {
        ModificaRicettaController.ricettaId = ricettaId;
    }
}
