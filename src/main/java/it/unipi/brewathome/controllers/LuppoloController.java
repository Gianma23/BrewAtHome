package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.data.Luppolo;
import it.unipi.brewathome.connection.data.HttpResponse;
import it.unipi.brewathome.utils.TipoLuppolo;
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


public class LuppoloController implements Initializable{

    private static final Logger logger =LogManager.getLogger(LuppoloController.class.getName());
    private ModificaRicettaController ricettaController;
    private static Luppolo updateLuppolo;
    private int id;
    
    @FXML private TextField fieldQuantita;
    @FXML private TextField fieldNome;
    @FXML private TextField fieldFornitore;
    @FXML private TextField fieldProvenienza;
    @FXML private ChoiceBox fieldTipo;
    @FXML private TextField fieldTempo;
    @FXML private TextField fieldAlpha;
    @FXML private Text errorMessage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // riempimento dropdown menu
        fieldTipo.getItems().setAll(Arrays.asList(TipoLuppolo.values()));
        fieldTipo.setValue("Pellet");
        
        if(updateLuppolo==null)
            return;
        
        fieldQuantita.setText(String.valueOf(updateLuppolo.getQuantita()));
        fieldNome.setText(updateLuppolo.getNome());
        fieldFornitore.setText(updateLuppolo.getFornitore());
        fieldProvenienza.setText(updateLuppolo.getProvenienza());
        fieldTipo.getSelectionModel().select(TipoLuppolo.indexOf(updateLuppolo.getTipo()));
        fieldAlpha.setText(String.valueOf(updateLuppolo.getAlpha()));
        fieldTempo.setText(String.valueOf(updateLuppolo.getTempo()));
        id = updateLuppolo.getId();
    }
    
    @FXML
    private void salva() {
        try {
            //controllo input
            if(!validInputs()) {
                errorMessage.setText("Inserire numeri maggiori di 0");
                return;
            }
            disableInputs();
            errorMessage.setText("Salvataggio in corso...");
            
            Luppolo request = new Luppolo(id,
                                        ricettaController.getRicettaId(),
                                        fieldNome.getText(),
                                        Integer.parseInt(fieldTempo.getText()),
                                        Integer.parseInt(fieldQuantita.getText()),
                                        fieldFornitore.getText(),
                                        fieldProvenienza.getText(),
                                        fieldTipo.getSelectionModel().getSelectedItem().toString(),
                                        Double.parseDouble(fieldAlpha.getText()));                                                   
            //serializzazione dati
            Gson gson = new Gson();
            String body = gson.toJson(request);
            
            Task task = new Task<Void>() {
                @Override public Void call() {
                    try {      
                        HttpResponse response = HttpConnector.postRequestWithToken("/hops/add", body, App.getToken());
                        int hopId = Integer.parseInt(response.getResponseBody());
                        request.setId(hopId);
                        
                        Platform.runLater(() -> {
                            if(updateLuppolo != null) {
                                int index = ricettaController.getLuppoli().indexOf(updateLuppolo);
                                ricettaController.getLuppoli().set(index, request);
                                updateLuppolo = null;
                            }
                            else
                                ricettaController.getLuppoli().add(request);

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
                    HttpResponse response = HttpConnector.deleteRequestWithToken("/hops/remove", "id=" + id, App.getToken());
                    
                    Gson gson = new Gson();
                    Luppolo lup = gson.fromJson(response.getResponseBody(), Luppolo.class);
                    
                    Platform.runLater(() -> {
                        //aggiungo alla tabella
                        ricettaController.getLuppoli().remove(lup);

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
    
    /* =========== UTILITA =========== */
    
    private void disableInputs() {
        fieldNome.setDisable(true);
        fieldQuantita.setDisable(true);
        fieldFornitore.setDisable(true);
        fieldProvenienza.setDisable(true);
        fieldTipo.setDisable(true);
        fieldTempo.setDisable(true);
        fieldAlpha.setDisable(true);
    }
    
    private void enableInputs() {
        fieldNome.setDisable(false);
        fieldQuantita.setDisable(false);
        fieldFornitore.setDisable(false);
        fieldProvenienza.setDisable(false);
        fieldTipo.setDisable(false);
        fieldTempo.setDisable(false);
        fieldAlpha.setDisable(false);
    }
    
    private boolean validInputs() {
        return !(Integer.valueOf(fieldQuantita.getText()) <= 0 || Integer.valueOf(fieldAlpha.getText()) <= 0 ||
                 Double.valueOf(fieldTempo.getText()) <= 0);
    }
    
    /* =========== SETTERS =========== */
    
    public void setRicettaController(ModificaRicettaController ricettaController) {
        this.ricettaController = ricettaController;
    }

    public static void setUpdateLuppolo(Luppolo updateLuppolo) {
        LuppoloController.updateLuppolo = updateLuppolo;
    }
}