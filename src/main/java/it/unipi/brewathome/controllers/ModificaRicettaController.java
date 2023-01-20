package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.brewathome.enums.Tipo;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.responses.FermentabileResponse;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.FermentabileRequest;
import it.unipi.brewathome.connection.requests.LuppoloRequest;
import it.unipi.brewathome.connection.requests.RicettaRequest;
import it.unipi.brewathome.connection.responses.HttpResponse;
import it.unipi.brewathome.connection.responses.LuppoloResponse;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
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
    //TODO: accorpare response e request
    private ObservableList<FermentabileResponse> fermentabili;
    private List<FermentabileRequest> fermentabiliList;
    private ObservableList<LuppoloResponse> luppoli;
    private List<LuppoloRequest> luppoliList;
    
    @FXML private GridPane grid;
    @FXML private ScrollPane scroll;
    @FXML private TableView tableFermentabili;
    @FXML private TableView tableLuppoli;
    @FXML private TableColumn columnPesoFermentabile;
    @FXML private TableColumn columnNomeFermentabile;
    @FXML private TableColumn columnColore;
    @FXML private TableColumn columnCategoria;
    @FXML private TableColumn columnPesoLuppolo;
    @FXML private TableColumn columnNomeLuppolo;
    @FXML private TableColumn columnAlpha;
    @FXML private TableColumn columnTempo;
    @FXML private Text textNomeRicetta;
    @FXML private Text textUltimaModifica;
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
            
            columnPesoFermentabile.setCellValueFactory(new PropertyValueFactory<>("quantita"));
            columnNomeFermentabile.setCellValueFactory(new PropertyValueFactory<>("nome"));
            columnColore.setCellValueFactory(new PropertyValueFactory<>("colore"));
            columnCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

            columnPesoLuppolo.setCellValueFactory(new PropertyValueFactory<>("quantita"));
            columnNomeLuppolo.setCellValueFactory(new PropertyValueFactory<>("nome"));
            columnAlpha.setCellValueFactory(new PropertyValueFactory<>("alpha"));
            columnTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
            
            fermentabili = FXCollections.observableArrayList();
            tableFermentabili.setItems(fermentabili);
            fermentabiliList = new ArrayList(); 
            
            luppoli = FXCollections.observableArrayList();
            tableLuppoli.setItems(luppoli);
            luppoliList = new ArrayList();
            
            caricaFermentabili();
            caricaLuppoli();
                    
            // colonna informazioni laterale
            caricaBarraLaterale();
            
            // sistemo dimensioni delle tabelle
            setDimensionTableFermentabili();
            setDimensionTableLuppoli();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
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
    private void aggiungiLuppolo() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("aggiungi_luppolo.fxml"));
        Scene scene = new Scene(loader.load(), 551, 624);
        //salvo per poi aggiornare la tabella
        LuppoloController.setRicettaController(this);
        
        App.addStyle(scene, "style.css");
        App.addStyle(scene, "fonts.css");
        App.addStyle(scene, "global.css");
        
        stage.setScene(scene);
        stage.setTitle("Luppolo");
        stage.setResizable(false);
        stage.show();
    }
    
    @FXML
    private void modificaFermentabile() throws IOException {
        int index = fermentabili.indexOf(tableFermentabili.getSelectionModel().getSelectedItem());
        FermentabileRequest fermentabile = fermentabiliList.get(index);
        FermentabileController.setUpdateFermentabile(fermentabile);
        aggiungiFermentabile();
    }
    
    @FXML
    private void modificaLuppolo() throws IOException {
        int index = luppoli.indexOf(tableLuppoli.getSelectionModel().getSelectedItem());
        LuppoloRequest luppolo = luppoliList.get(index);
        LuppoloController.setUpdateLuppolo(luppolo);
        aggiungiLuppolo();
    }
    
    @FXML
    private void cambiaStile() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("cambia_stile.fxml"));
        Scene scene = new Scene(loader.load(), 551, 624);
        //salvo per poi aggiornare la tabella
        LuppoloController.setRicettaController(this);
        
        App.addStyle(scene, "style.css");
        App.addStyle(scene, "fonts.css");
        App.addStyle(scene, "global.css");
        
        stage.setScene(scene);
        stage.setTitle("Stile");
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
        try {
            HttpConnector.deleteRequestWithToken("/recipes/remove", "recipe=" + ricettaId, App.getToken());
            App.setRoot("ricette");
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } 
    }
    
    @FXML
    private void salvaRicetta() {
        try {
            //TODO: controlli input
            System.out.println(fieldTipo.getSelectionModel().getSelectedItem());
            RicettaRequest request = new RicettaRequest(ricettaId,
                                                        fieldNomeRicetta.getText(),
                                                        fieldAutore.getText(),
                                                        fieldTipo.getSelectionModel().getSelectedItem().toString(),
                                                        0,
                                                        "test",
                                                        0,0,0,0,0);                                                   
            Gson gson = new Gson();
            String body = gson.toJson(request);
            
            HttpConnector.postRequestWithToken("/recipes/update", body, App.getToken());
            App.setRoot("ricette");
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } 
    }
    
    @FXML
    private void esci() throws IOException {
        App.setRoot("ricette");
    }
    
    public void caricaFermentabili() throws IOException {
        //svuoto la tabella e la lista
        tableFermentabili.getItems().clear();
        fermentabiliList.clear();
        
        HttpResponse response = HttpConnector.getRequestWithToken("/fermentables/all", "recipe=" + ricettaId, App.getToken());
        String responseBody = response.getResponseBody();
        
        if(responseBody.equals(""))
            return;
            
        Gson gson = new Gson();
        JsonArray fermentabiliArray = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
        for(JsonElement fermentabile : fermentabiliArray) {
            FermentabileResponse fermentabileTable = gson.fromJson(fermentabile, FermentabileResponse.class);
            fermentabili.add(fermentabileTable);
            FermentabileRequest ferm = gson.fromJson(fermentabile, FermentabileRequest.class);
            fermentabiliList.add(ferm);
        }
    }
    
    public void caricaLuppoli() throws IOException {
        //svuoto la tabella
        tableLuppoli.getItems().clear();
        
        HttpResponse response = HttpConnector.getRequestWithToken("/hops/all", "recipe=" + ricettaId, App.getToken());
        String responseBody = response.getResponseBody();
        
        if(responseBody.equals(""))
            return;
            
        Gson gson = new Gson();
        JsonArray luppoliArray = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
        for(JsonElement luppolo : luppoliArray) {
            LuppoloResponse luppoloTable = gson.fromJson(luppolo, LuppoloResponse.class);
            luppoli.add(luppoloTable);
            LuppoloRequest lup = gson.fromJson(luppolo, LuppoloRequest.class);
            luppoliList.add(lup);
        }
    }
    
    public void caricaBarraLaterale() throws IOException {
        
        // riempimento dropdown menu
        
        fieldTipo.getItems().setAll(Arrays.asList(Tipo.values()));
        
        // riempio i field con le info già esistenti 
        
        HttpResponse response = HttpConnector.getRequestWithToken("/recipes/info", "recipe=" + ricettaId, App.getToken());
        String responseBody = response.getResponseBody();
        
        Gson gson = new Gson();
        JsonObject recipeObj = gson.fromJson(responseBody, JsonElement.class).getAsJsonObject();
        
        String nome = recipeObj.get("nome").getAsString();
        textNomeRicetta.setText(nome);
        fieldNomeRicetta.setText(nome);
        
        if(!recipeObj.get("autore").isJsonNull())
            fieldAutore.setText(recipeObj.get("autore").getAsString());
        
        String tipo = recipeObj.get("tipo").getAsString();
        System.out.println(recipeObj);
        fieldTipo.getSelectionModel().select(Tipo.indexOf(tipo));
        
        String ultimaModifica = recipeObj.get("ultimaModifica").getAsString();
        LocalDate data = LocalDate.parse(ultimaModifica, DateTimeFormatter.ISO_DATE_TIME);
        textUltimaModifica.setText("Ultima modifica: " + data.getDayOfMonth() + " " + data.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALY) + " " + data.getYear());
    }
    
    public void setDimensionTableFermentabili() {
        // setto la larghezza delle colonne
        columnPesoFermentabile.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.2));
        columnNomeFermentabile.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.4));
        columnColore.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.1));
        columnCategoria.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.28));
        columnPesoFermentabile.setReorderable(false);
        columnNomeFermentabile.setReorderable(false);
        columnColore.setReorderable(false);
        columnCategoria.setReorderable(false);
        
        // sistemo altezza tabella
        tableFermentabili.setFixedCellSize(25);
        tableFermentabili.prefHeightProperty().bind(tableFermentabili.fixedCellSizeProperty().multiply(Bindings.size(tableFermentabili.getItems()).add(1.01)));
    }
    
        public void setDimensionTableLuppoli() {
        // setto la larghezza delle colonne
        columnPesoLuppolo.prefWidthProperty().bind(tableLuppoli.widthProperty().multiply(0.2));
        columnNomeLuppolo.prefWidthProperty().bind(tableLuppoli.widthProperty().multiply(0.48));
        columnAlpha.prefWidthProperty().bind(tableLuppoli.widthProperty().multiply(0.15));
        columnTempo.prefWidthProperty().bind(tableLuppoli.widthProperty().multiply(0.15));
        columnPesoLuppolo.setReorderable(false);
        columnNomeLuppolo.setReorderable(false);
        columnAlpha.setReorderable(false);
        columnTempo.setReorderable(false);
        
        // sistemo altezza tabella
        tableLuppoli.setFixedCellSize(25);
        tableLuppoli.prefHeightProperty().bind(tableLuppoli.fixedCellSizeProperty().multiply(Bindings.size(tableLuppoli.getItems()).add(1.01)));
    }
    
    public static void setRicettaId(int ricettaId) {
        ModificaRicettaController.ricettaId = ricettaId;
    }
    
    public static int getRicettaId() {
        return ModificaRicettaController.ricettaId;
    }
}
