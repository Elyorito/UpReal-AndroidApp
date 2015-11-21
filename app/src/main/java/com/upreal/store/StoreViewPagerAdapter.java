package com.upreal.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.utils.FragmentCommentary;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence title[];
    int nbTab;
    Store store;
    Bundle bundle;

    public StoreViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, Store mStore) {
        super(fm);
        this.title = mTitle;
        this.nbTab = mNbTab;
        this.store = mStore;

        bundle = new Bundle();
        bundle.putInt("idStore", store.getId());
        bundle.putParcelable("store", store);
        bundle.putInt("id", store.getId());
        bundle.putInt("type", 3);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                StoreFragmentInfo sfi = new StoreFragmentInfo();
                sfi.setArguments(bundle);
                return sfi;
            case 1:
                StoreFragmentProduct sfp = new StoreFragmentProduct();
                sfp.setArguments(bundle);
                return sfp;
            case 2:
                FragmentCommentary fc = new FragmentCommentary();
                fc.setArguments(bundle);
                return fc;
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
