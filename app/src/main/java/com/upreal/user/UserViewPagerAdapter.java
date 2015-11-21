package com.upreal.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.upreal.utils.FragmentCommentary;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.User;

/**
 * Created by Eric on 26/05/2015.
 */
public class UserViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence title[];
    int nbTab;
    User user;
    Bundle bundle;
    private SessionManagerUser sessionManagerUser;
    private static boolean toggleAccount = false;


    public UserViewPagerAdapter(FragmentManager fm, CharSequence mtitle[], int mNbTab, User mUser, Context mContext) {
        super(fm);
        this.title = mtitle;
        this.nbTab = mNbTab;
        this.user = mUser;

        bundle = new Bundle();
        bundle.putInt("idUser", user.getId());
        bundle.putParcelable("user", user);
        bundle.putInt("id", user.getId());
        bundle.putInt("type", 1);

        sessionManagerUser = new SessionManagerUser(mContext);
        toggleAccount = sessionManagerUser.isLogged();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                UserFragmentInfo ufi = new UserFragmentInfo();
                ufi.setArguments(bundle);
                return ufi;
            case 1:
                UserFragmentProduct ufp = new UserFragmentProduct();
                ufp.setArguments(bundle);
                return ufp;
            case 2:
                FragmentCommentary fc = new FragmentCommentary();
                fc.setArguments(bundle);
                return fc;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return nbTab;
    }
}
