package com.upreal.store;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.product.ProductFragmentTab2;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence title[];
    int nbTab;
    Store store;
    Bundle bundle;
    private Activity activity;

    public StoreViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, Store mStore, Activity mActivity) {
        super(fm);
        this.title = mTitle;
        this.nbTab = mNbTab;
        this.store = mStore;
        this.activity = mActivity;

        bundle = new Bundle();
        bundle.putParcelable("store", store);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new ProductFragmentTab2();
            case 2:
                return new ProductFragmentTab2();
            case 3:
                return new ProductFragmentTab2();
            default:
                return null;
        }
    }

    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return nbTab;
    }
}
