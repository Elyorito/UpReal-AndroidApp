package com.upreal.upreal.product;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Elyo on 12/02/2015.
 */
public class ProductViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Title[];
    int nbTab;

    public ProductViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab) {
        super(fm);
        this.Title = mTitle;
        this.nbTab = mNbTab;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: //Tab 1
                ProductFragmentTab1 tab1 = new ProductFragmentTab1();
                return tab1;
            case 1: //Tab2
                ProductFragmentTab2 tab2 = new ProductFragmentTab2();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Title[position];
    }

    @Override
    public int getCount() {
        return nbTab;
    }
}
