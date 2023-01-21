package it.unipi.brewathome.connection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DatabaseConnector {
    
    private static final Logger logger =LogManager.getLogger(DatabaseConnector.class.getName());
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gianmaria_saggini";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private DatabaseConnector() {
    }
    
    public static void createTables() {
        try (Connection co = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             BufferedReader reader = new BufferedReader(new FileReader(DatabaseConnector.class.getResource("/mysql/tables.sql").getPath()));) 
        {  
            ScriptRunner sr = new ScriptRunner(co);
            sr.runScript(reader);
        }
        catch (IOException | SQLException ioe) {
            logger.error(ioe.getMessage());
        }
    }
    
    public static void populateStyleTable() {
        
        //parse del file
        try (Connection co = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = co.prepareStatement("INSERT INTO stile VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");)
        {
            //controllo che la tabella sia vuota
            Statement st = co.createStatement();
            ResultSet result = st.executeQuery("SELECT EXISTS (SELECT 1 FROM stile);");
            result.next();
            if(result.getInt(1)==1)
                return;
            
            Path path = Path.of(DatabaseConnector.class.getResource("/bjcp-2021.json").toString().substring(6));
            String jsonFile = Files.readString(path);

            Gson gson = new Gson();
            JsonObject json = gson.fromJson(jsonFile, JsonObject.class);
            JsonArray categorie = json.get("styleguide").getAsJsonObject().get("category").getAsJsonArray();
            for(JsonElement categoria : categorie) {

                JsonArray sottocategorie = categoria.getAsJsonObject().get("subcategory").getAsJsonArray();
                for(JsonElement sottocategoria : sottocategorie) {

                    JsonObject stile = sottocategoria.getAsJsonObject();
                    JsonObject stats = stile.get("statistics").getAsJsonObject();
                    
                    ps.setString(1, stile.get("name").getAsString());
                    ps.setString(2, "bjcp-2021");
                    ps.setDouble(3, stats.get("abv").getAsJsonObject().get("min").getAsDouble());
                    ps.setDouble(4, stats.get("abv").getAsJsonObject().get("max").getAsDouble());
                    ps.setDouble(5, stats.get("og").getAsJsonObject().get("min").getAsDouble());
                    ps.setDouble(6, stats.get("og").getAsJsonObject().get("max").getAsDouble());
                    ps.setDouble(7, stats.get("fg").getAsJsonObject().get("min").getAsDouble());
                    ps.setDouble(8, stats.get("fg").getAsJsonObject().get("max").getAsDouble());
                    ps.setDouble(9, stats.get("srm").getAsJsonObject().get("min").getAsDouble());
                    ps.setDouble(10, stats.get("srm").getAsJsonObject().get("max").getAsDouble());
                    ps.setInt(11, stats.get("ibus").getAsJsonObject().get("min").getAsInt());
                    ps.setInt(12, stats.get("ibus").getAsJsonObject().get("max").getAsInt());
                    ps.executeUpdate();
                }
            }
        }
        catch (IOException | SQLException ioe) {
            logger.error(ioe.getMessage());
        }
    }
}