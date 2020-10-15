package com.example.smartpasal.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences prefs;
    Context context;

    public Session(Context cntx) {
        this.context=cntx;
        prefs = cntx.getSharedPreferences("s-martlogin", Context.MODE_PRIVATE);
    }

    public void setUsername(String username) {
        prefs.edit().putString("userName", username).apply();
    }

    public void setUserId(Integer userId){
        prefs.edit().putInt("userId",userId).apply();
    }

    public String getusername() {
        return prefs.getString("userName","");
    }

    public Integer getUserId(){
        return prefs.getInt("userId",0);
    }

    public void setToken(String token){
        prefs.edit().putString("token",token).apply();
    }

    public String getToken(){
        return prefs.getString("token","");
    }

    public void setJWT(String jwt){
        prefs.edit().putString("jwt",jwt).apply();
    }

    public String getJWT(){
        return prefs.getString("jwt","");
    }

    public void setBadgeCount(Integer badge_count){
        prefs.edit().putInt("badge_count",badge_count).apply();
    }
    public Integer getBadgeCount(){
        return prefs.getInt("badge_count",0);
    }

    public void destroy() {
        prefs.edit().clear().commit();
    }

    public String getAddress() {
        return prefs.getString("Address","");
    }

    public void setAddress(String address){
        prefs.edit().putString("Address",address).apply();

    }
}