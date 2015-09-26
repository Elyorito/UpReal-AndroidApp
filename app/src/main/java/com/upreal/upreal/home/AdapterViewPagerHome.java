package com.upreal.upreal.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.upreal.upreal.news.NewsFragment;

/**
 * Created by Elyo on 23/09/15.
 */
public class AdapterViewPagerHome extends FragmentPagerAdapter {

    private NewsFragment fnews;
    private NewsFragment fthread;
    private String[] tab;

    public AdapterViewPagerHome(FragmentManager fm, String[] tab_name) {
        super(fm);
        this.tab = tab_name;
        fnews = new NewsFragment();
        fthread = new NewsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return fnews;
            case 1:
                return fthread;
            default:
                break;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tab[position];
    }

    @Override
    public int getCount() {
        return 2;
    }
}
