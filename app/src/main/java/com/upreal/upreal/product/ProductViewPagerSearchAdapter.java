package com.upreal.upreal.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.upreal.utils.Product;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductViewPagerSearchAdapter extends FragmentStatePagerAdapter {

    private CharSequence Title[];
    private int nbTab;
    private String mSearchName;
    private Bundle b;

    public ProductViewPagerSearchAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, String searchname) {
        super(fm);
        this.Title = mTitle;
        this.nbTab = mNbTab;
        this.mSearchName = searchname;
        b = new Bundle();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: //Tab 1
                b.putString("searchname", this.mSearchName);

                ProductFragmentSearch fSearchProduct = new ProductFragmentSearch();
                fSearchProduct.setArguments(b);
                return fSearchProduct;
            case 1: //Tab2
                ProductFragmentTab2 tab2 = new ProductFragmentTab2();
                return tab2;
            case 2:
                ProductFragmentTab2 tab3 = new ProductFragmentTab2();
                return tab3;
            case 3:
                ProductFragmentTab2 tab4 = new ProductFragmentTab2();
                return tab4;
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
