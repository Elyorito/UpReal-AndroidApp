package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class Lists implements Parcelable {

    private int id;
    private String name;
    private int isPublic;
    private int type;
    private int nbItems;
    private int idUser;
    private String date;

    public Lists() {}

    public Lists(int id, String name, int isPublic, int type, int nbItems, int idUser, String date) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.type = type;
        this.nbItems = nbItems;
        this.idUser = idUser;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNbItems() {
        return nbItems;
    }

    public void setNbItems(int nb_items) {
        this.nbItems = nb_items;
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
        dest.writeString(this.name);
        dest.writeInt(this.isPublic);
        dest.writeInt(this.type);
        dest.writeInt(this.nbItems);
        dest.writeInt(this.idUser);
        dest.writeString(this.date);
    }

    public static final Parcelable.Creator<Lists> CREATOR = new Parcelable.Creator<Lists>() {
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
        this.id = in.readInt();
        this.name = in.readString();
        this.isPublic = in.readInt();
        this.type = in.readInt();
        this.nbItems = in.readInt();
        this.idUser = in.readInt();
        this.date = in.readString();
    }
}
