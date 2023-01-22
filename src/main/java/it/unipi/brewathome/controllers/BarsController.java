package it.unipi.brewathome.controllers;

import it.unipi.brewathome.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class BarsController {
    
    @FXML
    private void openRicette() throws IOException {
        App.setRoot("ricette");
    }
}
