module it.brewathome.brewathome {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;
    requires java.sql;
    requires org.mybatis;
    
    opens it.unipi.brewathome to javafx.fxml;
    exports it.unipi.brewathome;
}
