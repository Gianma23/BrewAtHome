/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.brewathome.http;

/**
 *
 * @author Utente
 */
public class HttpResponse {
    
    private int responseCode;
    private String responseBody;
    private String responseHeader;

    public HttpResponse(int responseCode, String responseBody, String responseHeader) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseHeader = responseHeader;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }
    
}
