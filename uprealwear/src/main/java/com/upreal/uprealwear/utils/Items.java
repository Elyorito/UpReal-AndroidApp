package com.upreal.uprealwear.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class Items implements Parcelable {

    private int id;
    private int idList;
    private int idProduct;
    private int idUser;

    public Items() {}

    public Items(int id, int idList, int idProduct, int idUser) {
        this.id = id;
        this.idList = idList;
        this.idProduct = idProduct;
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdList() {
        return idList;
    }

    public void setIdList(int idList) {
        this.idList = idList;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.idList);
        dest.writeInt(this.idProduct);
        dest.writeInt(this.idUser);
    }

    public static final Parcelable.Creator<Items> CREATOR = new Parcelable.Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    protected Items(Parcel in) {
        this.id = in.readInt();
        this.idList = in.readInt();
        this.idProduct = in.readInt();
        this.idUser = in.readInt();
    }
}
