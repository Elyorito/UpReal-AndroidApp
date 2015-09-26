package com.upreal.upreal.home;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.upreal.upreal.R;

import com.upreal.upreal.list.ListActivity;
import com.upreal.upreal.login.LoginActivity;
import com.upreal.upreal.scan.Camera2Activity;
import com.upreal.upreal.scan.CameraActivity;
import com.upreal.upreal.scan.GetProductActivity;
import com.upreal.upreal.user.UserActivity;
import com.upreal.upreal.utils.Article;
import com.upreal.upreal.utils.DividerItemDecoration;
import com.upreal.upreal.utils.SessionManagerUser;
import com.upreal.upreal.utils.SoapGlobalManager;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    //RecyclerView NavDrawerL
    private RecyclerView mRecyclerViewL;
    private RecyclerView.Adapter mAdapterL;
    private RecyclerView.LayoutManager mLayoutManagerL;
    private DrawerLayout DrawerL;
    private ActionBarDrawerToggle mDrawerToggleL;

    private AlertDialog.Builder builder;

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

    public HomeActivity(){}
    private SessionManagerUser sessionManagerUser;

    // Camera
    private static final int ACTIVITY_START_CAMERA = 0;
    private String mImageFileLocation = "";

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;
    private DatabaseQuery mDbQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_news_historic);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        tablayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        sessionManagerUser = new SessionManagerUser(getApplicationContext());

        mDbHelper = new DatabaseHelper(this);
        mDbQuery = new DatabaseQuery(mDbHelper);
        mDatabase = mDbHelper.openDataBase();

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
                this.getString(R.string.deconnexion)};

        ACCOUNT = new String[]{getString(R.string.connexion)};

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
        /*RecyclerView MainView Home OLD*/
/*
        mRecyclerViewHome = (RecyclerView) findViewById(R.id.RecyclerView_Home);
        mRecyclerViewHome.setHasFixedSize(true);
        mRecyclerViewHome.setLayoutManager(new GridLayoutManager(this, 1));
//        mAdapterHome = new AdapterHome(CARDHOME, HomeActivity.this);
        mAdapterHome = new AdapterHome(PRODUCTREDUC, PRODUCTREDUCPRICE, HomeActivity.this);
        mRecyclerViewHome.setAdapter(mAdapterHome);

        mViewpager_slideshow = (ViewPager) findViewById(R.id.viewpager_home);
        mAdapterSlide = new SlideViewPagerAdapter(this);
        mViewpager_slideshow.setAdapter(mAdapterSlide);
*/
        /*RecyclerView MainView News*/

//        mRecyclerViewHome = (RecyclerView) findViewById(R.id.RecyclerView_Home);
//        mRecyclerViewHome.setHasFixedSize(true);
//        mRecyclerViewHome.setLayoutManager(new GridLayoutManager(this, 1));
//        String Title[] = {"Welcome to upreal !","Les dernieres promotion du mois de juillet disponible !", "Fusion entre UpReal et Dealabs !"};
//        int type[] = {1, 2, 1};
//        String imagepath[] = {"path1", "path2", "path3"};
//        new RetreiveNews().execute();


      /*RecyclerView NavigDrawL*/

        mRecyclerViewL = (RecyclerView) findViewById(R.id.RecyclerView_NavigationDrawer);
        mRecyclerViewL.addItemDecoration(
                new DividerItemDecoration(this, null));
        mRecyclerViewL.setHasFixedSize(true);
        if (toggleAccount)
            mAdapterL = new AdapterNavDrawerHome(sessionManagerUser.getRegisterLoginUser()[0], ITEM_WACCOUNT, getApplicationContext(), sessionManagerUser.getUser());
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
    //                Toast.makeText(HomeActivity.this, "Item :" + rv.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    if (!sessionManagerUser.isLogged()) {
                        Toast.makeText(HomeActivity.this, R.string.not_logged, Toast.LENGTH_SHORT).show();
                        switch (rv.getChildPosition(child)) {
                            case 0://Connect
                                intentFilter = new IntentFilter(ACTION_CLOSE_HOME);
                                homeReceiver = new HomeReceiver();
                                registerReceiver(homeReceiver, intentFilter);
                                intent = new Intent(rv.getContext(), LoginActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 1: //Scan
                                View scanview = getLayoutInflater().inflate(R.layout.dialog_scan_choice, null);
                                ImageButton butQRcode = (ImageButton) scanview.findViewById(R.id.but_qrcode);
                                ImageButton butScan = (ImageButton) scanview.findViewById(R.id.but_scanir);
                                butQRcode.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent = new Intent(v.getContext(), CameraActivity.class);
                                        v.getContext().startActivity(intent);
                                    }
                                });

                                butScan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

/*
                                        intent = new Intent(v.getContext(), Camera2Activity.class);
                                        intent.putExtra("type", "scan");
                                        v.getContext().startActivity(intent);
*/
                                        intent = new Intent();
                                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                        File photoFile = null;
                                        try {
                                            photoFile = createImageFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        startActivityForResult(intent, ACTIVITY_START_CAMERA);
                                    }
                                });
                                builder = new AlertDialog.Builder(rv.getContext());
                                builder.setTitle(R.string.scan).setMessage(R.string.choose_scan_mode).setView(scanview).create().show();
               /*                 intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);*/
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
                            case 3://News
//                                Toast.makeText(rv.getContext(), "News Not Implemented",Toast.LENGTH_SHORT).show();
                                /*intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);*/
                                return true;
                            case 4://Catalogue
  //                              Toast.makeText(rv.getContext(), "Catalogue Not Implemented",Toast.LENGTH_SHORT).show();
                                /*intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);*/
                                return true;
                            case 5://Shop
    //                            Toast.makeText(rv.getContext(), "Shop Not Implemented",Toast.LENGTH_SHORT).show();
                                /*
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
*/
                                return true;
                            case 6://Loyalty
      //                          Toast.makeText(rv.getContext(), "Loyalty Not Implemented",Toast.LENGTH_SHORT).show();
                                /*
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
*/
                                return true;
                            case 7://Filleuls
        //                        Toast.makeText(rv.getContext(), "Sponsorship Not Implemented",Toast.LENGTH_SHORT).show();
                                /*
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
*/
                                return true;
                            case 8://Achievement
                       //         Toast.makeText(rv.getContext(), "Achievement Not Implemented",Toast.LENGTH_SHORT).show();
                                /*
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
*/
                                return true;

                            case 9://Parameter
                                intent = new Intent(rv.getContext(), ParameterActivity.class);
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
                                homeReceiver = new  HomeReceiver();
                                registerReceiver(homeReceiver, intentFilter);
                                intent = new Intent(rv.getContext(), UserActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 1://Scan
                                View scanview = getLayoutInflater().inflate(R.layout.dialog_scan_choice, null);
                                ImageButton butQRcode = (ImageButton) scanview.findViewById(R.id.but_qrcode);
                                ImageButton butScan = (ImageButton) scanview.findViewById(R.id.but_scanir);
                                butQRcode.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent = new Intent(v.getContext(), CameraActivity.class);
                                        v.getContext().startActivity(intent);
                                    }
                                });

                                butScan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent = new Intent();
                                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                        File photoFile = null;
                                        try {
                                            photoFile = createImageFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                        startActivityForResult(intent, ACTIVITY_START_CAMERA);
/*                                        intent = new Intent(v.getContext(), Camera2Activity.class);
                                        intent.putExtra("type", "scan");
                                        v.getContext().startActivity(intent);*/
                                    }
                                });
                                builder = new AlertDialog.Builder(rv.getContext());
                                builder.setTitle(R.string.scan).setMessage(R.string.choose_scan_mode).setView(scanview).create().show();
                                return true;
                            case 2://Lists
                                intent = new Intent(rv.getContext(), ListActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 3://News
//                                Toast.makeText(rv.getContext(), "News Not Implemented", Toast.LENGTH_SHORT).show();
                                /*intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);*/
                                return true;
                            case 4://Catalogue
/*
                                intent = new Intent(rv.getContext(), TestNews.class);
                                rv.getContext().startActivity(intent);
                                Toast.makeText(rv.getContext(), "Catalogue Not Implemented",Toast.LENGTH_SHORT).show();
*/
                                /*intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);*/
                                return true;
                            case 5://Shop
  //                              Toast.makeText(rv.getContext(), "Shop Not Implemented",Toast.LENGTH_SHORT).show();
                                /*
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
*/
                                return true;
                            case 6://Loyalty
    //                            Toast.makeText(rv.getContext(), "Loyalty Not Implemented",Toast.LENGTH_SHORT).show();
                                /*
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
*/
                                return true;
                            case 7://Filleuls
      //                          Toast.makeText(rv.getContext(), "Sponsorship Not Implemented",Toast.LENGTH_SHORT).show();
                                /*
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
*/
                                return true;
                            case 8://Achievement
        //                        Toast.makeText(rv.getContext(), "Achievment Not Implemented",Toast.LENGTH_SHORT).show();
                                /*
                                intent = new Intent(rv.getContext(), CameraActivity.class);
                                rv.getContext().startActivity(intent);
*/
                                return true;
                            case 9://Parameter
                                intent = new Intent(rv.getContext(), ParameterActivity.class);
                                rv.getContext().startActivity(intent);
                                return true;
                            case 10://Deconnexion
                                sessionManagerUser.deleteCurrentUser();
                                intentFilter = new IntentFilter(ACTION_CLOSE_HOME);
                                homeReceiver = new HomeReceiver();
                                registerReceiver(homeReceiver, intentFilter);
                                mAdapterL = new AdapterNavDrawerHome(sessionManagerUser.getRegisterLoginUser()[0], ITEM_WACCOUNT, getApplicationContext(), sessionManagerUser.getUser());
                                mDbHelper.deleteDataBase();
                                recreate();
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

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

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

    File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);
        mImageFileLocation = image.getAbsolutePath();

        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_START_CAMERA && resultCode == RESULT_OK) {
/*          Thumbnail
            Bundle extras = data.getExtras();
            Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
*/
            Toast.makeText(this, "ActivityResult", Toast.LENGTH_SHORT).show();
            // Get Bitmap from File
            Bitmap photoCapturedBitmap = BitmapFactory.decodeFile(mImageFileLocation);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoCapturedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            Intent intent = new Intent(this, GetProductActivity.class);
            intent.putExtra("bytes", byteArray);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void setupViewpager(ViewPager viewpager) {
        AdapterViewPagerHome adapter = new AdapterViewPagerHome(getSupportFragmentManager(), new String[]{"News", "Fil d'actualité"});
        viewpager.setAdapter(adapter);
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
                HomeActivity.this.recreate();
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
                if (!DrawerR.isDrawerVisible(Gravity.RIGHT))
                    DrawerR.openDrawer(Gravity.RIGHT);
                else
                    DrawerR.closeDrawers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
