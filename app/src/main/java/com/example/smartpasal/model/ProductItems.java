package com.example.smartpasal.model;



public class ProductItems {

    public String productName;

    public String picture_path;

    public int productId;

    public String marked_price;
    public String fixed_price;

    public String brand;
    public String desc;
    public String sku;

    public ProductItems() {
    }

    //for news details
    public ProductItems(String productName, String picture_path, int productId, String marked_price, String fixed_price, String brand, String desc, String sku) {

        this.productName = productName;

        this.picture_path = picture_path;

        this.productId = productId;
        this.marked_price = marked_price;
        this.fixed_price = fixed_price;
        this.brand = brand;
        this.desc = desc;
        this.sku = sku;

    }

    public ProductItems(String productName, String picture_path, int productId, String marked_price, String fixed_price) {
        this.productName = productName;
        this.picture_path = picture_path;
        this.productId = productId;
        this.marked_price = marked_price;
        this.fixed_price = fixed_price;
    }

    public ProductItems(ProductItems p) {
        this.productName = p.getProductName();

        this.picture_path = p.getPicture_path();

        this.productId = p.getProductId();
        this.marked_price = p.getMarked_price();
        this.fixed_price = p.getFixed_price();
        this.brand = p.getBrand();
        this.desc = p.getDesc();
        this.sku = p.getSku();

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

    public String getMarked_price() {
        return marked_price;
    }

    public void setMarked_price(String marked_price) {
        this.marked_price = marked_price;
    }

    public String getFixed_price() {
        return fixed_price;
    }

    public void setFixed_price(String fixed_price) {
        this.fixed_price = fixed_price;
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
                ", marked_price='" + marked_price + '\'' +
                ", fixed_price='" + fixed_price + '\'' +
                ", brand='" + brand + '\'' +
                ", desc='" + desc + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }
}

