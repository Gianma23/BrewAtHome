/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package it.unipi.brewathome;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class RicetteController implements Initializable {

    @FXML private GridPane grid;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("topBar.fxml"));
            Parent bar = fxmlLoader.load();

            grid.add(bar, 0, 0, 25, 1);
            grid.add(infrastructurePane(), 1, 1, 23, 1);
        }
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }    
    
    public ScrollPane infrastructurePane() {

            final FlowPane flow = new FlowPane();
            flow.setVgap(24);
            flow.setHgap(24);
            flow.setAlignment(Pos.CENTER);
            //FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("content_grid.fxml"));
            //GridPane contentGrid = fxmlLoader.load();
            //contentGrid.add(flow, 0, 0, 23, 1);
            
            final ScrollPane scroll = new ScrollPane();
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);    // Horizontal scroll bar
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);    // Vertical scroll bar
            scroll.setContent(flow);
        
            flow.setPrefWidth(scroll.getWidth()-10);
            ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                flow.setPrefWidth(scroll.getWidth()-10);
            };
            grid.widthProperty().addListener(stageSizeListener);
            
            flow.setStyle("-fx-background-color: red;");

            for (int i = 0; i < 28; i++) {
                flow.getChildren().add(generateRectangle());
            }
            return scroll;
    }
    
    public Rectangle generateRectangle() {

        Rectangle rect2 = new Rectangle(10, 10, 10, 10);
        rect2.setId("app");
        rect2.setArcHeight(8);
        rect2.setArcWidth(8);
        //rect2.setX(10);
        //rect2.setY(160);
        rect2.setStrokeWidth(1);
        rect2.setStroke(Color.WHITE);
        rect2.setWidth(320);
        rect2.setHeight(180);

        return rect2;
    }
}
