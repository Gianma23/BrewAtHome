/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.brewathome.http;

import it.unipi.brewathome.App;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Utente
 */
public class HttpConnector {
    
    private static final String BASE_URL = "http://localhost:8080";
    
    public static HttpResponse postRequest(String uri, String urlParameters) throws IOException {
        String url = App.BASE_URL + uri;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestMethod("POST");
        httpClient.setUseCaches(false);
        
        // invio richiesta
        
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending POST request to URL : " + url);
        System.out.println("header : " + httpClient.getHeaderField("Authorization"));
        System.out.println("parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        
        // lettura risposta
        
        InputStream inputStream;
        String responseHeader = "";
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = httpClient.getInputStream();
            responseHeader = httpClient.getHeaderField("Authorization");
        } else {
            inputStream = httpClient.getErrorStream();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null) 
            response.append(currentLine);

        in.close();
        
        return new HttpResponse(responseCode, response.toString(), responseHeader);
    }
    
    public static HttpResponse getRequest(String uri, String urlParameters) throws IOException {
        String url = App.BASE_URL + uri + "?" + urlParameters;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestMethod("GET");
        httpClient.setUseCaches(false);
        
        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending GET request to URL : " + url);
        System.out.println("parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        
        // lettura risposta
        
        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = httpClient.getInputStream();
        } else {
            inputStream = httpClient.getErrorStream();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null) 
            response.append(currentLine);

        in.close();
        
        return new HttpResponse(responseCode, response.toString(), "");
    }
    
    public static HttpResponse getRequestWithToken(String uri, String urlParameters, String token) throws IOException {
        String url = App.BASE_URL + uri + "?" + urlParameters;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestProperty("Authorization", token);
        httpClient.setRequestMethod("GET");
        httpClient.setUseCaches(false);
 
        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending GET request to URL : " + url);
        System.out.println("parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        
        // lettura risposta
        
        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = httpClient.getInputStream();
        } else {
            inputStream = httpClient.getErrorStream();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null) 
            response.append(currentLine);

        in.close();

        return new HttpResponse(responseCode, response.toString(), "");
    }
    
    public static HttpResponse putRequestWithToken(String uri, String urlParameters, String token) throws IOException {
        String url = App.BASE_URL + uri;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestProperty("Authorization", token);
        httpClient.setRequestMethod("PUT");
        httpClient.setUseCaches(false);
        
        // invio richiesta
        
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
 
        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending PUT request to URL : " + url);
        System.out.println("parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        
        return new HttpResponse(responseCode, "", "");
    }
    
        public static HttpResponse postRequestWithToken(String uri, String urlParameters, String token) throws IOException {
        String url = App.BASE_URL + uri;

        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        httpClient.setRequestProperty("Authorization", token);
        httpClient.setRequestMethod("POST");
        httpClient.setUseCaches(false);
 
         // invio richiesta
        
        httpClient.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
            wr.writeBytes(urlParameters);
            wr.flush();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        
        int responseCode = httpClient.getResponseCode();
        System.out.println("\nSending POST request to URL : " + url);
        System.out.println("parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        
        // lettura risposta
        
        InputStream inputStream;
        if (200 <= responseCode && responseCode <= 299) {
            inputStream = httpClient.getInputStream();
        } else {
            inputStream = httpClient.getErrorStream();
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder response = new StringBuilder();
        String currentLine;

        while ((currentLine = in.readLine()) != null) 
            response.append(currentLine);

        in.close();

        return new HttpResponse(responseCode, response.toString(), "");
    }
}
