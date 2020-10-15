package com.example.smartpasal.SmartAPI;

public class JwtResponse {
    private final String jwt;
    private String status;
    private String message;
    private String  role;

    public JwtResponse(String jwt, String status, String message, String role) {
        this.jwt = jwt;
        this.status = status;
        this.message = message;
        this.role=role;
    }

    public String getStatus() {
        return status;
    }



    public String getMessage() {
        return message;
    }




    public String getRole() {
        return role;
    }



    public String getJwt() {
        return jwt;
    }
}
