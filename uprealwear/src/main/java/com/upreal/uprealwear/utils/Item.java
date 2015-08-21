package com.upreal.uprealwear.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 08/08/2015.
 */
public class Item implements Parcelable {

    private int id;

    /*
            TARGET_TYPE
            1 = id_user
            2 = id_product
            3 = id_store
            4 = id_article
            5 = id_ovr_rate

            UNOFFICIAL
            6 = id_lists
            7 = id_items
         */
    private int targetType;

    private String imagePath;
    private String name;

    public Item(int id, int targetType, String name, String imagePath) {
        this.id = id;
        this.targetType = targetType;
        this.imagePath = imagePath;
        this.name = name;

        if (imagePath == null || imagePath == "")
            this.imagePath = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.targetType);
        dest.writeString(this.name);
        dest.writeString(this.imagePath);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {

        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public Item(Parcel in) {
        this.id = in.readInt();
        this.targetType = in.readInt();
        this.name = in.readString();
        this.imagePath = in.readString();
    }
}
