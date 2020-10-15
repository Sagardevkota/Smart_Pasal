package com.example.smartpasal.model;


public class SizeAttribute {


    private Integer id;

    private Integer product_id;

    private Float size;

    public SizeAttribute(Integer product_id, Float size) {
        this.product_id = product_id;
        this.size = size;
    }

    public SizeAttribute(SizeAttribute s){
        this.product_id=s.getProduct_id();
        this.size=s.getSize();
    }

    public SizeAttribute() {
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

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SizeAttribute{" +
                "id=" + id +
                ", product_id=" + product_id +
                ", size=" + size +
                '}';
    }
}
