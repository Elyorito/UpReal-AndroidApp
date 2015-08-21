package com.upreal.uprealwear.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 18/08/2015.
 */
public class Achievement implements Parcelable {

    private int id;
    private String name;
    private String desc;

    public Achievement() {}

    public Achievement(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.desc);
    }

    public static final Creator<Achievement> CREATOR = new Creator<Achievement>() {
        @Override
        public Achievement createFromParcel(Parcel in) {
            return new Achievement(in);
        }

        @Override
        public Achievement[] newArray(int size) {
            return new Achievement[size];
        }
    };

    protected Achievement(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.desc = in.readString();
    }

}
