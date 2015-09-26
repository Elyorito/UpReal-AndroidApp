package com.upreal.utils;

import java.util.Date;

/**
 * Created by Elyo on 04/04/2015.
 */
public class RateComment {

    private String mTextComment;
    private Date mDateTime;
    private String mNameUser;

    public RateComment() {}

    public RateComment(String textComment, Date dateTime, String userName) {
        this.mTextComment = textComment;
        this.mDateTime = dateTime;
        this.mNameUser = userName;
    }

    public String getmTextComment() {
        return mTextComment;
    }

    public void setmTextComment(String mTextComment) {
        this.mTextComment = mTextComment;
    }

    public Date getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(Date mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getmNameUser() {
        return mNameUser;
    }

    public void setmNameUser(String mNameUser) {
        this.mNameUser = mNameUser;
    }
}
