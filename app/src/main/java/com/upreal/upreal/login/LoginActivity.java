package com.upreal.upreal.login;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.upreal.upreal.R;
import com.upreal.upreal.view.SlidingTabLayout;

/**
 * Created by Elyo on 01/03/2015.
 */
public class LoginActivity extends ActionBarActivity {

    private CharSequence Tab[];

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private LoginViewPagerAdapter mAdapter;
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.connexion);
        setSupportActionBar(toolbar);
        Tab = new CharSequence[]{getString(R.string.select_connexion), getString(R.string.select_register)};

        mViewPager = (ViewPager) findViewById(R.id.login_viewpager);
        mAdapter = new LoginViewPagerAdapter(getSupportFragmentManager(), Tab, 2);
        mViewPager.setAdapter(mAdapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.login_sliding_tab);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ColorTabs);
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);
    }
}