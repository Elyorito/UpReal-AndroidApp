package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elyo on 29/08/15.
 */
public class Lists implements Parcelable {

    private int id;
    private String name;
    private int l_public;
    private int nb_items;
    private int id_user;
    private int type;
    private int date;


    public Lists() {}

    public Lists(int id, String name, int l_public, int nb_items, int id_user, int type, int date) {
        this.id = id;
        this.name = name;
        this.l_public = l_public;
        this.nb_items = nb_items;
        this.id_user = id_user;
        this.type = type;
        this.date = date;
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

    public int getL_public() {
        return l_public;
    }

    public void setL_public(int l_public) {
        this.l_public = l_public;
    }

    public int getNb_items() {
        return nb_items;
    }

    public void setNb_items(int nb_items) {
        this.nb_items = nb_items;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(l_public);
        dest.writeInt(nb_items);
        dest.writeInt(id_user);
        dest.writeInt(type);
        dest.writeInt(date);
    }

    public static final Creator<Lists> CREATOR = new Creator<Lists>() {
        @Override
        public Lists createFromParcel(Parcel in) {
            return new Lists(in);
        }

        @Override
        public Lists[] newArray(int size) {
            return new Lists[size];
        }
    };

    protected Lists(Parcel in) {
        id = in.readInt();
        name = in.readString();
        l_public = in.readInt();
        nb_items = in.readInt();
        id_user = in.readInt();
        type = in.readInt();
        date = in.readInt();
    }
}
