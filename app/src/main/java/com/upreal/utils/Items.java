package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elyo on 06/09/15.
 */
public class Items implements Parcelable {

    private int id_list;
    private int id_product;
    private int id_user;
    private int id;

    public Items() {}

    protected Items(Parcel in) {
        id_list = in.readInt();
        id_product = in.readInt();
        id_user = in.readInt();
        id = in.readInt();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public int getId_list() {
        return id_list;
    }

    public void setId_list(int id_list) {
        this.id_list = id_list;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_list);
        dest.writeInt(id_product);
        dest.writeInt(id_user);
        dest.writeInt(id);
    }
}
