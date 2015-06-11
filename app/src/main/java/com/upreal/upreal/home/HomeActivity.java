package com.upreal.upreal.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.upreal.upreal.R;

import com.upreal.upreal.list.ListActivity;
import com.upreal.upreal.login.LoginActivity;
import com.upreal.upreal.product.ProductActivity;
import com.upreal.upreal.scan.Camera2Activity;
import com.upreal.upreal.scan.Camera2Fragment;
import com.upreal.upreal.scan.CameraActivity;
import com.upreal.upreal.user.UserActivity;
import com.upreal.upreal.utils.SessionManagerUser;

import static android.view.GestureDetector.*;

/**
 * Created by Elyo on 03/02/2015.
 */
public class HomeActivity extends ActionBarActivity {


    private Toolbar toolbar;

    //RecyclerView Home
    private RecyclerView mRecyclerViewHome;
    private RecyclerView.Adapter mAdapterHome;
    private ViewPager mViewpager_slideshow;
    private SlideViewPagerAdapter mAdapterSlide;

    //RecyclerView NavDrawerL
    private RecyclerView mRecyclerViewL;
    private RecyclerView.Adapter mAdapterL;
    private RecyclerView.LayoutManager mLayoutManagerL;
    private DrawerLayout DrawerL;
    private ActionBarDrawerToggle mDrawerToggleL;

    //RecyclerView NavDrawerR
    private RecyclerView mRecyclerViewR;
    private RecyclerView.Adapter mAdapterR;
    private RecyclerView.LayoutManager mLayoutManagerR;
    private DrawerLayout DrawerR;
    private ActionBarDrawerToggle mDrawerToggleR;


    private Intent intent;

    private String CARDHOME[];
    private String ITEM_WTACCOUNT[];
    private String ITEM_WACCOUNT[];
    private String ACCOUNT[];

    private String PRODUCTREDUC[];
    private String PRODUCTREDUCPRICE[];
    private static boolean toggleAccount = false;

    public static final String ACTION_CLOSE_HOME = "HomeActivity.ACTION_CLOSE";
    private IntentFilter intentFilter;
    private HomeReceiver homeReceiver;

    public HomeActivity(){};
    private SessionManagerUser sessionManagerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManagerUser = new SessionManagerUser(getApplicationContext());
        //sessionManagerUser.deleteALL();

        toggleAccount = sessionManagerUser.isLogged();

        CARDHOME = new String[]{getString(R.string.profile), getString(R.string.scan),getString(R.string.news), getString(R.string.loyalty), getString(R.string.list), getString(R.string.acheivement)};
        ITEM_WTACCOUNT = new String[]{this.getString(R.string.scan),this.getString(R.string.history_product),
                this.getString(R.string.list),this.getString(R.string.news),
                this.getString(R.string.catalog), this.getString(R.string.shop), this.getString(R.string.settings)};
        ITEM_WACCOUNT = new String[]{this.getString(R.string.scan),
                this.getString(R.string.list),
                this.getString(R.string.news),
                this.getString(R.string.catalog),
                this.getString(R.string.shop),
                this.getString(R.string.loyalty_cards),
                this.getString(R.string.parrainage),
                this.getString(R.string.acheivement),
                this.getString(R.string.settings),
                "Deconnexion",
        "lol"};
        ACCOUNT = new String[]{getString(R.string.connexion)};

        PRODUCTREDUC = new String[] {"P'tit Louis", "P'tit Louis", "P'tit Louis", "P'tit Louis", "P'tit Louis", "P'tit Louis"};

        PRODUCTREDUCPRICE = new String[] {"1.20€ Remboursé", "1.20€ Remboursé", "1.20€ Remboursé", "1.20€ Remboursé", "1.20€ Remboursé", "1.20€ Remboursé"};

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (!sessionManagerUser.isLogged()) {
            String tab[] = sessionManagerUser.getRegisterLoginUser();
           Toast.makeText(getApplicationContext(), "UserName[" + tab[0] +"]", Toast.LENGTH_SHORT).show();
        }
        /*RecyclerView MainView Home*/
        mRecyclerViewHome = (RecyclerView) findViewById(R.id.RecyclerView_Home);
        mRecyclerViewHome.setHasFixedSize(true);
        mRecyclerViewHome.setLayoutManager(new GridLayoutManager(this, 1));
//        mAdapterHome = new AdapterHome(CARDHOME, HomeActivity.this);
        mAdapterHome = new AdapterHome(PRODUCTREDUC, PRODUCTREDUCPRICE, HomeActivity.this);
        mRecyclerViewHome.setAdapter(mAdapterHome);

        mViewpager_slideshow = (ViewPager) findViewById(R.id.viewpager_home);
        mAdapterSlide = new SlideViewPagerAdapter(this);
        mViewpager_slideshow.setAdapter(mAdapterSlide);

        /*RecyclerView NavigDrawL*/

        mRecyclerViewL = (RecyclerView) findViewById(R.id.RecyclerView_NavigationDrawer);
        mRecyclerViewL.setHasFixedSize(true);
        if (toggleAccount)
            mAdapterL = new AdapterNavDrawerHome(ACCOUNT, ITEM_WACCOUNT);
        else
            mAdapterL = new AdapterNavDrawerConnectHome(ACCOUNT, ITEM_WACCOUNT);
        mRecyclerViewL.setAdapter(mAdapterL);

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

        mRecyclerViewL.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());

                if (child != null && mGestureDetector.onTouchEvent(e)) {
                    DrawerL.closeDrawers();
                    Toast.makeText(HomeActivity.this, "Item :" + rv.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    if (!sessionManagerUser.isLogged()) {
                        Toast.makeText(HomeActivity.this, "Is NOT LOG", Toast.LENGTH_SHORT).show();
                        switch (rv.getChildPosition(child)) {
                            case 0://Connect
                                intentFilter = new IntentFilter(ACTION_CLOSE_HOME);
                                homeReceiver = new HomeReceiver();
                                registerReceiver(homeReceiver, intentFilter);
                                intent = new Intent(rv.getContext(), LoginActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 1: //Scan
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;

/*
                                intent = new Intent(rv.getContext(), Camera2Activity.class);
                                rv.getContext().startActivity(intent);
                                return true;
*/
                           case 2://Lists
                                intent = new Intent(rv.getContext(), ListActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            default:
                                return false;
                        }
                    }
                    if (sessionManagerUser.isLogged()) {
                        switch (rv.getChildPosition(child)) {
                            case 0://Connect
                                intentFilter = new IntentFilter(ACTION_CLOSE_HOME);
                                homeReceiver = new HomeReceiver();
                                registerReceiver(homeReceiver, intentFilter);
                                intent = new Intent(rv.getContext(), UserActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 1://Scan

//                                intent = new Intent(rv.getContext(), CameraActivity.class);
//                                rv.getContext().startActivity(intent);
//                                return true;

                                intent = new Intent(rv.getContext(), Camera2Activity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 2://Lists
                                intent = new Intent(rv.getContext(), ListActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 3://News
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 4://Catalogue
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 5://Shop
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 6://Loyalty
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 7://Filleuls
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 8://Achievment
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 9://Parameter
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 10://Deconnexion
                                sessionManagerUser.deleteCurrentUser();
                                intentFilter = new IntentFilter(ACTION_CLOSE_HOME);
                                homeReceiver = new HomeReceiver();
                                registerReceiver(homeReceiver, intentFilter);
                                mAdapterL = new AdapterNavDrawerHome(ACCOUNT, ITEM_WACCOUNT);
                                return true;
                            default:
                                return false;
                        }
                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        mLayoutManagerL = new LinearLayoutManager(this);
        mRecyclerViewL.setLayoutManager(mLayoutManagerL);
        DrawerL = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggleL = new ActionBarDrawerToggle(this, DrawerL, toolbar,  R.string.connexion/*"OpenDr"*/, R.string.connexion/*"CloseDr"*/) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        DrawerL.setDrawerListener(mDrawerToggleL);
        mDrawerToggleL.syncState();

//        mRecyclerViewR.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                View child = rv.findChildViewUnder(e.getX(), e.getY());
//
//                if (child != null && mGestureDetector.onTouchEvent(e)) {
//                    Toast.makeText(HomeActivity.this, "EDIT[" +  "]", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//        });

        mLayoutManagerR = new LinearLayoutManager(this);
        mRecyclerViewR.setLayoutManager(mLayoutManagerR);
        DrawerR = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggleR = new ActionBarDrawerToggle(this, DrawerR, toolbar,  R.string.connexion/*"OpenDr"*/, R.string.connexion/*"CloseDr"*/) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        DrawerR.setDrawerListener(mDrawerToggleR);
        mDrawerToggleR.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (homeReceiver != null)
            unregisterReceiver(homeReceiver);
    }

    public class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_CLOSE_HOME)) {
                HomeActivity.this.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                Toast.makeText(HomeActivity.this, "Search !!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
