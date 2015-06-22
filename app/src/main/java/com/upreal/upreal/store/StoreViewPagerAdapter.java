package com.upreal.upreal.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.upreal.product.ProductFragmentCommentary;
import com.upreal.upreal.product.ProductFragmentOptions;
import com.upreal.upreal.product.ProductFragmentSocial;
import com.upreal.upreal.utils.Store;

/**
 * Created by Kyosukke on 20/06/2015.
 */
public class StoreViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence title[];
    int nbTab;
    Store mStore;
    Bundle bundle;

    public StoreViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, Store store) {
        super(fm);
        this.title = mTitle;
        this.nbTab = mNbTab;
        this.mStore = store;
        bundle = new Bundle();
        bundle.putParcelable("store", store);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: //Commentary
                ProductFragmentCommentary com = new ProductFragmentCommentary();
                com.setArguments(bundle);
                return com;
            case 1: //Social
                ProductFragmentSocial social = new ProductFragmentSocial();
                social.setArguments(bundle);
                return social;
            case 2: //Options
                ProductFragmentOptions opt = new ProductFragmentOptions();
                opt.setArguments(bundle);
                return opt;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return nbTab;
    }
}
