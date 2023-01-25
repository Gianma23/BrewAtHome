package it.unipi.brewathome.controllers;

import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.data.Ricetta;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;


public class CardRicettaController {

    @FXML private VBox cardBox;
    private Ricetta ricetta;
    
    @FXML
    private void modificaRicetta() throws IOException {
        
        FXMLLoader loader = new FXMLLoader(App.class.getResource("modifica_ricetta.fxml"));
        ModificaRicettaController controller = new ModificaRicettaController();
        controller.setRicetta(ricetta); 
        loader.setController(controller);
        cardBox.getScene().setRoot(loader.load());
    }
    
    public void setRicetta(Ricetta ricetta) {
        this.ricetta = ricetta;
    }
}
