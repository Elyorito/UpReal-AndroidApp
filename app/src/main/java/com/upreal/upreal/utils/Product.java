package com.upreal.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elyo on 19/03/2015.
 */
public class Product implements Parcelable{

    private int id;
    private String name;
    private String ean;
    private String picture;
    private String brand;

    public Product() {
    }

    public Product(int id, String name, String ean, String picture, String brand) {
        this.id = id;
        this.name = name;
        this.ean = ean;
        this.picture = picture;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ean);
        dest.writeString(this.picture);
        dest.writeString(this.brand);
    }

    public static final Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ean = in.readString();
        this.picture = in.readString();
        this.brand = in.readString();

    }
}
