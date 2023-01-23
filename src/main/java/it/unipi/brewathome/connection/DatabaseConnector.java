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
    
    private static final Logger logger = LogManager.getLogger(DatabaseConnector.class.getName());
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gianmaria_saggini_615710";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private DatabaseConnector() {}
    
    public static void createTables() {
        try (Connection co = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             BufferedReader reader = new BufferedReader(new FileReader(DatabaseConnector.class.getResource("/mysql/tables.sql").getPath()));) 
        {  
            ScriptRunner sr = new ScriptRunner(co);
            sr.runScript(reader);
        }
        catch (IOException | SQLException ioe) {
            logger.error(ioe);
        }
    }
    
    public static void populateStyleTable() {
        
        //parse del file
        try (Connection co = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = co.prepareStatement("INSERT INTO stile VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");)
        {
            //controllo che la tabella sia vuota
            Statement st = co.createStatement();
            ResultSet result = st.executeQuery("SELECT EXISTS (SELECT 1 FROM stile);");
            result.next();
            if(result.getInt(1)==1)
                return;
            
            Path path = Path.of(DatabaseConnector.class.getResource("/json/bjcp-2021.json").toString().substring(6));
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
            logger.error(ioe);
        }
    }
    
    public static void populateAccount() {
        try (Connection co = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);)
        {
            //controllo che la tabella sia vuota
            Statement st = co.createStatement();
            ResultSet result = st.executeQuery("SELECT EXISTS (SELECT 1 FROM account);");
            result.next();
            if(result.getInt(1)==1)
                return;
            
            st.executeUpdate("INSERT INTO account VALUES ('admin@email.com', 123);");
        }
        catch (SQLException ioe) {
            logger.error(ioe);
        }
    }
    
    public static void populateRicettawithIngredienti() {
        
        //parse del file
        try (Connection co = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement psFerm = co.prepareStatement("INSERT INTO fermentabile VALUES (?,?,?,?,?,?,?,?,?,?,?);");
             PreparedStatement psLup = co.prepareStatement("INSERT INTO luppolo VALUES (?,?,?,?,?,?,?,?,?);");)
        {
            //controllo che la tabella sia vuota
            Statement st = co.createStatement();
            ResultSet result = st.executeQuery("SELECT EXISTS (SELECT 1 FROM ricetta);");
            result.next();
            if(result.getInt(1)==1)
                return;
        
            // popolo ricetta
            st.executeUpdate("INSERT INTO ricetta (id, account_id, nome, descrizione, autore, tipo, stile_id, volume, rendimento)"
                           + " VALUES (1, 'admin@email.com', 'Birra rossa', 'Ricetta per provare', 'gianmaria saggini', 'Ammostamento Parziale', 'Irish Red Ale', 23.0, 71.9);");
            
            Path pathFerm = Path.of(DatabaseConnector.class.getResource("/json/fermentabili.json").toString().substring(6));
            String jsonFerm = Files.readString(pathFerm);
            Path pathLup = Path.of(DatabaseConnector.class.getResource("/json/luppoli.json").toString().substring(6));
            String jsonLup = Files.readString(pathLup);
            
            Gson gson = new Gson();
            JsonArray fermentabili = gson.fromJson(jsonFerm, JsonArray.class);
            for(JsonElement fermentabileEl : fermentabili) {
                
                JsonObject fermentabile = fermentabileEl.getAsJsonObject();
                
                psFerm.setInt(1, fermentabile.get("id").getAsInt());
                psFerm.setInt(2, fermentabile.get("ricettaId").getAsInt());
                psFerm.setString(3, fermentabile.get("nome").getAsString());
                psFerm.setInt(4, fermentabile.get("quantita").getAsInt());
                psFerm.setString(5, fermentabile.get("categoria").getAsString());
                psFerm.setString(6, fermentabile.get("fornitore").getAsString());
                psFerm.setString(7, fermentabile.get("provenienza").getAsString());
                psFerm.setString(8, fermentabile.get("tipo").getAsString());
                psFerm.setInt(9, fermentabile.get("colore").getAsInt());
                psFerm.setDouble(10, fermentabile.get("potenziale").getAsDouble());
                psFerm.setDouble(11, fermentabile.get("rendimento").getAsDouble());
                psFerm.executeUpdate();
            }
            
            JsonArray luppoli = gson.fromJson(jsonLup, JsonArray.class);
            for(JsonElement luppoloEl : luppoli) {

                JsonObject luppolo = luppoloEl.getAsJsonObject();

                psLup.setInt(1, luppolo.get("id").getAsInt());
                psLup.setInt(2, luppolo.get("ricettaId").getAsInt());
                psLup.setInt(3, luppolo.get("quantita").getAsInt());
                psLup.setInt(4, luppolo.get("tempo").getAsInt());
                psLup.setString(5, luppolo.get("nome").getAsString());
                psLup.setString(6, luppolo.get("fornitore").getAsString());
                psLup.setString(7, luppolo.get("provenienza").getAsString());
                psLup.setString(8, luppolo.get("tipo").getAsString());
                psLup.setDouble(9, luppolo.get("alpha").getAsDouble());
                psLup.executeUpdate();
            }
        }
        catch (IOException | SQLException ioe) {
            logger.error(ioe);
        }
    }
}