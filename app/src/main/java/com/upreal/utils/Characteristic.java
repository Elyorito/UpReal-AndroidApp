package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 05/11/2015.
 */
public class Characteristic implements Parcelable {

    private int type;
    private String value;
    private String name;
    private int healthy;

    public Characteristic() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealthy() {
        return healthy;
    }

    public void setHealthy(int healthy) {
        this.healthy = healthy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.name);
        dest.writeString(this.value);
        dest.writeInt(this.healthy);
    }
    public static final Creator<Characteristic> CREATOR = new Parcelable.Creator<Characteristic>() {

        @Override
        public Characteristic createFromParcel(Parcel source) {
            return new Characteristic(source);
        }

        @Override
        public Characteristic[] newArray(int size) {
            return new Characteristic[size];
        }
    };

    public Characteristic(Parcel in) {
        this.type = in.readInt();
        this.name = in.readString();
        this.value = in.readString();
        this.healthy = in.readInt();
    }
}