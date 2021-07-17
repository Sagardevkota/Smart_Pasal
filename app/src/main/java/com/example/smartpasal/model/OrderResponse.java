package com.example.smartpasal.model;

import lombok.Data;

@Data
public class OrderResponse {


    private int orderId;
    private int productId;
    private String productColor;
    private float productSize;
    private int price;
    private int quantity;
    private String orderedDate;
    private String deliveredDate;
    private String deliveryAddress;
    private String status;

    //productsInfo
    private String productName;
    private int discount;
    private String picturePath;


}

