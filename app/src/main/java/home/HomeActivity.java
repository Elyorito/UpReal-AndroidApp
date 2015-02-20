package home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.upreal.upreal.R;

import product.ProductActivity;

import static android.view.GestureDetector.*;

/**
 * Created by Elyo on 03/02/2015.
 */
public class HomeActivity extends ActionBarActivity {


    private Toolbar toolbar;

    //RecyclerView Home
    private RecyclerView mRecyclerViewHome;
    private RecyclerView.Adapter mAdapterHome;
    private RecyclerView.LayoutManager mLayoutManagerHome;
    private DrawerLayout DrawerHome;

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


    private String CARDHOME[];
    private String ITEM_WTACCOUNT[];
    private String ITEM_WACCOUNT[];
    private String ACCOUNT[];

    private static boolean toggleAccount = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        CARDHOME = new String[]{getString(R.string.profile), getString(R.string.scan),getString(R.string.news), getString(R.string.loyalty), getString(R.string.list), getString(R.string.acheivement)};
        ITEM_WTACCOUNT = new String[]{this.getString(R.string.scan),this.getString(R.string.history_product),
                this.getString(R.string.list),this.getString(R.string.news),
                this.getString(R.string.catalog), this.getString(R.string.shop), this.getString(R.string.services)};
        ITEM_WACCOUNT = new String[]{this.getString(R.string.scan),this.getString(R.string.history_product),
                this.getString(R.string.list),this.getString(R.string.news),
                this.getString(R.string.catalog), this.getString(R.string.shop), this.getString(R.string.loyalty_cards), this.getString(R.string.parrainage), this.getString(R.string.acheivement), this.getString(R.string.services)};
        ACCOUNT = new String[]{getString(R.string.connexion)};

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        /*RecyclerView MainView Home*/
        mRecyclerViewHome = (RecyclerView) findViewById(R.id.RecyclerView_Home);
        mRecyclerViewHome.setHasFixedSize(true);
        mRecyclerViewHome.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapterHome = new AdapterHome(CARDHOME, HomeActivity.this);
        mRecyclerViewHome.setAdapter(mAdapterHome);

        /*RecyclerView NavigDrawL*/

        mRecyclerViewL = (RecyclerView) findViewById(R.id.RecyclerView_NavigationDrawer);
        mRecyclerViewL.setHasFixedSize(true);
        if (!toggleAccount)
            mAdapterL = new AdapterNavDrawerHome(ACCOUNT, ITEM_WACCOUNT);
        else
            mAdapterL = new AdapterNavDrawerConnectHome(ACCOUNT, ITEM_WTACCOUNT);
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
                    return true;
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
                Toast.makeText(HomeActivity.this, "Search!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this.getApplicationContext(), ProductActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
