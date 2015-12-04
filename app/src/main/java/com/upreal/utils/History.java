package com.upreal.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kyosukke on 18/08/2015.
 */
public class History implements Parcelable {

    private int id;
    private int idUser;
    private int actionType;
    private int idType;
    private int idTarget;
    private Date date;

    public History() {}

    public History(int id, int idUser, int actionType, int idType, int idTarget, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            this.date = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        dest.writeSerializable(this.date);
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
        this.date = (Date) in.readSerializable();
    }

    public static class createHistory extends AsyncTask<Void, Void, Void> {

        private SessionManagerUser sessionManagerUser;
        private int action;
        private int type;
        private int target;

        public createHistory(Context context, int action_type, int id_type, int id_target) {
            sessionManagerUser = new SessionManagerUser(context.getApplicationContext());
            action = action_type;
            type = id_type;
            target = id_target;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SoapUserUtilManager su = new SoapUserUtilManager();

            if (sessionManagerUser.isLogged())
                su.createHistory(sessionManagerUser.getUserId(), action, type, target);
            return null;
        }
    }
}
