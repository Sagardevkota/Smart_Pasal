package com.example.smartpasal.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
@Builder
public class Orders {

    private String productName;
    private Integer orderId;
    private Integer productId;
    private Integer userId;
    private String productColor;
    private Float productSize;
    private int discount;
    private Integer price;
    private Integer quantity;
    private String orderedDate;
    private String deliveredDate;
    private String deliveryAddress;
    private String status;
    private String picturePath;



}
