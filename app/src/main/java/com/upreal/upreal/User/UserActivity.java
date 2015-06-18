package com.upreal.upreal.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.Address;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapUserUtilManager;
import com.upreal.upreal.utils.User;
import com.upreal.upreal.view.SlidingTabLayout;

/**
 * Created by Eric on 26/05/2015.
 */
public class UserActivity extends ActionBarActivity {

    private TextView userLocal;

    private Address address;
    private CharSequence title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        TextView userUsername = (TextView) findViewById(R.id.user_username);
        TextView userDesc = (TextView) findViewById(R.id.user_desc);
        userLocal = (TextView) findViewById(R.id.user_local);

        SessionManagerUser sessionManagerUser = new SessionManagerUser(getApplicationContext());
        boolean toggleAccount = sessionManagerUser.isLogged();

        User user;
        if (getIntent().getExtras() == null)
            user = sessionManagerUser.getUser();
        else
            user = getIntent().getExtras().getParcelable("listuser");

        title = new String(user.getUsername());
        userUsername.setText(user.getUsername());
        userLocal.setText("Adresse " + getResources().getString(R.string.not_defined));
        if (user.getId_address() != -1 && user.getId_address() != 0)
            new getAddress().execute(user.getId_address());
        if (user.getFirstname() == null && user.getLastname() == null)
            userDesc.setText(R.string.no_info);
        else
            userDesc.setText(user.getFirstname() + " " + user.getLastname());
//        userLocal.setText(Need service get address from IdAddress);



        CharSequence Tab[] = null;
        if (toggleAccount && sessionManagerUser.getUserId() == user.getId()) {
            // User
            Tab = new CharSequence[]{getString(R.string.commentary), getString(R.string.options)};
            Log.v("Options", "Here");
        }
        else {
            // Other User
            Tab = new CharSequence[] {getString(R.string.commentary), getString(R.string.social)};
            Log.v("Social", "Here");
            Log.v("session ID", String.valueOf(sessionManagerUser.getUserId()));
            Log.v("user ID", String.valueOf(user.getId()));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        UserViewPagerAdapter adapter = new UserViewPagerAdapter(getSupportFragmentManager(), Tab, 2, user, getApplicationContext());
        mViewPager.setAdapter(adapter);

        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
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

    private class getAddress extends AsyncTask<Integer, Void, Address> {
        SoapUserUtilManager pm = new SoapUserUtilManager();

        @Override
        protected Address doInBackground(Integer... params) {
            return pm.getAddressInfo(params[0]);
        }

        protected void onPostExecute(Address result) {
            userLocal.setText(result.getAddress() + "\n"
                    + result.getAddress2() + "\n"
                    + result.getCountry() + " "
                    + result.getCity() + "\n"
                    + ((result.getPostalCode() == 0) ? "" : String.valueOf(result.getPostalCode())));
        }
    }
}
