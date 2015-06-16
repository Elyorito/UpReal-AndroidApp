package com.upreal.upreal.utils;

/**
 * Created by Eric on 13/06/2015.
 */
public class Address {
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

    public Address() {}

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
}
