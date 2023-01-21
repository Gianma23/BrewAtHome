package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.FermentabileRequest;
import it.unipi.brewathome.connection.requests.LuppoloRequest;
import it.unipi.brewathome.utils.Tipo;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class LuppoloController implements Initializable{

    private static ModificaRicettaController ricettaController;
    private static LuppoloRequest updateLuppolo;
    private int id;
    
    @FXML private TextField fieldQuantita;
    @FXML private TextField fieldNome;
    @FXML private TextField fieldCategoria;
    @FXML private TextField fieldFornitore;
    @FXML private TextField fieldProvenienza;
    @FXML private ChoiceBox fieldTipo;
    @FXML private TextField fieldTempo;
    @FXML private TextField fieldAlpha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // riempimento dropdown menu
        fieldTipo.getItems().setAll(Arrays.asList(Tipo.values()));
        
        if(updateLuppolo==null)
            return;
        
        fieldQuantita.setText(String.valueOf(updateLuppolo.getQuantita()));
        fieldNome.setText(updateLuppolo.getNome());
        fieldCategoria.setText(updateLuppolo.getCategoria());
        fieldFornitore.setText(updateLuppolo.getFornitore());
        fieldProvenienza.setText(updateLuppolo.getProvenienza());
        fieldTipo.getSelectionModel().select(Tipo.indexOf(updateLuppolo.getTipo()));
        fieldAlpha.setText(String.valueOf(updateLuppolo.getAlpha()));
        fieldTempo.setText(String.valueOf(updateLuppolo.getTempo()));
        id = updateLuppolo.getId();
        
        updateLuppolo = null;
    }
    
    @FXML
    private void salva() {
        try {
            //TODO: controlli input
            LuppoloRequest request = new LuppoloRequest(id,
                                                        ModificaRicettaController.getRicettaId(),
                                                        fieldNome.getText(),
                                                        Integer.parseInt(fieldTempo.getText()),
                                                        Integer.parseInt(fieldQuantita.getText()),
                                                        fieldCategoria.getText(),
                                                        fieldFornitore.getText(),
                                                        fieldProvenienza.getText(),
                                                        fieldTipo.getSelectionModel().getSelectedItem().toString(),
                                                        Double.parseDouble(fieldAlpha.getText()));                                                   
            Gson gson = new Gson();
            String body = gson.toJson(request);
            
            HttpConnector.postRequestWithToken("/hops/add", body, App.getToken());
            
            //ricarico la tabella
            ricettaController.caricaLuppoli();
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        
        Stage stage = (Stage) fieldQuantita.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void elimina() {
        try {
            HttpConnector.deleteRequestWithToken("/hops/remove", "id=" + id, App.getToken());
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        Stage stage = (Stage) fieldQuantita.getScene().getWindow();
        stage.close();
    }

    public static void setRicettaController(ModificaRicettaController ricettaController) {
        LuppoloController.ricettaController = ricettaController;
    }

    public static void setUpdateLuppolo(LuppoloRequest updateLuppolo) {
        LuppoloController.updateLuppolo = updateLuppolo;
    }
}