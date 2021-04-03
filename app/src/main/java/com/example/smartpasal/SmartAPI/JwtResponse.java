package com.example.smartpasal.SmartAPI;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private final String jwt;
    private final String status;
    private final String message;
    private final String  role;

}
