package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eric on 13/06/2015.
 */
public class Address implements Parcelable {
    private int id;
    private String address;
    private String address2;
    private String city;
    private String country;
    private int postalCode;
    // + date

    public Address (int id, String address, String address2, String country, String city, int postalCode) {
        this.id = id;
        this.address = address;
        this.address2 = address2;
        this.city = city;
        this.country = country;
        this.postalCode = postalCode;
    }

    public Address() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.address);
        dest.writeString(this.address2);
        dest.writeString(this.city);
        dest.writeString(this.country);
        dest.writeInt(this.postalCode);
    }

    public static final Creator<Address> CREATOR = new Parcelable.Creator<Address>() {

        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public Address(Parcel in) {
        this.id = in.readInt();
        this.address = in.readString();
        this.address2 = in.readString();
        this.city = in.readString();
        this.country = in.readString();
        this.postalCode = in.readInt();
    }
}
