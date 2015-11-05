package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreSell implements Parcelable {

    private int id;
    private double price;
    private int idProduct;
    private int idStore;

    public StoreSell() {}

    public StoreSell(int id, double price, int idProduct, int idStore) {
        this.id = id;
        this.price = price;
        this.idProduct = idProduct;
        this.idStore = idStore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
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
        dest.writeInt(this.idStore);
    }
    public static final Creator<StoreSell> CREATOR = new Parcelable.Creator<StoreSell>() {

        @Override
        public StoreSell createFromParcel(Parcel source) {
            return new StoreSell(source);
        }

        @Override
        public StoreSell[] newArray(int size) {
            return new StoreSell[size];
        }
    };

    public StoreSell(Parcel in) {
        this.id = in.readInt();
        this.price = in.readDouble();
        this.idProduct = in.readInt();
        this.idStore = in.readInt();
    }
}
