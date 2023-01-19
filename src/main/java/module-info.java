module it.brewathome.brewathome {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;
    requires java.sql;
    requires org.mybatis;
    requires org.apache.logging.log4j;
    
    opens it.unipi.brewathome.connection.requests to com.google.gson;
    opens it.unipi.brewathome to javafx.fxml;
    opens it.unipi.brewathome.controllers to javafx.fxml;
    opens it.unipi.brewathome.connection.responses to javafx.base;
    exports it.unipi.brewathome;
}
