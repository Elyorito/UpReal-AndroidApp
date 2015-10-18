package com.upreal.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Elyo on 19/03/2015.
 */
public class Product implements Parcelable{

    private int id;
    private String name;
    private String ean;
    private String picture;
    private String brand;
    private String shortDesc;

    public Product() {
    }


    public Product(int id, String name, String ean, String picture, String brand, String shortDesc) {
        this.id = id;
        this.name = name;
        this.ean = ean;
        this.picture = picture;
        this.brand = brand;
        this.shortDesc = shortDesc;
    }

    public Product(String name) {
        this.id = 0;
        this.name = "";
        this.ean = "";
        this.picture = "";
        this.brand = "";
    }

    public Product(int id, String name, String ean, String picture, String brand) {

        this.id = id;
        this.name = name;
        this.ean = ean;
        this.picture = picture;
        this.brand = brand;
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

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ean);
        dest.writeString(this.picture);
        dest.writeString(this.brand);
        //dest.writeString(this.shortDesc);
    }

    public static final Creator<Product> CREATOR = new Parcelable.Creator<Product>() {

        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ean = in.readString();
        this.picture = in.readString();
        this.brand = in.readString();
        //this.shortDesc = in.readString();
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    static public class setProductCategory extends AsyncTask<Void, Void, Void> {
        private int mIdProduct;
        private String mKeyword;

        public setProductCategory(int idProduct, String keyword) {
            mIdProduct = idProduct;
            mKeyword = keyword;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SoapProductUtilManager sm = new SoapProductUtilManager();
            sm.setProductCategory(mIdProduct, mKeyword);
            return null;
        }
    }

    static public class getCategory extends AsyncTask<Void, Void, ArrayList<String>> {

        private Spinner mSpinner;
        private Activity mActivity;

        public getCategory(Spinner spinner, Activity activity) {
            mSpinner = spinner;
            mActivity = activity;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> list;
            SoapProductUtilManager sm = new SoapProductUtilManager();
            list = sm.getCategory();

            return list;
        }

        protected void onPostExecute(ArrayList<String> list) {
            super.onPostExecute(list);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                    (mActivity, android.R.layout.simple_spinner_item, list);

            dataAdapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);

            mSpinner.setAdapter(dataAdapter);
        }
    }

    static public class getProductCategory extends AsyncTask<Void, Void, String> {

        private int mIdProduct;
        private Spinner mCategory;

        public getProductCategory(int idProduct, Spinner category) {
            mIdProduct = idProduct;
            mCategory = category;
        }

        @Override
        protected String doInBackground(Void... params) {
            SoapProductUtilManager sm = new SoapProductUtilManager();
            String cat = sm.getProductCategory(mIdProduct);
            return cat;
        }

        protected void onPostExecute(String category) {
            mCategory.setSelection(((ArrayAdapter) mCategory.getAdapter()).getPosition(category));
        }
    }
}
