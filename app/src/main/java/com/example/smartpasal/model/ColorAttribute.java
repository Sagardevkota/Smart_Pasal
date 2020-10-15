package com.example.smartpasal.model;


public class ColorAttribute {


    private Integer id;

    private Integer product_id;

    private String color;

    public ColorAttribute(Integer product_id, String color) {
        this.product_id = product_id;
        this.color = color;
    }

    public ColorAttribute() {
    }

    public ColorAttribute(ColorAttribute c) {
        this.color=c.getColor();
        this.product_id=c.getProduct_id();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ColorAttribute{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", color='" + color + '\'' +
                '}';
    }
}
