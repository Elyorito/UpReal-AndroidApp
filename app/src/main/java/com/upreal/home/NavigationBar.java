package com.upreal.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.upreal.R;
import com.upreal.list.ListActivity;
import com.upreal.login.LoginActivity;
import com.upreal.scan.CameraActivity;
import com.upreal.scan.ScanActivity;
import com.upreal.user.UserActivity;
import com.upreal.utils.DividerItemDecoration;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.database.DatabaseHelper;

/**
 * Created by Kyosukke on 13/11/2015.
 */
public class NavigationBar extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private Activity activity;
    private NavigationBar mContext;
    private Toolbar toolbar;
    final GestureDetector mGestureDetector;
    private SessionManagerUser sessionManagerUser;

    private String ACCOUNT[];
    private String ITEM_WACCOUNT[];

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private Intent intent;

    public NavigationBar(Activity activity) {
        mContext = this;
        toolbar = (Toolbar) activity.findViewById(R.id.app_bar);
        mRecyclerView = (RecyclerView) activity.findViewById(R.id.RecyclerView_NavigationDrawer);
        mDrawer = (DrawerLayout) activity.findViewById(R.id.DrawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawer, toolbar,  R.string.connexion/*"OpenDr"*/, R.string.connexion/*"CloseDr"*/) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*
        toolbar.setBackgroundColor(activity.getResources().getColor(R.color.ColorTabs));
        toolbar.setTitleTextColor(activity.getResources().getColor(R.color.ColorTitle));
        */

        this.activity = activity;
        sessionManagerUser = new SessionManagerUser(activity.getApplicationContext());

        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(activity, null));
        mRecyclerView.setHasFixedSize(true);

        if (sessionManagerUser.isLogged()) {
            ITEM_WACCOUNT = new String[]{activity.getString(R.string.news),
                    activity.getString(R.string.scan),
                    activity.getString(R.string.list),
                    activity.getString(R.string.settings),
                    activity.getString(R.string.deconnexion)};
            mAdapter = new AdapterNavDrawerHome(sessionManagerUser.getRegisterLoginUser()[0], ITEM_WACCOUNT, activity.getApplicationContext(), sessionManagerUser.getUser());
        }
        else {
            ITEM_WACCOUNT = new String[]{activity.getString(R.string.news),
                    activity.getString(R.string.scan),
                    activity.getString(R.string.settings)};
            ACCOUNT = new String[]{activity.getString(R.string.connexion)};
            mAdapter = new AdapterNavDrawerConnectHome(ACCOUNT, ITEM_WACCOUNT);
        }

        mRecyclerView.addOnItemTouchListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mGestureDetector = new GestureDetector(activity, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());

        if (child != null && mGestureDetector.onTouchEvent(e)) {
            mDrawer.closeDrawers();
            //                Toast.makeText(HomeActivity.this, "Item :" + rv.getChildPosition(child), Toast.LENGTH_SHORT).show();
            if (sessionManagerUser.isLogged())
                switch (rv.getChildAdapterPosition(child)) {
                case 0://Connect
                    intent = new Intent(rv.getContext(), UserActivity.class);
                    rv.getContext().startActivity(intent);
                    return true;
                case 1://Home
                    intent = new Intent(rv.getContext(), HomeActivity.class);
                    rv.getContext().startActivity(intent);
                    activity.finish();
                    return true;
                case 2://Scan
                    startScan(rv.getContext());
                    return true;
                case 3://List
                    intent = new Intent(rv.getContext(), ListActivity.class);
                    rv.getContext().startActivity(intent);
                    return true;
                case 4://Settings
                    intent = new Intent(rv.getContext(), ParameterActivity.class);
                    rv.getContext().startActivity(intent);
                    return true;
                case 5://Disconnect
                    sessionManagerUser.deleteCurrentUser();
                    new DatabaseHelper(activity).deleteDataBase();
                    intent = new Intent(rv.getContext(), HomeActivity.class);
                    rv.getContext().startActivity(intent);
                    activity.finish();
                    return true;
                default:
                    return false;
            }
            else {
                switch (rv.getChildAdapterPosition(child)) {
                    case 0://Connect
                        intent = new Intent(rv.getContext(), LoginActivity.class);
                        rv.getContext().startActivity(intent);
                        return true;
                    case 1://Home
                        intent = new Intent(rv.getContext(), HomeActivity.class);
                        rv.getContext().startActivity(intent);
                        activity.finish();
                        return true;
                    case 2://Scan
                        startScan(rv.getContext());
                        return true;
                    case 3://Parameter
                        intent = new Intent(rv.getContext(), ParameterActivity.class);
                        rv.getContext().startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        }

        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private void startScan(final Context context) {
        View scanview = activity.getLayoutInflater().inflate(R.layout.dialog_scan_choice, null);
        ImageButton butQRcode = (ImageButton) scanview.findViewById(R.id.but_qrcode);
        ImageButton butScan = (ImageButton) scanview.findViewById(R.id.but_scanir);
        butQRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), ScanActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        butScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), CameraActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.scan).setMessage(R.string.choose_scan_mode).setView(scanview).create().show();
    }
}
