package com.example.smartpasal.model;

public class ProductItems {

    private String productName;

    private String picture_path;
    private int productId;
    private  String price;
    private String brand;
    private String desc;
    private String sku;
    private int discount;
    private String category;
    private String type;
    private int stock;

    private Integer seller_id;

    private String rating;


    //for news details
    public ProductItems(String productName, String picture_path, int productId, String price, String brand, String desc, String sku,Integer discount,Integer stock) {

        this.productName = productName;
        this.picture_path = picture_path;
        this.price=price;
        this.productId = productId;
        this.brand = brand;
        this.desc = desc;
        this.sku = sku;
        this.discount=discount;
        this.stock=stock;

    }



    public ProductItems(String productName, String picture_path, int productId, String price,Integer discount,Integer stock,Integer seller_id) {
        this.productName = productName;
        this.picture_path = picture_path;
        this.productId = productId;
        this.price=price;
        this.discount=discount;
        this.stock=stock;
        this.seller_id=seller_id;
    }

    public ProductItems(ProductItems p) {
        this.productName = p.getProductName();
        this.picture_path = p.getPicture_path();
        this.productId = p.getProductId();
        this.price=p.getPrice();
        this.brand = p.getBrand();
        this.desc = p.getDesc();
        this.sku = p.getSku();
        this.discount=p.getDiscount();
        this.stock=p.getStock();
        this.seller_id=p.getSeller_id();
        this.rating=p.getRating();
        this.type=p.getType();
        this.category=p.getCategory();


    }

    public Integer getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(Integer seller_id) {
        this.seller_id = seller_id;
    }

    public String getPicture_path() {
        return picture_path;
    }



    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return "ProductItems{" +
                "productName='" + productName + '\'' +
                ", picture_path='" + picture_path + '\'' +
                ", productId=" + productId +
                ", price='" + price + '\'' +
                ", brand='" + brand + '\'' +
                ", desc='" + desc + '\'' +
                ", sku='" + sku + '\'' +
                ", discount=" + discount +
                ", stock=" + stock +
                '}';
    }



    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

