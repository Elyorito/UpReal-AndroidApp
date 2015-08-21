package com.upreal.uprealwear.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Elyo on 04/04/2015.
 */
public class Rate implements Parcelable {

    private int id;
    private int id_user;
    private int mark;
    private String commentary;
    private String date;
    private int active;

    public Rate(){}

    public Rate(int id, int id_user, int mark, String commentary, String date, int active) {
        this.id = id;
        this.id_user = id_user;
        this.mark= mark;
        this.commentary = commentary;
        this.date= date;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return id_user;
    }

    public void setIdUser(int id_user) {
        this.id_user = id_user;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.id_user);
        dest.writeInt(this.mark);
        dest.writeString(this.commentary);
        dest.writeString(this.date);
        dest.writeInt(this.active);
    }
    public static final Creator<Rate> CREATOR = new Creator<Rate>() {

        @Override
        public Rate createFromParcel(Parcel source) {
            return new Rate(source);
        }

        @Override
        public Rate[] newArray(int size) {
            return new Rate[size];
        }
    };

    public Rate(Parcel in) {
        this.id = in.readInt();
        this.id_user = in.readInt();
        this.mark = in.readInt();
        this.commentary = in.readString();
        this.date = in.readString();
        this.active = in.readInt();
    }
}
