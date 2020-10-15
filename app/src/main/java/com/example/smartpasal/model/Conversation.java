package com.example.smartpasal.model;

public class Conversation {
    private String message;
    private Integer asker;
    private String date;
    private Integer productId;

    public Conversation(String message, Integer asker, String date,Integer productId) {
        this.message = message;
        this.asker = asker;
        this.date = date;
        this.productId=productId;
    }

    public Conversation(Conversation c){
        this.message=c.getMessage();
        this.asker=c.getAsker();
        this.date=c.getDate();
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAsker() {
        return asker;
    }

    public void setAsker(Integer asker) {
        this.asker = asker;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
