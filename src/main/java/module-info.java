module it.brewathome.brewathome {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;
    
    opens it.unipi.brewathome to javafx.fxml;
    exports it.unipi.brewathome;
}
