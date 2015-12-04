package com.upreal.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kyosukke on 04/12/2015.
 */
public class Suggestion implements Parcelable {

    private int id;
    private int idUser;
    private int idType;
    private int targetType;
    private int idTarget;
    private String text;

    public Suggestion() {}

    public Suggestion(int id, int idUser, int idType, int targetType, int idTarget, String text) {
        this.id = id;
        this.idUser = idUser;
        this.targetType = targetType;
        this.idType = idType;
        this.idTarget = idTarget;
        this.text = text;
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

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.idUser);
        dest.writeInt(this.idType);
        dest.writeInt(this.targetType);
        dest.writeInt(this.idTarget);
        dest.writeString(this.text);
    }

    public static final Parcelable.Creator<Suggestion> CREATOR = new Parcelable.Creator<Suggestion>() {
        @Override
        public Suggestion createFromParcel(Parcel in) {
            return new Suggestion(in);
        }

        @Override
        public Suggestion[] newArray(int size) {
            return new Suggestion[size];
        }
    };

    protected Suggestion(Parcel in) {
        this.id = in.readInt();
        this.idUser = in.readInt();
        this.idType = in.readInt();
        this.targetType = in.readInt();
        this.idTarget = in.readInt();
        this.text = in.readString();
    }
}
