package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.requests.AuthRequest;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.responses.HttpResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class AccediController {
    
    private static final Logger logger =LogManager.getLogger(AccediController.class);
    @FXML private Button buttonAccedi;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Text message;
    
    @FXML 
    private void login() {
        
        Gson gson = new Gson();
        AuthRequest request = new AuthRequest(email.getText(), password.getText());
        
        buttonAccedi.setDisable(true);
        email.setDisable(true);
        password.setDisable(true);
        message.setText("Accesso in corso...");
        
        Task task = new Task<Void>() {
            @Override public Void call() {
                try {
                    HttpResponse response = HttpConnector.postRequest("/auth/login", gson.toJson(request));
                    String responseHeader = response.getResponseHeader();
                    String responseBody = response.getResponseBody();
                    int responseCode = response.getResponseCode();
                       
                    Platform.runLater(() -> {
                        if (200 <= responseCode && responseCode <= 299) {        
                            Stage stage = (Stage) email.getScene().getWindow();
                            try {
                                App.setToken(responseHeader);
                                App.setRoot("ricette");
                            }
                            catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }

                            stage.setWidth(1480);
                            stage.setHeight(900);
                            stage.centerOnScreen();
                        }
                        else {
                            buttonAccedi.setDisable(false);
                            email.setDisable(false);
                            password.setDisable(false);
                            message.setText(responseBody);
                        }
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
    
    @FXML
    private void openRegistrati() {
        try {
            App.setRoot("registrati");
        }
        catch (IOException ioe) {
            logger.error(ioe);
        }      
    }
}
