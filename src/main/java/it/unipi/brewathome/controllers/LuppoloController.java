package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.LuppoloRequest;
import java.io.IOException;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class LuppoloController implements Initializable{

    private static ModificaRicettaController ricettaController;
    
    @FXML private TextField fieldQuantita;
    @FXML private TextField fieldNome;
    @FXML private TextField fieldCategoria;
    @FXML private TextField fieldFornitore;
    @FXML private TextField fieldProvenienza;
    @FXML private TextField fieldTipo;
    @FXML private TextField fieldTempo;
    @FXML private TextField fieldAlpha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        
    }
    
    @FXML
    private void salva() {
        try {
            //TODO: controlli input
            LuppoloRequest request = new LuppoloRequest(ModificaRicettaController.getRicettaId(),
                                                        fieldNome.getText(),
                                                        Integer.parseInt(fieldTempo.getText()),
                                                        Integer.parseInt(fieldQuantita.getText()),
                                                        fieldCategoria.getText(),
                                                        fieldFornitore.getText(),
                                                        fieldProvenienza.getText(),
                                                        fieldTipo.getText(),
                                                        Double.parseDouble(fieldAlpha.getText()));                                                   
            Gson gson = new Gson();
            String body = gson.toJson(request);
            
            HttpConnector.postRequestWithToken("/recipes/hops/add", body, App.getToken());
            
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
    private void annulla() {
        Stage stage = (Stage) fieldQuantita.getScene().getWindow();
        stage.close();
    }

    public static void setRicettaController(ModificaRicettaController ricettaController) {
        LuppoloController.ricettaController = ricettaController;
    }
}