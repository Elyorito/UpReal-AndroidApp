package com.upreal.upreal.login;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Tab[];
    private int mNbTab;

    public LoginViewPagerAdapter(android.support.v4.app.FragmentManager fm, CharSequence tab[], int nbtab) {
        super(fm);
        this.Tab = tab;
        this.mNbTab = nbtab;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: //Connect
                LoginFragmentConnect connect = new LoginFragmentConnect();
                return connect;
            case 1: //Register
                LoginFragmentRegister register = new LoginFragmentRegister();
                return register;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Tab[position];
    }

    @Override
    public int getCount() {
        return this.mNbTab;
    }
}
