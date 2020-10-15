package com.example.smartpasal.model;

public class Orders {

    private Integer orderId;

    private Integer productId;

    private Integer userId;

    private String productColor;

    private Float productSize;

    private Integer price;

    private Integer qty;

    private String orderedDate;

    private String delivered_date;

    private String deliveryAddress;

    private String status;

    public Orders(Integer productId, Integer userId, String productColor, Float productSize, Integer price, Integer qty, String orderedDate, String delivered_date, String deliveryAddress, String status) {
        this.productId = productId;
        this.userId = userId;
        this.productColor = productColor;
        this.productSize = productSize;
        this.price = price;
        this.qty = qty;
        this.orderedDate = orderedDate;
        this.delivered_date = delivered_date;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
    }

    public Orders(Integer userId, Integer productId, String productColor, Float productSize, Integer price, Integer qty, String orderedDate,  String deliveryAddress,String delivered_date) {
        this.productId = productId;
        this.userId = userId;
        this.productColor = productColor;
        this.productSize = productSize;
        this.price = price;
        this.qty = qty;
        this.orderedDate = orderedDate;
        this.deliveryAddress = deliveryAddress;
        this.delivered_date=delivered_date;


    }

    public Orders(String productName, Integer productId, String productColor, Float productSize, Integer price, Integer qty, String picture_path){
        this.productId = productId;
        this.productColor = productColor;
        this.productSize = productSize;
        this.price = price;
        this.qty = qty;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public Float getProductSize() {
        return productSize;
    }

    public void setProductSize(Float productSize) {
        this.productSize = productSize;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Orders{" +
                ", productId=" + productId +
                ", price=" + price +
                ", qty=" + qty +

                '}';
    }
}
