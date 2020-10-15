package com.example.smartpasal.model;


public class OrderResponse {


    private Integer order_id;
    private Integer product_id;
    private String product_color;
    private Float product_size;
    private Integer price;
    private Integer quantity;
    private String ordered_date;
    private String delivered_date;
    private String delivery_address;
    private String status;

    //productsInfo
    private String product_name;
    private Integer discount;
    private String picture_path;



    public OrderResponse(Integer order_id, Integer product_id, String product_color, Float product_size, Integer price, Integer quantity, String ordered_date, String delivered_date, String delivery_address, String status, String product_name, Integer discount, String picture_path) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.product_color = product_color;
        this.product_size = product_size;
        this.price = price;
        this.quantity = quantity;
        this.ordered_date = ordered_date;
        this.delivered_date = delivered_date;
        this.delivery_address = delivery_address;
        this.status = status;
        this.product_name = product_name;
        this.discount = discount;
        this.picture_path = picture_path;
    }

    public OrderResponse(OrderResponse o) {
        this.order_id = o.getOrder_id();
        this.product_id =o.getProduct_id();
        this.product_color = o.getProduct_color();
        this.product_size = o.getProduct_size();
        this.price = o.getPrice();
        this.quantity = o.getQuantity();
        this.ordered_date = o.getOrdered_date();
        this.delivered_date = o.getDelivered_date();
        this.delivery_address = o.getDelivery_address();
        this.status = o.getStatus();
        this.product_name = o.getProduct_name();
        this.discount = o.getDiscount();
        this.picture_path = o.getPicture_path();
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public Float getProduct_size() {
        return product_size;
    }

    public void setProduct_size(Float product_size) {
        this.product_size = product_size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOrdered_date() {
        return ordered_date;
    }

    public void setOrdered_date(String ordered_date) {
        this.ordered_date = ordered_date;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "order_id=" + order_id +
                ", product_id=" + product_id +
                ", product_color='" + product_color + '\'' +
                ", product_size=" + product_size +
                ", price=" + price +
                ", quantity=" + quantity +
                ", ordered_date='" + ordered_date + '\'' +
                ", delivered_date='" + delivered_date + '\'' +
                ", delivery_address='" + delivery_address + '\'' +
                ", status='" + status + '\'' +
                ", product_name='" + product_name + '\'' +
                ", discount=" + discount +
                ", picture_path='" + picture_path + '\'' +
                '}';
    }
}

