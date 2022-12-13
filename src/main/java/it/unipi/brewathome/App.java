package it.unipi.brewathome;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import javafx.scene.control.Control;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static boolean canResize = false;
    
    @Override
    public void start(Stage stage) throws IOException {        
        Parent initialPage = loadFXML("accedi");
        scene = new Scene(initialPage);
        String css = this.getClass().getResource("/styles/accedi.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
                
        // listener sul cambio di root
        ChangeListener<Parent> listener = (observable, oldValue, newValue) -> {
            if(canResize) {
                stage.setResizable(true);
                stage.setMaximized(true);
                putSizeListener(stage, newValue);
            }
            else {
                System.out.println(oldValue.prefWidth(0));
                System.out.println(newValue.prefWidth(0));
                stage.sizeToScene();
            }
        };
        scene.rootProperty().addListener(listener);
        
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

    public static void main(String[] args) {
        launch();
    }

}