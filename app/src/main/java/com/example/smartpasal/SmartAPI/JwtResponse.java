package com.example.smartpasal.SmartAPI;

public class JwtResponse {
    private final String jwt;
    private String status;
    private String message;

    public JwtResponse(String jwt, String status, String message) {
        this.jwt = jwt;
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getJwt() {
        return jwt;
    }
}
