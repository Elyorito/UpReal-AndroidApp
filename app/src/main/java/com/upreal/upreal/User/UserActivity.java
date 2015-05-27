package com.upreal.upreal.user;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.User;
import com.upreal.upreal.view.SlidingTabLayout;

/**
 * Created by Eric on 26/05/2015.
 */
public class UserActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private UserViewPagerAdapter adapter;

    private TextView userUsername;
    private TextView userDesc;
    private TextView userLocal;

    private User user;
    private SlidingTabLayout mSlidingTabLayout;
    private CharSequence title;

    private SessionManagerUser sessionManagerUser;
    private static boolean toggleAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userUsername = (TextView) findViewById(R.id.user_username);
        userDesc = (TextView) findViewById(R.id.user_desc);
        userLocal = (TextView) findViewById(R.id.user_local);

        user = getIntent().getExtras().getParcelable("listuser");

        title = new String(user.getUsername());
        userUsername.setText(user.getUsername());

        userDesc.setText(user.getFirstname()+user.getLastname());
//        userLocal.setText(Need service get address from IdAddress);
        sessionManagerUser = new SessionManagerUser(getApplicationContext());

        toggleAccount = sessionManagerUser.isLogged();

        CharSequence Tab[] = null;
        if (!toggleAccount && sessionManagerUser.getUserId() == user.getId()) {
            // User
            Tab = new CharSequence[]{getString(R.string.commentary), getString(R.string.options)};
        }
        else {
            // Other User
            Tab = new CharSequence[] {getString(R.string.commentary), getString(R.string.social)};
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new UserViewPagerAdapter(getSupportFragmentManager(), Tab, 2, user, getApplicationContext());
        mViewPager.setAdapter(adapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.ColorTabs);
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }
}
