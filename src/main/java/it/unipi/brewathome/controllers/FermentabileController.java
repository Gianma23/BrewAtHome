package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.Fermentabile;
import it.unipi.brewathome.connection.responses.HttpResponse;
import it.unipi.brewathome.utils.CategoriaFermentabile;
import it.unipi.brewathome.utils.TipoFermentabile;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FermentabileController implements Initializable{

    private static final Logger logger =LogManager.getLogger(FermentabileController.class.getName());
    private static ModificaRicettaController ricettaController;
    private static Fermentabile updateFermentabile;
    private int id;
    
    @FXML private TextField fieldQuantita;
    @FXML private TextField fieldNome;
    @FXML private ChoiceBox fieldCategoria;
    @FXML private TextField fieldFornitore;
    @FXML private TextField fieldProvenienza;
    @FXML private ChoiceBox fieldTipo;
    @FXML private TextField fieldColore;
    @FXML private TextField fieldPotenziale;
    @FXML private TextField fieldRendimento;
    @FXML private Text errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        // riempimento dropdown menu
        fieldTipo.getItems().setAll(Arrays.asList(TipoFermentabile.values()));
        fieldTipo.setValue("Grani");
        fieldCategoria.getItems().setAll(Arrays.asList(CategoriaFermentabile.values()));
        fieldCategoria.setValue("Base");
        
        if(updateFermentabile==null)
            return;
        
        fieldQuantita.setText(String.valueOf(updateFermentabile.getQuantita()));
        fieldNome.setText(updateFermentabile.getNome());
        fieldCategoria.getSelectionModel().select(CategoriaFermentabile.indexOf(updateFermentabile.getCategoria()));
        fieldFornitore.setText(updateFermentabile.getFornitore());
        fieldProvenienza.setText(updateFermentabile.getProvenienza());
        fieldTipo.getSelectionModel().select(TipoFermentabile.indexOf(updateFermentabile.getTipo()));
        fieldColore.setText(String.valueOf(updateFermentabile.getColore()));
        fieldPotenziale.setText(String.valueOf(updateFermentabile.getPotenziale()));
        fieldRendimento.setText(String.valueOf(updateFermentabile.getRendimento()));
        id = updateFermentabile.getId();
        
        updateFermentabile = null;
    }
    
    @FXML
    private void salva() {
        try {
            errorMessage.setText("");
 
            Fermentabile request = new Fermentabile(id,
                                                    ModificaRicettaController.getRicettaId(),
                                                    fieldNome.getText(),
                                                    Integer.valueOf(fieldQuantita.getText()),
                                                    fieldCategoria.getSelectionModel().getSelectedItem().toString(),
                                                    fieldFornitore.getText(),
                                                    fieldProvenienza.getText(),
                                                    fieldTipo.getSelectionModel().getSelectedItem().toString(),
                                                    Integer.valueOf(fieldColore.getText()),
                                                    Double.valueOf(fieldPotenziale.getText()),
                                                    Double.valueOf(fieldRendimento.getText()));  
            //serializzazione dati
            Gson gson = new Gson();
            String body = gson.toJson(request);
            
            Task task = new Task<Void>() {
                @Override public Void call() {
                    try {                                           
                        HttpConnector.postRequestWithToken("/fermentables/add", body, App.getToken());
                        
                        Platform.runLater(() -> {
                            //aggiungo alla tabella
                            ricettaController.getFermentabili().add(request);

                            Stage stage = (Stage) fieldQuantita.getScene().getWindow();
                            stage.close();
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
        catch(NumberFormatException ne) {
            errorMessage.setText("Inserire i dati nel formato corretto.");
        }
    }
    
    @FXML
    private void elimina() {
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    HttpResponse response = HttpConnector.deleteRequestWithToken("/fermentables/remove", "id=" + id, App.getToken());
                    
                    Gson gson = new Gson();
                    Fermentabile ferm = gson.fromJson(response.getResponseBody(), Fermentabile.class);

                    Platform.runLater(() -> {
                        //aggiungo alla tabella
                        ricettaController.getFermentabili().remove(ferm);
                        
                        Stage stage = (Stage) fieldQuantita.getScene().getWindow();
                        stage.close();
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

    /* =========== SETTERS =========== */
    
    public static void setRicettaController(ModificaRicettaController ricettaController) {
        FermentabileController.ricettaController = ricettaController;
    }

    public static void setUpdateFermentabile(Fermentabile updateFermentabile) {
        FermentabileController.updateFermentabile = updateFermentabile;
    }
}