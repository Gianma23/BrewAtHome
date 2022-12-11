package it.unipi.brewathome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    
    @Override
    public void start(Stage stage) throws IOException {
        
        scene = new Scene(loadFXML("accedi"), 1000, 700);
        String css = this.getClass().getResource("/styles/accedi.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Brew at Home");
        
        // listener sul cambio di root
        ChangeListener<Parent> listener = (observable, oldValue, newValue) -> {
            putSizeListener(stage, newValue);
        };
        scene.rootProperty().addListener(listener);
        
        //size listener to first page
        putSizeListener(stage, scene.getRoot());
        stage.setScene(scene);
        stage.show();   
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
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
                    System.out.println("set");
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

    public static void main(String[] args) {
        launch();
    }

}