package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.unipi.brewathome.utils.Tipo;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.Fermentabile;
import it.unipi.brewathome.connection.requests.Luppolo;
import it.unipi.brewathome.connection.requests.RicettaRequest;
import it.unipi.brewathome.connection.responses.HttpResponse;
import it.unipi.brewathome.utils.BeerMath;
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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ModificaRicettaController implements Initializable {

    private static final Logger logger =LogManager.getLogger(ModificaRicettaController.class.getName());
    private static int ricettaId;
    private ObservableList<Fermentabile> fermentabili;
    private ObservableList<Luppolo> luppoli;
    private List<Luppolo> luppoliList;
    
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
    @FXML private Text textStile;
    @FXML private Text textAbv;
    @FXML private Text textOg;
    @FXML private Text textFg;
    
    @FXML private TextArea fieldDescrizione;
    @FXML private TextField fieldNomeRicetta;
    @FXML private TextField fieldAutore;
    @FXML private TextField fieldVolume;
    @FXML private TextField fieldRendimento;
    @FXML private ChoiceBox fieldTipo;
    
    @FXML private Rectangle barAbv;
    @FXML private Rectangle barOg;
    @FXML private Rectangle barFg;
    
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {   
            App.addBars(grid);
            
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

            luppoli = FXCollections.observableArrayList();
            tableLuppoli.setItems(luppoli);
            luppoliList = new ArrayList();
            
            // carico info della ricetta
            caricaInfoRicetta();

            // aggiungo listener su statistiche birra
            aggiornaStatsListener();
            
            caricaFermentabili();
            caricaLuppoli();
            
            // sistemo dimensioni delle tabelle
            setDimensionTableFermentabili();
            setDimensionTableLuppoli();
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }
  
    @FXML
    private void aggiungiFermentabile() {
        try {
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
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }
    
    @FXML
    private void aggiungiLuppolo() {
        try {
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
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }
    
    @FXML
    private void modificaFermentabile() {
        Fermentabile fermentabile = (Fermentabile) tableFermentabili.getSelectionModel().getSelectedItem();
        FermentabileController.setUpdateFermentabile(fermentabile);
        aggiungiFermentabile();
    }
    
    @FXML
    private void modificaLuppolo() {
        Luppolo luppolo = (Luppolo) tableLuppoli.getSelectionModel().getSelectedItem();
        LuppoloController.setUpdateLuppolo(luppolo);
        aggiungiLuppolo();
    }
    
    @FXML
    private void cambiaStile() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("cambia_stile.fxml"));
            Scene scene = new Scene(loader.load(), 551, 624);
            //salvo per poi aggiornare la tabella
            StiliController.setRicettaController(this);

            App.addStyle(scene, "style.css");
            App.addStyle(scene, "fonts.css");
            App.addStyle(scene, "global.css");

            stage.setScene(scene);
            stage.setTitle("Stile");
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
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
            logger.error(ioe.getMessage());
        } 
    }
    
    @FXML
    private void salvaRicetta() {
        try {
            //TODO: controlli input
            double volume = Double.valueOf(fieldVolume.getText());
            double rendimento = Double.valueOf(fieldRendimento.getText());
            
            RicettaRequest request = new RicettaRequest(ricettaId,
                                                        fieldNomeRicetta.getText(),
                                                        fieldDescrizione.getText(),
                                                        fieldAutore.getText(),
                                                        fieldTipo.getSelectionModel().getSelectedItem().toString(),
                                                        textStile.getText(),
                                                        volume,
                                                        rendimento);                                                   
            Gson gson = new Gson();
            String body = gson.toJson(request);
            
            HttpConnector.postRequestWithToken("/recipes/update", body, App.getToken());
            App.setRoot("ricette");
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } 
    }

    @FXML
    private void esci() throws IOException {
        App.setRoot("ricette");
    }
    
    /* =========== UTILITA =========== */
    
    public void caricaFermentabili() throws IOException {
        //svuoto la tabella e la lista
        tableFermentabili.getItems().clear();
        
        HttpResponse response = HttpConnector.getRequestWithToken("/fermentables/all", "recipe=" + ricettaId, App.getToken());
        String responseBody = response.getResponseBody();
        
        if(responseBody.equals(""))
            return;
            
        Gson gson = new Gson();
        JsonArray fermentabiliArray = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
        for(JsonElement fermentabile : fermentabiliArray) {
            Fermentabile fermentabileTable = gson.fromJson(fermentabile, Fermentabile.class);
            fermentabili.add(fermentabileTable);
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
            Luppolo luppoloTable = gson.fromJson(luppolo, Luppolo.class);
            luppoli.add(luppoloTable);
            Luppolo lup = gson.fromJson(luppolo, Luppolo.class);
            luppoliList.add(lup);
        }
    }
    
    private void aggiornaStatsListener() {      

        // listener cambio lista fermentabili
        fermentabili.addListener((ListChangeListener<Fermentabile>) (e) -> {
            
            // calcolo OG
            int sumGU = 0;
            for(Fermentabile fermentabile : fermentabili) {
                sumGU += BeerMath.calcolaGU(fermentabile.getPotenziale(), fermentabile.getQuantita());
            }
            double OG = BeerMath.calcolaOG(sumGU, Double.valueOf(fieldRendimento.getText()), Double.valueOf(fieldVolume.getText()));
            textOg.setText(String.valueOf(OG));
            
            // calcolo FG
            double FG = BeerMath.calcolaFG(OG, 75);
            textFg.setText(String.valueOf(FG));
        });
    }
    
    private void caricaInfoRicetta() throws IOException {
        
        // riempimento dropdown menu
        
        fieldTipo.getItems().setAll(Arrays.asList(Tipo.values()));
        
        // riempio i field con le info già esistenti 
        
        HttpResponse response = HttpConnector.getRequestWithToken("/recipes/info", "recipe=" + ricettaId, App.getToken());
        String responseBody = response.getResponseBody();
        
        Gson gson = new Gson();
        RicettaRequest ricetta = gson.fromJson(responseBody, RicettaRequest.class);
        
        String nome = ricetta.getNome();
        textNomeRicetta.setText(nome);
        fieldNomeRicetta.setText(nome);
        
        if(ricetta.getDescrizione()!=null)
            fieldDescrizione.setText(ricetta.getDescrizione());
        
        if(ricetta.getAutore()!=null)
            fieldAutore.setText(ricetta.getAutore());
        
        String tipo = ricetta.getTipo();
        fieldTipo.getSelectionModel().select(Tipo.indexOf(tipo));
        
        String stile = ricetta.getStileId();
        textStile.setText(stile);
        
        String volume = String.valueOf(ricetta.getVolume());
        fieldVolume.setText(volume);
        
        String rendimento = String.valueOf(ricetta.getRendimento());
        fieldRendimento.setText(rendimento);
        
        String ultimaModifica = ricetta.getUltimaModifica();
        LocalDate data = LocalDate.parse(ultimaModifica, DateTimeFormatter.ISO_DATE_TIME);
        textUltimaModifica.setText("Ultima modifica: " + data.getDayOfMonth() + " " + data.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALY) + " " + data.getYear());
    }
    
    private void setDimensionTableFermentabili() {
        // setto la larghezza delle colonne
        columnPesoFermentabile.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.2));
        columnNomeFermentabile.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.4));
        columnColore.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.10));
        columnCategoria.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.28));
        columnPesoFermentabile.setReorderable(false);
        columnNomeFermentabile.setReorderable(false);
        columnColore.setReorderable(false);
        columnCategoria.setReorderable(false);
        
        // sistemo altezza tabella
        tableFermentabili.setFixedCellSize(28);
        tableFermentabili.prefHeightProperty().bind(tableFermentabili.fixedCellSizeProperty().multiply(Bindings.size(tableFermentabili.getItems()).add(1.3)));
    }
    
    private void setDimensionTableLuppoli() {
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
        tableLuppoli.setFixedCellSize(28);
        tableLuppoli.prefHeightProperty().bind(tableLuppoli.fixedCellSizeProperty().multiply(Bindings.size(tableLuppoli.getItems()).add(1.3)));
    }
    
    /* =========== GETTERS & SETTERS =========== */
    
    public static void setRicettaId(int ricettaId) {
        ModificaRicettaController.ricettaId = ricettaId;
    }
    
    public static int getRicettaId() {
        return ModificaRicettaController.ricettaId;
    }
    
    public void setTextStile(String text) {
        this.textStile.setText(text);
    }
}
