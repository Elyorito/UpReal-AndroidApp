package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 04/11/2015.
 */
public class UserSell implements Parcelable {

    private int id;
    private double price;
    private int idProduct;
    private int idUser;

    public UserSell() {}

    public UserSell(int id, double price, int idProduct, int idUser) {
        this.id = id;
        this.price = price;
        this.idProduct = idProduct;
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idStore) {
        this.idUser = idStore;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeDouble(this.price);
        dest.writeInt(this.idProduct);
        dest.writeInt(this.idUser);
    }
    public static final Parcelable.Creator<UserSell> CREATOR = new Parcelable.Creator<UserSell>() {

        @Override
        public UserSell createFromParcel(Parcel source) {
            return new UserSell(source);
        }

        @Override
        public UserSell[] newArray(int size) {
            return new UserSell[size];
        }
    };

    public UserSell(Parcel in) {
        this.id = in.readInt();
        this.price = in.readDouble();
        this.idProduct = in.readInt();
        this.idUser = in.readInt();
    }
}
