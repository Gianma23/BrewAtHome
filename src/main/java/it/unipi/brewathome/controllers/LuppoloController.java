package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.Fermentabile;
import it.unipi.brewathome.connection.requests.Luppolo;
import it.unipi.brewathome.utils.TipoLuppolo;
import it.unipi.brewathome.utils.TipoRicetta;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
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
    private static ModificaRicettaController ricettaController;
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
        
        updateLuppolo = null;
    }
    
    @FXML
    private void salva() {
        try {
            errorMessage.setText("");
            
            Luppolo request = new Luppolo(id,
                                        ModificaRicettaController.getRicettaId(),
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
            HttpConnector.postRequestWithToken("/hops/add", body, App.getToken());
            
            //ricarico la tabella
            ricettaController.caricaLuppoli();
            
            Stage stage = (Stage) fieldQuantita.getScene().getWindow();
            stage.close();
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
        catch(NumberFormatException ne) {
            errorMessage.setText("Inserire i dati nel formato corretto.");
        }
    }
    
    @FXML
    private void elimina() {
        try {
            HttpConnector.deleteRequestWithToken("/hops/remove", "id=" + id, App.getToken());
            //ricarico la tabella
            ricettaController.caricaLuppoli();
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
        Stage stage = (Stage) fieldQuantita.getScene().getWindow();
        stage.close();
    }

    public static void setRicettaController(ModificaRicettaController ricettaController) {
        LuppoloController.ricettaController = ricettaController;
    }

    public static void setUpdateLuppolo(Luppolo updateLuppolo) {
        LuppoloController.updateLuppolo = updateLuppolo;
    }
}