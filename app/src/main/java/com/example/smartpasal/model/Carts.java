package com.example.smartpasal.model;

import java.util.Locale;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Carts {
    private Integer id;
    private Integer user_id;
    private Integer product_id;
    private String color;
    private Float size;
    private String  price;

    public Carts() {
    }

    public Carts(Integer user_id, Integer product_id, String price,String color, Float size) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.color = color;
        this.price=price;
        this.size = size;
    }


    public Carts(Integer user_id, Integer product_id) {
        this.user_id = user_id;
        this.product_id = product_id;
    }




}

