package com.upreal.utils;

/**
 * Created by Elyo on 04/04/2015.
 */
public class Rate {

    private int mId;
    private int mId_user;
    private int mMark;
    private String mCommentary;
    private String mDate;
    private int mActive;
    private int mUp;
    private int mDown;

    public Rate(){}

    public Rate(int mId_user, int mMark, String mCommentary, String mDate, int mActive, int mUp, int mDown) {

        this.mId_user = mId_user;
        this.mMark= mMark;
        this.mCommentary = mCommentary;
        this.mDate= mDate;
        this.mActive = mActive;
        this.mUp = mUp;
        this.mDown = mDown;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmId_user() {
        return mId_user;
    }

    public void setmId_user(int mId_user) {
        this.mId_user = mId_user;
    }

    public int getmMark() {
        return mMark;
    }

    public void setmMark(int mMark) {
        this.mMark = mMark;
    }

    public String getmCommentary() {
        return mCommentary;
    }

    public void setmCommentary(String mCommentary) {
        this.mCommentary = mCommentary;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public int getmActive() {
        return mActive;
    }

    public void setmActive(int mActive) {
        this.mActive = mActive;
    }

    public int getmUp() {
        return mUp;
    }

    public void setmUp(int mUp) {
        this.mUp = mUp;
    }

    public int getmDown() {
        return mDown;
    }

    public void setmDown(int mDown) {
        this.mDown = mDown;
    }
}
