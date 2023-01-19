/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.brewathome.connection;

import it.unipi.brewathome.connection.responses.HttpResponse;
import it.unipi.brewathome.controllers.RicetteController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Utente
 */
public class HttpConnector {
    
    private static final Logger logger =LogManager.getLogger(RicetteController.class);
    private static final String BASE_URL = "http://localhost:8080";
    
        public static HttpResponse getRequest(String uri, String urlParameters) throws IOException {
        String url = BASE_URL + uri + "?" + urlParameters;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestMethod("GET");
        
        // lettura risposta
        
        int responseCode = httpClient.getResponseCode();
        String responseBody = input(httpClient, responseCode);
        
        printConnection("GET", url, urlParameters, responseCode);
        return new HttpResponse(responseCode, responseBody, "");
    }
    
    public static HttpResponse getRequestWithToken(String uri, String urlParameters, String token) throws IOException {
        String url = BASE_URL + uri + "?" + urlParameters;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestProperty("Authorization", token);
        httpClient.setRequestMethod("GET");
 
        // lettura risposta
        
        int responseCode = httpClient.getResponseCode();
        String responseBody = input(httpClient, responseCode);
        
        printConnection("GET", url, urlParameters, responseCode);
        return new HttpResponse(responseCode, responseBody, "");
    }
    
    public static HttpResponse postRequest(String uri, String urlParameters) throws IOException {
        String url = BASE_URL + uri;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestProperty("Content-Type", "application/json");
        httpClient.setRequestMethod("POST");
        
        // invio richiesta
        
        output(httpClient, urlParameters);      
        
        // lettura risposta
        
        int responseCode = httpClient.getResponseCode();
        String responseBody = input(httpClient, responseCode);
        String responseHeader = httpClient.getHeaderField("Authorization");
        
        printConnection("POST", url, urlParameters, responseCode);
        return new HttpResponse(responseCode, responseBody, responseHeader);
    }
       
    public static HttpResponse postRequestWithToken(String uri, String urlParameters, String token) throws IOException {
        String url = BASE_URL + uri;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestProperty("Authorization", token);
        httpClient.setRequestProperty("Content-Type", "application/json");
        httpClient.setRequestMethod("POST");

        // invio richiesta
        
        output(httpClient, urlParameters);
        
        // lettura risposta
        
        int responseCode = httpClient.getResponseCode();
        String responseBody = input(httpClient, responseCode);

        printConnection("POST", url, urlParameters, responseCode);
        return new HttpResponse(responseCode, responseBody, "");
    }
    
    public static HttpResponse putRequestWithToken(String uri, String urlParameters, String token) throws IOException {
        String url = BASE_URL + uri;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestProperty("Authorization", token);
        httpClient.setRequestMethod("PUT");
        
        // invio richiesta
        
        output(httpClient, urlParameters);
        
        // lettura risposta
        
        int responseCode = httpClient.getResponseCode();
        String responseBody = input(httpClient, responseCode);
        
        printConnection("PUT", url, urlParameters, responseCode);
        return new HttpResponse(responseCode, responseBody, "");
    }
        
    // =============== METODI DI UTILITA ===============    
        
    private static String input(HttpURLConnection httpClient, int responseCode) {
        
        StringBuilder response = new StringBuilder();
        
        try(InputStream inputStream = 
                (200 <= responseCode && responseCode <= 299) ? httpClient.getInputStream() : httpClient.getErrorStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));) {

            String currentLine;

            while ((currentLine = in.readLine()) != null) 
                response.append(currentLine);
        }
        catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
        return response.toString();
    }
        
    private static void output(HttpURLConnection httpClient, String parameters) {
        
        httpClient.setDoOutput(true);
        try (OutputStreamWriter wr = new OutputStreamWriter(httpClient.getOutputStream())) {
            
            wr.write(parameters);
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private static void printConnection(String type, String url, String urlParameters, int responseCode) {
        logger.info("Sending " + type + " request to URL : " + url);
        logger.info("parameters : " + urlParameters);
        logger.info("Response Code : " + responseCode);
    }
}
