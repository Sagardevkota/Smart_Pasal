package com.example.smartpasal.model;

public class SliderItems {
    public String tvName;
    public String picture_path;
    public String user_id;

    public SliderItems( String tvName,String picture_path,String user_id){
        this.tvName=tvName;
        this.picture_path=picture_path;
        this.user_id=user_id;
    }

    public String getPicture_path() {
        return picture_path;
    }
    public String getTvName() {
        return tvName;
    }

}
