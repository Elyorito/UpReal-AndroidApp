package com.upreal.upreal.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.User;

/**
 * Created by Eric on 26/05/2015.
 */
public class UserViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Title[];
    int nbTab;
    User mUser;
    Bundle bundle;
    private SessionManagerUser sessionManagerUser;
    private static boolean toggleAccount = false;


    public UserViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, User user, Context context) {
        super(fm);
        this.Title = mTitle;
        this.nbTab = mNbTab;
        this.mUser = user;
        bundle = new Bundle();
        bundle.putParcelable("user", user);
        sessionManagerUser = new SessionManagerUser(context);
        toggleAccount = sessionManagerUser.isLogged();

    }

    @Override
    public Fragment getItem(int position) {

        if (toggleAccount && sessionManagerUser.getUserId() == mUser.getId()) {

            switch (position) {
                case 0: //Commentary
                    UserFragmentCommentary com = new UserFragmentCommentary();
                    com.setArguments(bundle);
                    return com;
                case 1: //Options
                    UserFragmentOptions opt = new UserFragmentOptions();
                    opt.setArguments(bundle);
                    return opt;
                default:
                    return null;
            }
        } else {
            switch (position) {
                case 0: //Commentary
                    UserFragmentCommentary com = new UserFragmentCommentary();
                    com.setArguments(bundle);
                    return com;
                case 1: //Social
                    UserFragmentSocial social = new UserFragmentSocial();
                    social.setArguments(bundle);
                    return social;
                default:
                    return null;
            }
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
