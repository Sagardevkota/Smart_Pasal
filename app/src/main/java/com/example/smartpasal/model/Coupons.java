package com.example.smartpasal.model;

public class Coupons {

    private Integer id;
    private Integer product_id;
    private String coupon_code;
    private Integer discount;

    public Coupons() {
    }

    public Coupons(Integer product_id, String coupon_code) {
        this.product_id = product_id;
        this.coupon_code = coupon_code;
    }

    public Coupons(Integer product_id, String coupon_code, Integer discount) {
        this.product_id = product_id;
        this.coupon_code = coupon_code;
        this.discount = discount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
