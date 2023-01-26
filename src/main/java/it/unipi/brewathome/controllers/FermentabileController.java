package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.data.Fermentabile;
import it.unipi.brewathome.connection.data.HttpResponse;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FermentabileController implements Initializable{

    private static final Logger logger =LogManager.getLogger(FermentabileController.class.getName());
    private ModificaRicettaController ricettaController;
    private static Fermentabile updateFermentabile;
    private int id;
    
    @FXML private Button buttonSalva;
    @FXML private Button buttonElimina;
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
        
        if(updateFermentabile==null) {
            return;
        }
        
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
    }
    
    @FXML
    private void salva() {
        try {
            
            //controllo input
            if(!validInputs()) {
                errorMessage.setText("Numeri nel formato sbagliato.");
                return;
            }
            disableInputs();
            errorMessage.setText("Salvataggio in corso...");
            
            Fermentabile request = new Fermentabile(id,
                                                    ricettaController.getRicettaId(),
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
                        HttpResponse response = HttpConnector.postRequestWithToken("/fermentables/add", body, App.getToken());
                        int fermentableId = Integer.parseInt(response.getResponseBody());
                        request.setId(fermentableId);
                        
                        Platform.runLater(() -> {
                            if(updateFermentabile != null) {
                                int index = ricettaController.getFermentabili().indexOf(updateFermentabile);
                                ricettaController.getFermentabili().set(index, request);
                                updateFermentabile = null;
                            }
                            else
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
            errorMessage.setText("Numeri nel formato sbagliato.");
            enableInputs();
        }
    }
    
    @FXML
    private void elimina() {
        disableInputs();
        errorMessage.setText("Salvataggio in corso...");
            
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
                        updateFermentabile = null;
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
        fieldCategoria.setDisable(true);
        fieldQuantita.setDisable(true);
        fieldFornitore.setDisable(true);
        fieldProvenienza.setDisable(true);
        fieldTipo.setDisable(true);
        fieldColore.setDisable(true);
        fieldPotenziale.setDisable(true);
        fieldRendimento.setDisable(true);
        buttonElimina.setDisable(false);
        buttonSalva.setDisable(false);
    }
    
    private void enableInputs() {
        fieldNome.setDisable(false);
        fieldCategoria.setDisable(false);
        fieldQuantita.setDisable(false);
        fieldFornitore.setDisable(false);
        fieldProvenienza.setDisable(false);
        fieldTipo.setDisable(false);
        fieldColore.setDisable(false);
        fieldPotenziale.setDisable(false);
        fieldRendimento.setDisable(false);
        buttonElimina.setDisable(false);
        buttonSalva.setDisable(false);
    }
    
    private boolean validInputs() {
        return !(Integer.valueOf(fieldQuantita.getText()) <= 0 || Integer.valueOf(fieldColore.getText()) <= 0 ||
                 Double.valueOf(fieldPotenziale.getText()) < 1 || Double.valueOf(fieldRendimento.getText()) <= 0);
    }
    
    /* =========== SETTERS =========== */
    
    public void setRicettaController(ModificaRicettaController ricettaController) {
        this.ricettaController = ricettaController;
    }

    public static void setUpdateFermentabile(Fermentabile updateFermentabile) {
        FermentabileController.updateFermentabile = updateFermentabile;
    }
}