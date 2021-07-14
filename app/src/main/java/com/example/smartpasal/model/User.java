package com.example.smartpasal.model;


import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Parcelable {

    private int id;
    private String userName;
    private String password;
    private String deliveryAddress;
    private String role;
    private String age;
    private String gender;
    private String phone;
    private Boolean verified;
    private Double latitude;
    private Double longitude;
    private int cartCount;

    public User(String userName, String password, String deliveryAddress, String phone, String role, String age, String gender, Double latitude, Double longitude) {
        this.userName = userName;
        this.password = password;
        this.deliveryAddress = deliveryAddress;
        this.phone = phone;
        this.role = role;
        this.age = age;
        this.gender = gender;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public User(String userName, String deliveryAddress, String phone) {
        this.userName = userName;
        this.deliveryAddress = deliveryAddress;
        this.phone = phone;

    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    protected User(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        password = in.readString();
        deliveryAddress = in.readString();
        phone = in.readString();
        byte tmpVerified = in.readByte();
        verified = tmpVerified == 0 ? null : tmpVerified == 1;
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(userName);
        parcel.writeString(password);
        parcel.writeString(deliveryAddress);
        parcel.writeString(phone);
        parcel.writeByte((byte) (verified == null ? 0 : verified ? 1 : 2));
        if (latitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(latitude);
        }
        if (longitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(longitude);
        }
    }
}

