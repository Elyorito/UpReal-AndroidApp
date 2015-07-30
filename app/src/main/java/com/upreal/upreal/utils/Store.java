package com.upreal.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 20/06/2015.
 */
public class Store implements Parcelable {

    private int id;
    private String name;
    private String website;
    private int id_address;
    private int id_company;

    public Store() {}

    public Store(int id, String name, String website, int id_address, int id_company) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.id_address = id_address;
        this.id_company = id_company;
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

    public int getId_address() {
        return id_address;
    }

    public void setId_address(int id_address) {
        this.id_address = id_address;
    }

    public int getId_company() {
        return id_company;
    }

    public void setId_company(int id_company) {
        this.id_company = id_company;
    }

    public String getCompanyName() {return "Non accessible";}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.website);
        dest.writeInt(this.id_address);
        dest.writeInt(this.id_company);
    }
    public static final Creator<Store> CREATOR = new Parcelable.Creator<Store>() {

        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public Store(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.website = in.readString();
        this.id_address = in.readInt();
        this.id_company = in.readInt();
    }
}
