package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.requests.FermentabileRequest;
import it.unipi.brewathome.connection.responses.HttpResponse;
import it.unipi.brewathome.enums.Tipo;
import java.io.IOException;
import java.net.IDN;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
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
public class FermentabileController implements Initializable{

    private static ModificaRicettaController ricettaController;
    private static FermentabileRequest updateFermentabile;
    private int id;
    
    @FXML private TextField fieldQuantita;
    @FXML private TextField fieldNome;
    @FXML private TextField fieldCategoria;
    @FXML private TextField fieldFornitore;
    @FXML private TextField fieldProvenienza;
    @FXML private ChoiceBox fieldTipo;
    @FXML private TextField fieldColore;
    @FXML private TextField fieldPotenziale;
    @FXML private TextField fieldRendimento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        // riempimento dropdown menu
        fieldTipo.getItems().setAll(Arrays.asList(Tipo.values()));
        
        if(updateFermentabile==null)
            return;
        
        fieldQuantita.setText(String.valueOf(updateFermentabile.getQuantita()));
        fieldNome.setText(updateFermentabile.getNome());
        fieldCategoria.setText(updateFermentabile.getCategoria());
        fieldFornitore.setText(updateFermentabile.getFornitore());
        fieldProvenienza.setText(updateFermentabile.getProvenienza());
        fieldTipo.getSelectionModel().select(Tipo.indexOf(updateFermentabile.getTipo()));
        fieldColore.setText(String.valueOf(updateFermentabile.getColore()));
        fieldPotenziale.setText(String.valueOf(updateFermentabile.getPotenziale()));
        fieldRendimento.setText(String.valueOf(updateFermentabile.getRendimento()));
        id = updateFermentabile.getId();
        
        updateFermentabile = null;
    }
    
    @FXML
    private void salva() {
        try {
            //TODO: controlli input
            FermentabileRequest request = new FermentabileRequest(id,
                                                                  ModificaRicettaController.getRicettaId(),
                                                                  fieldNome.getText(),
                                                                  Integer.parseInt(fieldQuantita.getText()),
                                                                  fieldCategoria.getText(),
                                                                  fieldFornitore.getText(),
                                                                  fieldProvenienza.getText(),
                                                                  fieldTipo.getSelectionModel().getSelectedItem().toString(),
                                                                  Integer.parseInt(fieldColore.getText()),
                                                                  Integer.parseInt(fieldPotenziale.getText()),
                                                                  Integer.parseInt(fieldRendimento.getText()));                                                   
            //serializzazione dati
            Gson gson = new Gson();
            String body = gson.toJson(request);
            HttpConnector.postRequestWithToken("/fermentables/add", body, App.getToken());
            
            //ricarico la tabella
            ricettaController.caricaFermentabili();
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
            HttpConnector.deleteRequestWithToken("/fermentables/remove", "id=" + id, App.getToken());
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        Stage stage = (Stage) fieldQuantita.getScene().getWindow();
        stage.close();
    }

    public static void setRicettaController(ModificaRicettaController ricettaController) {
        FermentabileController.ricettaController = ricettaController;
    }

    public static void setUpdateFermentabile(FermentabileRequest updateFermentabile) {
        FermentabileController.updateFermentabile = updateFermentabile;
    }
}