package com.example.smartpasal.model;

import lombok.Data;

@Data
public class CartResponse {

    private int productId;
    private String productName;
    private String desc;
    private String category;
    private String brand;
    private String sku;
    private String type;
    private String picturePath;
    private Integer discount;
    private Integer stock;
    private String color;
    private float size;
    private String price;

    public CartResponse(int productId, String productName, String desc, String price, String category, String brand, String sku, String type, String picture_path, Integer discount, Integer stock, String color, float size) {
        this.productId = productId;
        this.productName = productName;
        this.desc = desc;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.sku = sku;
        this.type = type;
        this.picturePath = picture_path;
        this.discount = discount;
        this.stock = stock;
        this.color = color;
        this.size = size;
    }


}
