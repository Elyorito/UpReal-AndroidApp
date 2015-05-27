package com.upreal.upreal.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Toast;

import com.upreal.upreal.user.UserFragmentSearch;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductViewPagerSearchAdapter extends FragmentStatePagerAdapter {

    private CharSequence Title[];
    private int nbTab;
    private String mSearchName;
    private Bundle b;
    private ProductFragmentSearch fSearchProduct;
    private UserFragmentSearch fSearchUser;

    public ProductViewPagerSearchAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, String searchname) {
        super(fm);
        this.Title = mTitle;
        this.nbTab = mNbTab;
        this.mSearchName = searchname;
        b = new Bundle();
        b.putString("searchname", this.mSearchName);
        fSearchProduct = new ProductFragmentSearch();
        fSearchUser = new UserFragmentSearch();
        fSearchProduct.setArguments(b);
        fSearchUser.setArguments(b);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: //Tab 1
                return fSearchProduct;
            case 1: //Tab2
                return fSearchUser;
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
