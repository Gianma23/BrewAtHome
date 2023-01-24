package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.unipi.brewathome.utils.TipoRicetta;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.Fermentabile;
import it.unipi.brewathome.connection.requests.Luppolo;
import it.unipi.brewathome.connection.requests.Ricetta;
import it.unipi.brewathome.connection.responses.HttpResponse;
import it.unipi.brewathome.connection.responses.Stile;
import it.unipi.brewathome.utils.BeerMath;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
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
    private ObservableList<Fermentabile> fermentabili;
    private ObservableList<Luppolo> luppoli;
    private List<Luppolo> luppoliList;
    private Ricetta ricetta;
    private Stile stile;
    
    @FXML private GridPane grid;
    @FXML private TableView tableFermentabili;
    @FXML private TableView tableLuppoli;
    
    @FXML private TableColumn columnPesoFermentabile;
    @FXML private TableColumn columnNomeFermentabile;
    @FXML private TableColumn columnColore;
    @FXML private TableColumn columnTipo;
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
    @FXML private Text textEbc;
    @FXML private Text textIbu;
    @FXML private Text errorMessage;
    
    @FXML private TextArea fieldDescrizione;
    @FXML private TextField fieldNomeRicetta;
    @FXML private TextField fieldAutore;
    @FXML private TextField fieldVolume;
    @FXML private TextField fieldRendimento;
    @FXML private ChoiceBox fieldTipo;
    
    @FXML private Rectangle barAbv;
    @FXML private Rectangle barOg;
    @FXML private Rectangle barFg;
    @FXML private Rectangle barEbc;
    @FXML private Rectangle barIbu;
    
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {   
            App.addLeftBar(grid);
            
            // carico i fermentabili e i luppoli nella tabella
            
            columnPesoFermentabile.setCellValueFactory(new PropertyValueFactory<>("quantita"));
            columnNomeFermentabile.setCellValueFactory(new PropertyValueFactory<>("nome"));
            columnColore.setCellValueFactory(new PropertyValueFactory<>("colore"));
            columnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

            columnPesoLuppolo.setCellValueFactory(new PropertyValueFactory<>("quantita"));
            columnNomeLuppolo.setCellValueFactory(new PropertyValueFactory<>("nome"));
            columnAlpha.setCellValueFactory(new PropertyValueFactory<>("alpha"));
            columnTempo.setCellValueFactory(new PropertyValueFactory<>("tempo"));
            
            fermentabili = FXCollections.observableArrayList();
            tableFermentabili.setItems(fermentabili);

            luppoli = FXCollections.observableArrayList();
            tableLuppoli.setItems(luppoli);
            luppoliList = new ArrayList();
            
            // sistemo dimensioni delle tabelle
            setDimensionTableFermentabili();
            setDimensionTableLuppoli();
            
            IntegerProperty taskCounter = new SimpleIntegerProperty(3);
            
            // carico info della ricetta
            Task taskInfo = caricaInfoRicetta();
            taskInfo.setOnSucceeded(e -> taskCounter.set(taskCounter.get() - 1));
            new Thread(taskInfo).start();
            
            // aggiungo gli ingredienti
            Task taskLup = caricaLuppoli();
            taskLup.setOnSucceeded(e -> taskCounter.set(taskCounter.get() - 1));
            new Thread(taskLup).start();
            
            Task taskFerm = caricaFermentabili();
            taskFerm.setOnSucceeded(e -> taskCounter.set(taskCounter.get() - 1));
            new Thread(taskFerm).start();
            
            //se la ricetta ha caricato aggiorno le statistiche
            taskCounter.addListener((o, oldValue, newValue) -> {
                if(newValue.intValue() == 0) {
                    // aggiungo listener per statistiche birra
                    fermentabili.addListener((ListChangeListener<Fermentabile>) (e) -> aggiornaStats());
                    luppoli.addListener((ListChangeListener<Luppolo>) (e) -> aggiornaStats());
                    fieldVolume.focusedProperty().addListener((obs, oldVal, newVal) -> {
                        if(!newVal) aggiornaStats();
                    });
                    fieldRendimento.focusedProperty().addListener((obs, oldVal, newVal) -> {
                        if(!newVal) aggiornaStats();
                    });
                    aggiornaStats();
                }
            });
        }
        catch (IOException ioe) {
            logger.error(ioe);
        }
    }
    
    /* ================ FXML FUNC ================ */
  
    @FXML
    private void aggiungiFermentabile() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("aggiungi_fermentabile.fxml"));
            FermentabileController controller = new FermentabileController();
            controller.setRicettaController(this); 
            loader.setController(controller);
            Scene scene = new Scene(loader.load());

            App.addStyle(scene, "style.css");
            App.addStyle(scene, "fonts.css");
            App.addStyle(scene, "global.css");

            stage.setScene(scene);
            stage.setTitle("Fermentabile");
            stage.setResizable(false);
            stage.show();
            
            // chiudo la finestra quando cambio pagina
            grid.getScene().rootProperty().addListener((observable, oldValue, newValue) -> {
                stage.close();
            });
        }
        catch (IOException ioe) {
            logger.error(ioe);
        }
    }
    
    @FXML
    private void aggiungiLuppolo() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("aggiungi_luppolo.fxml"));
            LuppoloController controller = new LuppoloController();
            controller.setRicettaController(this); 
            loader.setController(controller);  
            Scene scene = new Scene(loader.load());

            App.addStyle(scene, "style.css");
            App.addStyle(scene, "fonts.css");
            App.addStyle(scene, "global.css");

            stage.setScene(scene);
            stage.setTitle("Luppolo");
            stage.setResizable(false);
            stage.show();
            
            // listener sul cambio di root
            grid.getScene().rootProperty().addListener((observable, oldValue, newValue) -> {
                stage.close();
            });
        }
        catch (IOException ioe) {
            logger.error(ioe);
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
            logger.error(ioe);
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
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    HttpConnector.deleteRequestWithToken("/recipes/remove", "recipe=" + ricetta.getId(), App.getToken());
                    App.setRoot("ricette");
                }
                catch (IOException ioe) {
                    logger.error(ioe);
                } 
                return null;
            }
        };
        new Thread(task).start();
    }
    
    @FXML
    private void salvaRicetta() {
        try {
            //controlli input
            Double volume = Double.valueOf(fieldVolume.getText());
            Double rendimento = Double.valueOf(fieldRendimento.getText());
            if(volume <= 0 || rendimento <= 0) {
                errorMessage.setText("inserire valori maggiori di 0.");
                return;
            }
            
            Ricetta request = new Ricetta(ricetta.getId(),
                                          fieldNomeRicetta.getText(),
                                          fieldDescrizione.getText(),
                                          fieldAutore.getText(),
                                          fieldTipo.getSelectionModel().getSelectedItem().toString(),
                                          textStile.getText(),
                                          volume,
                                          rendimento);
            //serializzazione
            Gson gson = new Gson();
            String body = gson.toJson(request);
        
            Task task = new Task<Void>() {
                @Override public Void call() {
                    try {
                        HttpConnector.postRequestWithToken("/recipes/update", body, App.getToken());
                        App.setRoot("ricette");
                    }
                    catch (IOException ioe) {
                        logger.error(ioe);
                    }
                    return null;
                }
            };
            new Thread(task).start();
        }   
        catch(NumberFormatException ne) {
            logger.error(ne);
        }
    }

    @FXML
    private void esci() throws IOException {
        App.setRoot("ricette");
    }
    
    /* =============== TASK ASYNC =============== */
        
    private Task caricaFermentabili() throws IOException {
        //svuoto la tabella e la lista
        tableFermentabili.getItems().clear();
        
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    HttpResponse response = HttpConnector.getRequestWithToken("/fermentables/all", "recipe=" + ricetta.getId(), App.getToken());
                    String responseBody = response.getResponseBody();

                    if(responseBody.equals(""))
                        return null;

                    Gson gson = new Gson();
                    JsonArray fermentabiliArray = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
                    Platform.runLater(() -> {
                        for(JsonElement fermentabile : fermentabiliArray) {
                            Fermentabile fermentabileTable = gson.fromJson(fermentabile, Fermentabile.class);
                            fermentabili.add(fermentabileTable);
                        }
                    });
                }
                catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                return null;
            }
        };
        return task;
    }
    
    private Task caricaLuppoli() throws IOException {
        //svuoto la tabella
        tableLuppoli.getItems().clear();
        
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    HttpResponse response = HttpConnector.getRequestWithToken("/hops/all", "recipe=" + ricetta.getId(), App.getToken());
                    String responseBody = response.getResponseBody();

                    if(responseBody.equals(""))
                        return null;

                    Gson gson = new Gson();
                    JsonArray luppoliArray = gson.fromJson(responseBody, JsonElement.class).getAsJsonArray();
                    Platform.runLater(() -> {
                        for(JsonElement luppolo : luppoliArray) {
                            Luppolo luppoloTable = gson.fromJson(luppolo, Luppolo.class);
                            luppoli.add(luppoloTable);
                            Luppolo lup = gson.fromJson(luppolo, Luppolo.class);
                            luppoliList.add(lup);
                        }
                    });
                }
                catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
                return null;
            }
        };
        return task;
    }
    
    private Task caricaInfoRicetta() throws IOException {
        // riempimento dropdown menu
        fieldTipo.getItems().setAll(Arrays.asList(TipoRicetta.values()));
        
        // riempio i field con le info già esistenti 
        
        textNomeRicetta.setText(ricetta.getNome());
        fieldNomeRicetta.setText(ricetta.getNome());
        fieldDescrizione.setText(ricetta.getDescrizione());
        fieldAutore.setText(ricetta.getAutore());
        fieldTipo.getSelectionModel().select(TipoRicetta.indexOf(ricetta.getTipo()));
        fieldVolume.setText(String.valueOf(ricetta.getVolume()));
        fieldRendimento.setText(String.valueOf(ricetta.getRendimento()));
        String ultimaModifica = ricetta.getUltimaModifica();
        LocalDate data = LocalDate.parse(ultimaModifica, DateTimeFormatter.ISO_DATE_TIME);
        textUltimaModifica.setText("Ultima modifica: " + data.getDayOfMonth() + " " + data.getMonth().getDisplayName(TextStyle.FULL, Locale.ITALY) + " " + data.getYear());
        
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    Gson gson = new Gson();
                    HttpResponse responseStile = HttpConnector.getRequest("/categories/filter-name", "name=" + ricetta.getStileId());
                    String stileBody = responseStile.getResponseBody();
                    stile = gson.fromJson(stileBody, Stile.class);
                    
                    Platform.runLater(() -> {          
                        textStile.setText(ricetta.getStileId());
                    });
                }
                catch (IOException ioe) {
                    throw new UncheckedIOException(ioe);
                }
                return null;
            }
        };
        return task;
    }
    
    /* ================ UTILITA ================ */
    
    public void aggiornaStats() {          
        // calcolo OG
        int sumGU = 0;
        for(Fermentabile fermentabile : fermentabili) {
            sumGU += BeerMath.CalcolaGU(fermentabile.getPotenziale(), fermentabile.getQuantita());
        }
        double OG = BeerMath.CalcolaOG(sumGU, Double.valueOf(fieldRendimento.getText()), Double.valueOf(fieldVolume.getText()));
        textOg.setText(String.valueOf(OG));
        
        // calcolo FG
        double FG = BeerMath.CalcolaFG(OG, 75);
        textFg.setText(String.valueOf(FG));

        //calcolo ABV
        double ABV = BeerMath.CalcolaABV(OG, FG);
        textAbv.setText(String.valueOf(ABV) + "%");

        //calcolo EBC
        List<Integer> arrayEBC = new ArrayList();
        List<Integer> arrayPeso = new ArrayList();
        for(Fermentabile fermentabile : fermentabili) {
            arrayEBC.add(fermentabile.getColore());
            arrayPeso.add(fermentabile.getQuantita());
        }
        int[] ebc = arrayEBC.stream().mapToInt(i->i).toArray();
        int[] pesi = arrayPeso.stream().mapToInt(i->i).toArray();
        double EBC = BeerMath.CalcolaEBC(ebc, pesi, Double.valueOf(fieldVolume.getText()));
        textEbc.setText(String.valueOf(EBC));

        //calcolo IBU
        List<Double> arrayAlpha = new ArrayList();
        List<Integer> arrayPesoIbu = new ArrayList();
        List<Integer> arrayMinuti = new ArrayList();
        for(Luppolo luppolo : luppoli) {
            arrayAlpha.add(luppolo.getAlpha());
            arrayPesoIbu.add(luppolo.getQuantita());
            arrayMinuti.add(luppolo.getTempo());
        }
        double[] alpha = arrayAlpha.stream().mapToDouble(i->i).toArray();
        int[] pesiIbu = arrayPesoIbu.stream().mapToInt(i->i).toArray();
        int[] minuti = arrayMinuti.stream().mapToInt(i->i).toArray();
        double IBU = BeerMath.RagerIBU(alpha, pesiIbu, minuti, Double.valueOf(fieldVolume.getText()));
        textIbu.setText(String.valueOf(IBU));
        
        aggiornaBarreStats(OG, FG, ABV, EBC, IBU);
    }
    
    private void aggiornaBarreStats(double OG, double FG, double ABV, double EBC, double IBU) {
        //aggiorno barra OG
        int offsetOG = (int) Math.round((OG - stile.getOgMin())*128/(stile.getOgMax()-stile.getOgMin()));
        long realOffsetOG = (offsetOG < 0)? Math.max(offsetOG, -42) : Math.min(offsetOG, 170);
        barOg.translateXProperty().set(realOffsetOG);
        
        //aggiorno barra FG
        int offsetFG = (int) Math.round((FG - stile.getFgMin())*128/(stile.getFgMax()-stile.getFgMin()));
        long realOffsetFG = (offsetFG < 0)? Math.max(offsetFG, -42) : Math.min(offsetFG, 170);
        barFg.translateXProperty().set(realOffsetFG);
        
        //aggiorno barra ABV
        int offsetABV = (int) Math.round((ABV - stile.getAbvMin())*128/(stile.getAbvMax()-stile.getAbvMin()));
        long realOffsetABV = (offsetABV < 0)? Math.max(offsetABV, -42) : Math.min(offsetABV, 170);
        barAbv.translateXProperty().set(realOffsetABV);
        
        //aggiorno barra EBC
        double ebcMin = stile.getSrmMin()*1.97;
        double ebcMax = stile.getSrmMax()*1.97;
        int offsetEBC = (int) Math.round((EBC - ebcMin)*128/(ebcMax-ebcMin));
        long realOffsetEBC = (offsetEBC < 0)? Math.max(offsetEBC, -42) : Math.min(offsetEBC, 170);
        barEbc.translateXProperty().set(realOffsetEBC);
        
        //aggiorno barra IBU
        int offsetIBU = (int) Math.round((IBU - stile.getIbuMin())*128/(stile.getIbuMax()-stile.getIbuMin()));
        long realOffsetIBU = (offsetIBU < 0)? Math.max(offsetIBU, -42) : Math.min(offsetIBU, 170);
        barIbu.translateXProperty().set(realOffsetIBU);
    }
    
    private void setDimensionTableFermentabili() {
        // setto la larghezza delle colonne
        columnPesoFermentabile.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.15));
        columnNomeFermentabile.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.4));
        columnColore.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.20));
        columnTipo.prefWidthProperty().bind(tableFermentabili.widthProperty().multiply(0.23));
        columnPesoFermentabile.setReorderable(false);
        columnNomeFermentabile.setReorderable(false);
        columnColore.setReorderable(false);
        columnTipo.setReorderable(false);
        
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
    
    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }
    
    public int getRicettaId() {
        return this.ricetta.getId();
    }

    public ObservableList<Fermentabile> getFermentabili() {
        return fermentabili;
    }
    
    public ObservableList<Luppolo> getLuppoli() {
        return luppoli;
    }
    
    public void setStile(Stile stile) {
        this.stile = stile;
        this.textStile.setText(stile.getNome());
    }
}
