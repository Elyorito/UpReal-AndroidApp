package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sofiane on 06/08/2015.
 */
public class Article implements Parcelable {

    private int id;
    private String title;
    private String body;
    private String creation;
    private int type;
    private String picture;

    public Article(){}

    public Article(int id, String title, String body, String creation, int type, String picture) {

        this.id = id;
        this.title = title;
        this.body = body;
        this.creation = creation;
        this.type = type;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.body);
        dest.writeString(this.creation);
        dest.writeInt(this.type);
        dest.writeString(this.picture);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    protected Article(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.body = in.readString();
        this.creation = in.readString();
        this.type = in.readInt();
        this.picture = in.readString();
    }
}
