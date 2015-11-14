package com.upreal.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.upreal.product.ProductActivity;
import com.upreal.store.StoreActivity;
import com.upreal.user.UserActivity;

/**
 * Created by Eric on 14/11/2015.
 */
public class Refresh extends AsyncTask<Void, Void, Object> {
    private Activity mActivity;
    private int mType;
    private int mId;

    public Refresh(Activity activity, int type, int id) {
        mActivity = activity;
        mType = type;
        mId = id;
    }

    @Override
    protected Object doInBackground(Void... params) {
        Object object = null;
        switch (mType) {
            case 1:
                SoapUserManager sm = new SoapUserManager();
                object = sm.getAccountInfoUsername(mId);
                break;
            case 2:
                SoapProductManager sp = new SoapProductManager();
                object = sp.getProductInfo(mId);
                break;
            case 3:
                SoapStoreManager ss = new SoapStoreManager();
                object = ss.getStoreInfo(mId);
                break;
            default:
                break;
        }
        return object;
    }

    protected void onPostExecute(Object object) {
        Intent intent = new Intent();
        switch (mType) {
            case 1:
                intent.setClass(mActivity.getApplicationContext(),UserActivity.class);
                intent.putExtra("user", (User) object);
                break;
            case 2:
                intent.setClass(mActivity.getApplicationContext(),ProductActivity.class);
                intent.putExtra("prod", (Product) object);
                break;
            case 3:
                intent.setClass(mActivity.getApplicationContext(),StoreActivity.class);
                intent.putExtra("store", (Store) object);
                break;
            default:
                break;
        }
        mActivity.startActivity(intent);
        mActivity.finish();
    }
}
