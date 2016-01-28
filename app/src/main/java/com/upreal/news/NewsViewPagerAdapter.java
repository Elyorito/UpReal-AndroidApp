package com.upreal.news;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.utils.Article;
import com.upreal.utils.FragmentCommentary;

/**
 * Created by Eric on 05/12/2015.
 */
public class NewsViewPagerAdapter extends FragmentStatePagerAdapter {
    private CharSequence Title[];
    int nbTab;
    private Article mArticle;
    Bundle bundle;
    private Activity mActivity;

    public NewsViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, Article article, Activity activity) {
        super(fm);
        Title = mTitle;
        nbTab = mNbTab;
        mArticle = article;
        mActivity = activity;
        bundle = new Bundle();
        bundle.putParcelable("article", article);
        bundle.putInt("idArticle", article.getId());
        bundle.putInt("type", 4);
        bundle.putInt("id", article.getId());

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Title[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: //Info
                NewsFragmentInfo nfi = new NewsFragmentInfo();
                nfi.setArguments(bundle);
                return nfi;
            case 1: //Avis
                FragmentCommentary com = new FragmentCommentary();
                com.setArguments(bundle);
                return com;
            default:
                return null;
        }    }

    @Override
    public int getCount() {
        return nbTab;
    }
}
