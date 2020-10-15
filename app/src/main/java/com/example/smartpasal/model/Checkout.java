package com.example.smartpasal.model;

public class Checkout {
    private String productName;
    private Integer productId;
    private String productColor;
    private Float productSize;
    private Integer price;
    private Integer qty;
    private String productImage;

    public Checkout(String productName, Integer productId, String productColor, Float productSize, Integer price, Integer qty, String productImage) {
        this.productName = productName;
        this.productId = productId;
        this.productColor = productColor;
        this.productSize = productSize;
        this.price = price;
        this.qty = qty;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductColor() {
        return productColor;
    }

    public Float getProductSize() {
        return productSize;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQty() {
        return qty;
    }

    public String getProductImage() {
        return productImage;
    }
}
