package com.example.smartpasal;

public class ProductsAdapterItems {



    public  String tvName;

    public   String picture_path;

    public String user_id;

    public String marked_price;
    public String fixed_price;

    public String brand;
    public String desc;
    public String sku;


    //for news details
    ProductsAdapterItems( String tvName, String picture_path ,String user_id,String marked_price,String fixed_price,String brand, String desc,String sku)
    {

        this. tvName=tvName;

        this. picture_path=picture_path;

        this.user_id=user_id;
        this.marked_price=marked_price;
        this.fixed_price=fixed_price;
        this.brand=brand;
        this.desc=desc;
        this.sku=sku;

    }





}
