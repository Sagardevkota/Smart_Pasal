package com.example.smartpasal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItems implements Parcelable {

    private String productName;
    private String picturePath;
    private int productId;
    private  String price;
    private String brand;
    private String desc;
    private String sku;
    private int discount;
    private String category;
    private String type;
    private int stock;

    private int seller_id;

    private String rating;
    private List<String> colors = new ArrayList<>();
    private List<String> sizes = new ArrayList<>();


    protected ProductItems(Parcel in) {
        productName = in.readString();
        picturePath = in.readString();
        productId = in.readInt();
        price = in.readString();
        brand = in.readString();
        desc = in.readString();
        sku = in.readString();
        discount = in.readInt();
        category = in.readString();
        type = in.readString();
        stock = in.readInt();
        seller_id = in.readInt();
        rating = in.readString();
        colors = in.createStringArrayList();
        sizes = in.createStringArrayList();
    }

    public static final Creator<ProductItems> CREATOR = new Creator<ProductItems>() {
        @Override
        public ProductItems createFromParcel(Parcel in) {
            return new ProductItems(in);
        }

        @Override
        public ProductItems[] newArray(int size) {
            return new ProductItems[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productName);
        dest.writeString(picturePath);
        dest.writeInt(productId);
        dest.writeString(price);
        dest.writeString(brand);
        dest.writeString(desc);
        dest.writeString(sku);
        dest.writeInt(discount);
        dest.writeString(category);
        dest.writeString(type);
        dest.writeInt(stock);
        dest.writeInt(seller_id);
        dest.writeString(rating);
        dest.writeStringList(colors);
        dest.writeStringList(sizes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductItems)) return false;
        ProductItems that = (ProductItems) o;
        return getProductId() == that.getProductId() && getDiscount() == that.getDiscount() && getStock() == that.getStock() && getSeller_id() == that.getSeller_id() && Objects.equals(getProductName(), that.getProductName()) && Objects.equals(getPicturePath(), that.getPicturePath()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getBrand(), that.getBrand()) && Objects.equals(getDesc(), that.getDesc()) && Objects.equals(getSku(), that.getSku()) && Objects.equals(getCategory(), that.getCategory()) && Objects.equals(getType(), that.getType()) && Objects.equals(getRating(), that.getRating()) && Objects.equals(getColors(), that.getColors()) && Objects.equals(getSizes(), that.getSizes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductName(), getPicturePath(), getProductId(), getPrice(), getBrand(), getDesc(), getSku(), getDiscount(), getCategory(), getType(), getStock(), getSeller_id(), getRating(), getColors(), getSizes());
    }
}

