package com.example.smartpasal.model;

import lombok.Data;

@Data
public class OrderResponse {


    private Integer orderId;
    private Integer productId;
    private String color;
    private Float size;
    private Integer price;
    private Integer quantity;
    private String orderedDate;
    private String deliveredDate;
    private String deliveryAddress;
    private String status;

    //productsInfo
    private String productName;
    private Integer discount;
    private String picturePath;


}

