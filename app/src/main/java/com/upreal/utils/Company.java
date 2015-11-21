package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 21/11/2015.
 */
public class Company implements Parcelable {

    private int id;
    private String name;
    private String website;

    public Company() {}

    public Company(int id, String name, String website) {
        this.id = id;
        this.name = name;
        this.website = website;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.website);
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    protected Company(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.website = in.readString();
    }
}
