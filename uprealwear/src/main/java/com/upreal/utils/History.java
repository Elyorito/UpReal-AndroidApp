package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 18/08/2015.
 */
public class History implements Parcelable {

    private int id;
    private int idUser;
    private int actionType;
    private int idType;
    private int idTarget;
    private String date;

    public History() {}

    public History(int id, int idUser, int actionType, int idType, int idTarget, String date) {
        this.id = id;
        this.idUser = idUser;
        this.actionType = actionType;
        this.idType = idType;
        this.idTarget = idTarget;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public int getIdTarget() {
        return idTarget;
    }

    public void setIdTarget(int idTarget) {
        this.idTarget = idTarget;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.idUser);
        dest.writeInt(this.actionType);
        dest.writeInt(this.idType);
        dest.writeInt(this.idTarget);
        dest.writeString(this.date);
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    protected History(Parcel in) {
        this.id = in.readInt();
        this.idUser = in.readInt();
        this.actionType = in.readInt();
        this.idType = in.readInt();
        this.idTarget = in.readInt();
        this.date = in.readString();
    }
}
