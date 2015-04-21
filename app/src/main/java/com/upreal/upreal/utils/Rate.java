package com.upreal.upreal.utils;

/**
 * Created by Elyo on 04/04/2015.
 */
public class Rate {

    private int mIdUser;
    private int mIdProduct;
    private int mIdOvrRate;
    private int mType;
    private int mIdArticle;

    public Rate(){}

    public Rate(int idUser, int idProduct, int idOvrRate, int type, int idArticle) {

        this.mIdUser = idUser;
        this.mIdProduct = idProduct;
        this.mIdOvrRate = idOvrRate;
        this.mType = type;
        this.mIdArticle = idArticle;
    }


    public int getmIdUser() {
        return mIdUser;
    }

    public void setmIdUser(int mIdUser) {
        this.mIdUser = mIdUser;
    }

    public int getmIdProduct() {
        return mIdProduct;
    }

    public void setmIdProduct(int mIdProduct) {
        this.mIdProduct = mIdProduct;
    }

    public int getmIdOvrRate() {
        return mIdOvrRate;
    }

    public void setmIdOvrRate(int mIdOvrRate) {
        this.mIdOvrRate = mIdOvrRate;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public int getmIdArticle() {
        return mIdArticle;
    }

    public void setmIdArticle(int mIdArticle) {
        this.mIdArticle = mIdArticle;
    }
}
