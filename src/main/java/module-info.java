module it.unipi.brewathome {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;
    requires java.sql;
    requires org.mybatis;
    requires org.apache.logging.log4j;
    
    opens it.unipi.brewathome.connection.data to com.google.gson, javafx.base;
    opens it.unipi.brewathome to javafx.fxml;
    opens it.unipi.brewathome.controllers to javafx.fxml;
    exports it.unipi.brewathome;
}
