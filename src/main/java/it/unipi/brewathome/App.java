package it.unipi.brewathome;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static boolean canResize = false;
    private static final String BASE_URL= "http://localhost:8080";
    private static final String DB_NAME = "gianmaria_saggini";
    
    @Override
    public void start(Stage stage) throws IOException {  
            
        // mi connnetto al database e creo le tabelle
        DatabaseConnector dbConnector = new DatabaseConnector(DB_NAME);
        dbConnector.createTables();
        
        // carico gli stili di colore css
        loadStyle();
        
        scene = new Scene(loadFXML("accedi"));
        App.addStyle(scene, "style.css");
        scene.getStylesheets().add(getClass().getResource("/styles/fonts.css").toExternalForm());
                     
        // listener sul cambio di root
        ChangeListener<Parent> listener = (observable, oldValue, newValue) -> {
            if(canResize) {
                stage.setResizable(true);
                stage.setMaximized(true);
                putSizeListener(stage, newValue);
            }
            else {
                stage.sizeToScene();
            }
        };
        scene.rootProperty().addListener(listener);
        
        stage.setScene(scene);
        stage.setTitle("Brew at Home");
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();   
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    static void setCanResize(boolean canResize) {
        App.canResize = canResize;
    }
    
    private void putSizeListener(Stage stage, Parent newValue) {      
        GridPane grid = (GridPane) newValue;
        ObservableList<ColumnConstraints> cols = grid.getColumnConstraints();

        int i = 0;
        double[] colsWidth = new double[25];
        for(ColumnConstraints col : cols) {
            colsWidth[i++] = col.getPercentWidth();
        }

        // listener sul cambio di dimensione della finestra
        ChangeListener<Number> stageSizeListener = (obv, oldv, newv) -> {

            if(stage.getWidth() > 1800)
                for(ColumnConstraints col : cols) 
                    col.setPercentWidth(-1);
            else {
                int j = 0;
                for(ColumnConstraints col : cols) {
                    col.setPercentWidth(colsWidth[j++]);
                }
            }
        };
        stage.widthProperty().addListener(stageSizeListener);
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void addStyle(Scene scene, String stylesheet) {
        String css = App.class.getResource("/styles/" + "style.css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    private void loadStyle() throws IOException {
        HttpURLConnection httpClient = (HttpURLConnection) new URL(BASE_URL + "/style/colors").openConnection();
        httpClient.setRequestMethod("GET");
        
        /* Prendo la stringa json dal servizio */
        
        String inputLine;
        StringBuffer content = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()));
        while((inputLine = in.readLine()) != null)
            content.append(inputLine);
        in.close();
        
        /* Converto in json e produco il css */
        
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
        JsonArray colors = json.getAsJsonObject().get("entities").getAsJsonArray();
        
        StringBuilder cssString = new StringBuilder("* {\n");
        for(JsonElement color : colors) {
            JsonObject colorObj = color.getAsJsonObject();
            String colorId = "-" + colorObj.get("id")
                    .getAsString().replaceAll("\\.","-");
            String colorValue = colorObj.get("value").getAsString();
            if(colorValue.substring(0,1).equals("{"))
                colorValue = "-" + colorValue.replaceAll("\\.", "-").substring(1, colorValue.length() - 1);
                
            cssString.append(colorId).append(": ").append(colorValue).append(";\n");
        }
        cssString.append("}");
        
        /* Salvo il file style.css creato nella cartella style */
        
        File file = new File(this.getClass().getResource("/styles").getPath(), "style.css");

        PrintWriter out = new PrintWriter(file);
        out.println(cssString);
        out.close();
    }
    
    public static void main(String[] args) {
        launch();
    }

}