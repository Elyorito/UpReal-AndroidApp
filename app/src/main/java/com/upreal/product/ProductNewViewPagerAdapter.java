package com.upreal.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.utils.Product;

/**
 * Created by Elyo on 01/10/15.
 */
public class ProductNewViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Title[];
    int nbTab;
    Product mProduct;
    Bundle bundle;

    public ProductNewViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, Product product) {
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
            case 0: //Info
//                ProductFragmentCommentary com = new ProductFragmentCommentary();
//                com.setArguments(bundle);
//                return com;
                return new ProductFragmentTab2();
            case 1: //Prix
//                ProductFragmentSocial social = new ProductFragmentSocial();
//                social.setArguments(bundle);
//                return social;
                return new ProductFragmentTab2();
            case 2: //Avis
//                ProductFragmentOptions opt = new ProductFragmentOptions();
//                opt.setArguments(bundle);
//                return opt;
                ProductFragmentCommentary com = new ProductFragmentCommentary();
                com.setArguments(bundle);
                return com;
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