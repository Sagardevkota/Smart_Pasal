package com.example.smartpasal.model;


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

    public Carts(Integer user_id, Integer product_id,String price) {
        this.user_id = user_id;
        this.product_id = product_id;
    }

    public Carts(Integer user_id, Integer product_id, String color,String price) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.color = color;
    }

    public Carts(Integer user_id, Integer product_id, Float size,String price) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.size = size;
    }

    public Carts(Integer user_id, Integer product_id) {
        this.user_id = user_id;
        this.product_id = product_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
}

