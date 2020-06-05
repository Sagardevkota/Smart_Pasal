package com.example.smartpasal.model;

public class Carts {


    private Integer id;

    private Integer user_id;

    private Integer product_id;

    public Carts() {
    }

    public Carts(Integer user_id, Integer product_id) {
        this.user_id = user_id;
        this.product_id = product_id;
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

