package it.unipi.brewathome;

import it.unipi.brewathome.connection.DatabaseConnector;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unipi.brewathome.connection.HttpConnector;
import it.unipi.brewathome.connection.data.HttpResponse;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class App extends Application {

    private static final Logger logger =LogManager.getLogger(App.class.getName());
    private static String token;
    private static Scene scene;
    private static boolean canResize = false;

    @Override
    public void start(Stage stage) {  
        
        // creo le tabelle e le popolo
        DatabaseConnector.createTables();
        DatabaseConnector.populateStyleTable();
        DatabaseConnector.populateAccount();
        DatabaseConnector.populateRicettawithIngredienti();
        
        try {
            // carico gli stili di colore css
            loadStyle();

            // aggiungo gli stili alla scena
            scene = new Scene(loadFXML("accedi"));
            App.addStyle(scene, "style.css");
            App.addStyle(scene, "fonts.css");
            App.addStyle(scene, "global.css");
        }
        catch (IOException ioe) {
            logger.error(ioe);
        }     
        
        // listener sul cambio di root
        scene.rootProperty().addListener((observable, oldValue, newValue) -> {
            if(canResize) {
                putSizeListener(stage, newValue);
                stage.setResizable(true);
            }
            else {
                stage.setResizable(false);
                stage.sizeToScene();
            }
        });
        
        stage.setScene(scene);
        stage.setTitle("Brew at Home");
        stage.getIcons().add(new Image(App.class.getResource("/img/luppoli_icon.png").toString()));
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();   
    }
    
    /* ======= UTILITA ======= */
    
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
            resizeColumns(stage, cols, colsWidth);
        };
        stage.widthProperty().addListener(stageSizeListener);
        resizeColumns(stage, cols, colsWidth);
    }
    
    private void resizeColumns(Stage stage, ObservableList<ColumnConstraints> cols, double[] colsWidth) {
        if(stage.getWidth() > 1800)
            for(ColumnConstraints col : cols) 
                col.setPercentWidth(-1);
        else {
            int j = 0;
            for(ColumnConstraints col : cols) {
                col.setPercentWidth(colsWidth[j++]);
            }
        }
    }
      
    public static void addStyle(Scene scene, String stylesheet) {
        String css = App.class.getResource("/styles/" + stylesheet).toExternalForm();
        scene.getStylesheets().add(css);
    }
    
    public static void addLeftBar(GridPane grid) throws IOException {
        FXMLLoader loadLeftBar = new FXMLLoader(App.class.getResource("leftBar.fxml"));
        Parent leftBar = loadLeftBar.load();
        grid.add(leftBar, 0, 0, 1, 1);
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void loadStyle() throws IOException {
        
        HttpResponse response = HttpConnector.getRequest("/style/colors", "");
        String responseBody = response.getResponseBody();
        
        /* Converto in json e produco il css */
        
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(responseBody, JsonElement.class);
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

        try (PrintWriter out = new PrintWriter(file)) {
            out.println(cssString);
        }
    }
    
    /* ================ GETTERS & SETTERS ================ */
    
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    public static void setToken(String token) {
        App.token = token;
    }
    
    public static String getToken() {
        return token;
    }
    
    public static void setCanResize(boolean canResize) {
        App.canResize = canResize;
    }
    
    public static void main(String[] args) {
        launch();
    }
}