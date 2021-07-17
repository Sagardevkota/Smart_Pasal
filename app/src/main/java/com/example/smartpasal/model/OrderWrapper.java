package com.example.smartpasal.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OrderWrapper {

    private List<Orders> orders = new ArrayList<>();
}
