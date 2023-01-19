/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import enums.Tipo;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.responses.FermentabileResponse;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.responses.HttpResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class ModificaRicettaController implements Initializable {

    private static int ricettaId;
    private ObservableList<FermentabileResponse> fermentabili;
    
    @FXML private GridPane grid;
    @FXML private ScrollPane scroll;
    @FXML private TableView tableFermentabili;
    @FXML private TableColumn columnPeso;
    @FXML private TableColumn columnNome;
    @FXML private TableColumn columnColore;
    @FXML private TableColumn columnCategoria;
    @FXML private Text textNomeRicetta;
    @FXML private TextField fieldNomeRicetta;
    @FXML private TextField fieldAutore;
    @FXML private ChoiceBox fieldTipo;
    
      
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
            
            columnPeso.setCellValueFactory(new PropertyValueFactory<>("quantita"));
            columnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            columnColore.setCellValueFactory(new PropertyValueFactory<>("colore"));
            columnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

            fermentabili = FXCollections.observableArrayList();
            tableFermentabili.setItems(fermentabili);
            
            caricaFermentabili();
            caricaLuppoli();
                    
            // colonna informazioni laterale
            HttpConnector.getRequestWithToken("/recipes/info", "recipe=" + ricettaId, App.getToken());
            fieldTipo.getItems().setAll(Arrays.asList(Tipo.values()));

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
  
    @FXML
    private void aggiungiFermentabile() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("aggiungi_fermentabile.fxml"));
        Scene scene = new Scene(loader.load(), 551, 624);
        //salvo per poi aggiornare la tabella
        FermentabileController.setRicettaController(this);
        
        App.addStyle(scene, "style.css");
        App.addStyle(scene, "fonts.css");
        App.addStyle(scene, "global.css");
        
        stage.setScene(scene);
        stage.setTitle("Fermentabile");
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    private void aggiornaNome() {
        String nome = fieldNomeRicetta.getText();
        if(nome.equals(""))
            textNomeRicetta.setText("Nome ricetta");
        else
            textNomeRicetta.setText(nome);
    }
    
    @FXML
    private void eliminaRicetta() {
        
    }
    
    @FXML
    private void salvaRicetta() {
        
    }
    
    @FXML
    private void esci() {
        
    }
    
    public void caricaFermentabili() throws IOException {
        //svuoto la tabella
        tableFermentabili.getItems().clear();
        
        HttpResponse response = HttpConnector.getRequestWithToken("/recipes/fermentables", "recipe=" + ricettaId, App.getToken());
        String responseBody = response.getResponseBody();

        Gson gson = new Gson();
        JsonArray ricette = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
        for(JsonElement ricetta : ricette) {
            JsonObject ricettaObj = ricetta.getAsJsonObject();
            System.out.println(ricettaObj);
            FermentabileResponse fermentabileTable = new FermentabileResponse(ricettaObj.get("nome").getAsString(),
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
    
    public static int getRicettaId() {
        return ModificaRicettaController.ricettaId;
    }
}
