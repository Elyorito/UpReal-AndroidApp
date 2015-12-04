package com.upreal.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.upreal.R;
import com.upreal.utils.SessionManagerUser;

import java.io.File;

import static android.view.GestureDetector.SimpleOnGestureListener;

/**
 * Created by Elyo on 03/02/2015.
 */
public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tablayout;
    private ViewPager viewPager;

    //RecyclerView Home
    private RecyclerView mRecyclerViewHome;
    private RecyclerView.Adapter mAdapterHome;
    private ViewPager mViewpager_slideshow;
    private SlideViewPagerAdapter mAdapterSlide;

    private AlertDialog.Builder builder;

    //RecyclerView NavDrawerR
    private RecyclerView mRecyclerViewR;
    private RecyclerView.Adapter mAdapterR;
    private RecyclerView.LayoutManager mLayoutManagerR;
    private DrawerLayout DrawerR;
    private ActionBarDrawerToggle mDrawerToggleR;

    private Intent intent;

    private static boolean toggleAccount = false;

    public static final String ACTION_CLOSE_HOME = "HomeActivity.ACTION_CLOSE";

    public HomeActivity(){}
    private SessionManagerUser sessionManagerUser;

    // Camera
    private static final int ACTIVITY_START_CAMERA = 0;
    private String mImageFileLocation = "";

    // Storage
    private static final int PERMISSIONS_REQUEST = 1;
    private File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_news_historic);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        tablayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        sessionManagerUser = new SessionManagerUser(getApplicationContext());

        toggleAccount = sessionManagerUser.isLogged();

        toolbar.setBackgroundColor(getResources().getColor(R.color.ColorTabs));
        toolbar.setTitleTextColor(getResources().getColor(R.color.ColorTitle));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdapterViewPagerHome adapter = new AdapterViewPagerHome(getSupportFragmentManager(), new String[]{"News", "Fil d'actualité"});
        viewPager.setAdapter(adapter);
        //setupViewpager(viewPager);
        tablayout.setupWithViewPager(viewPager);

        if (!sessionManagerUser.isLogged()) {
            String tab[] = sessionManagerUser.getRegisterLoginUser();
            //         Toast.makeText(getApplicationContext(), "UserName[" + tab[0] +"]", Toast.LENGTH_SHORT).show();
        }

        /*RecyclerView NavigDrawR*/
        mRecyclerViewR = (RecyclerView) findViewById(R.id.RecyclerView_NavigationDrawerR);
        mRecyclerViewR.setHasFixedSize(true);
        mAdapterR = new AdapterNavDrawerSearchHome(HomeActivity.this);
        mRecyclerViewR.setAdapter(mAdapterR);

        final GestureDetector mGestureDetector = new GestureDetector(HomeActivity.this, new SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        new NavigationBar(this);

        mLayoutManagerR = new LinearLayoutManager(this);
        mRecyclerViewR.setLayoutManager(mLayoutManagerR);
        DrawerR = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggleR = new ActionBarDrawerToggle(this, DrawerR, toolbar,  R.string.connexion/*"OpenDr"*/, R.string.connexion/*"CloseDr"*/) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                final EditText editText = (EditText) drawerView.findViewById(R.id.edittext_search);
                //editText.requestFocus();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        DrawerR.setDrawerListener(mDrawerToggleR);
        mDrawerToggleR.syncState();
    }

    private void setupViewpager(ViewPager viewpager) {
        AdapterViewPagerHome adapter = new AdapterViewPagerHome(getSupportFragmentManager(), new String[]{"News", "Fil d'actualité"});
        viewpager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
//                if (!DrawerR.isDrawerVisible(Gravity.RIGHT))
//                    DrawerR.openDrawer(Gravity.RIGHT);
//                else
//                    DrawerR.closeDrawers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AdapterViewPagerHome adapter = new AdapterViewPagerHome(getSupportFragmentManager(), new String[]{"News", "Fil d'actualité"});
        viewPager.setAdapter(adapter);
    }
}
