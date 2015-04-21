package com.upreal.upreal.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.upreal.utils.Product;

/**
 * Created by Elyo on 12/02/2015.
 */
public class ProductViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Title[];
    int nbTab;
    Product mProduct;
    Bundle bundle;

    public ProductViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, Product product) {
        super(fm);
        this.Title = mTitle;
        this.nbTab = mNbTab;
        this.mProduct = product;
        bundle = new Bundle();
        bundle.putParcelable("product", product);
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
        return Title[position];
    }

    @Override
    public int getCount() {
        return nbTab;
    }
}
