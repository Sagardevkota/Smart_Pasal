package com.example.smartpasal.model;




public class CartResponse {

    private int productId;

    private String productName;

    private String desc;
    private String category;

    private String brand;

    private String sku;

    private String type;


    private String picture_path;


    private Integer discount;


    private Integer stock;

    private String color;

    private float size;

    private String price;

    public CartResponse(int productId, String productName, String desc, String price, String category, String brand, String sku, String type, String picture_path, Integer discount, Integer stock, String color, float size) {
        this.productId = productId;
        this.productName = productName;
        this.desc = desc;
        this.price=price;
        this.category = category;
        this.brand = brand;
        this.sku = sku;
        this.type = type;
        this.picture_path = picture_path;
        this.discount = discount;
        this.stock = stock;
        this.color = color;
        this.size = size;
    }

    public CartResponse(CartResponse c){
        this.productId = c.getProductId();
        this.productName = c.getProductName();
        this.desc = c.getDesc();
        this.price=c.getPrice();
        this.category = c.getCategory();
        this.brand = c.getBrand();
        this.sku = c.getSku();
        this.type = c.getType();
        this.picture_path = c.getPicture_path();
        this.discount = c.getDiscount();
        this.stock = c.getStock();
        this.color = c.getColor();
        this.size = c.getSize();

    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
