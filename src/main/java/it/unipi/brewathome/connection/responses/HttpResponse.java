package it.unipi.brewathome.connection.responses;

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
