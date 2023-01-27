package it.unipi.brewathome.controllers;

import com.google.gson.Gson;
import it.unipi.brewathome.App;
import it.unipi.brewathome.connection.data.AuthRequest;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.data.HttpResponse;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RegistratiController {
    
    private static final Logger logger =LogManager.getLogger(RegistratiController.class.getName());
    @FXML private Button buttonRegistrati;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private PasswordField confirmPassword;
    @FXML private Text errorMessage;
    
    @FXML 
    private void register() {
        
        Pattern pattern = Pattern.compile("^(.+)@(\\S+)$");
        if(!pattern.matcher(email.getText()).matches()) {
            errorMessage.setText("Inserire una mail valida.");
            return;
        }
        if(password.getText().length() < 3) {
            errorMessage.setText("La password deve essere lunga almeno 3.");
            return;
        }
        if(!password.getText().equals(confirmPassword.getText())) {
            errorMessage.setText("La password non corrisponde con quella di conferma.");
            return;
        }
            
        Gson gson = new Gson();
        AuthRequest request = new AuthRequest(email.getText(), password.getText());
                
        disableInputs();
        errorMessage.setText("Registrazione in corso...");
        
        Task task = new Task<Void>() {
            @Override public Void call() {       
                try {
                    HttpResponse response = HttpConnector.postRequest("/auth/register", gson.toJson(request));
                    String responseBody = response.getResponseBody();
                    int responseCode = response.getResponseCode();

                    Platform.runLater(() -> {
                        if (200 <= responseCode && responseCode <= 299)
                            try {
                                App.setRoot("accedi");
                            }
                            catch (IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        else {
                                enableInputs();
                                errorMessage.setText(responseBody);
                        }
                    });
                }
                catch (IOException ioe) {
                    logger.error(ioe);
                    enableInputs();
                    errorMessage.setText("Connessione fallita, riprovare più tardi.");
                }
                return null;
            }
        }; 
        new Thread(task).start();
    }
    
    @FXML
    private void openAccedi() {
        try {
            App.setRoot("accedi");  
        }
        catch (IOException ioe) {
            logger.error(ioe);
        }  
    }
    
    private void enableInputs() {
        buttonRegistrati.setDisable(false);
        email.setDisable(false);
        password.setDisable(false);
        confirmPassword.setDisable(false);
    }
    
    private void disableInputs() {
        buttonRegistrati.setDisable(true);
        email.setDisable(true);
        password.setDisable(true);
        confirmPassword.setDisable(true);
    }
}
