package com.upreal.upreal.store;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.upreal.R;
import com.upreal.upreal.geolocalisation.GeolocalisationActivity;
import com.upreal.upreal.utils.SoapProductManager;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;
import com.upreal.upreal.utils.Store;
import com.upreal.upreal.view.SlidingTabLayout;

/**
 * Created by Kyosukke on 20/06/2015.
 */
public class StoreActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String TAG = StoreActivity.class.getSimpleName();
    private Toolbar toolbar;

    private ViewPager mViewPager;
    private StoreViewPagerAdapter adapter;
    private SlidingTabLayout mSlidingTabLayout;
    private Store store;
    private CharSequence title;

    private TextView storeName;
    private TextView storeWeb;
    private TextView storeCompany;
    private ImageView storePicture;

    private Button geoloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        storeName = (TextView) findViewById(R.id.store_name);
        storeWeb = (TextView) findViewById(R.id.store_website);
        storeCompany = (TextView) findViewById(R.id.store_company);
        storePicture = (ImageView) findViewById(R.id.store_picture);

        geoloc = (Button) findViewById(R.id.geoloc);
        geoloc.setOnClickListener(this);

        store = getIntent().getExtras().getParcelable("store");

        if (store == null) {
            Log.i(TAG, "Store in argument is null.");
            return ;
        }

        title = new String(store.getName());
        storeName.setText(store.getName());
        storeWeb.setText(store.getWebsite());
        storeCompany.setText(store.getCompanyName());

        CharSequence tab[] = {getString(R.string.commentary), getString(R.string.social), getString(R.string.options)};
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new StoreViewPagerAdapter(getSupportFragmentManager(), tab, 3, store);
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

//        new RetrievePicture().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            SQLiteDatabase mDatabase;
            DatabaseHelper mDbHelper;
            DatabaseQuery mDbQuery;
            mDbHelper = new DatabaseHelper(getApplicationContext());
            mDbQuery = new DatabaseQuery(mDbHelper);
            mDatabase = mDbHelper.openDataBase();
            mDbHelper.deleteDataBase();
            mDatabase.close();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.geoloc:
                Intent intent = new Intent(v.getContext(), GeolocalisationActivity.class);
                Log.i(TAG, "Store id:" + store.getId() + ".");
                intent.putExtra("id_store", store.getId());
                v.getContext().startActivity(intent);
                break ;
            default:
                break ;
        }
    }

    class RetrievePicture extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            SoapProductManager pm = new SoapProductManager();

            String path = pm.getPicture(store.getId(), 1);
            return path;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            storePicture.setImageURI(Uri.parse(path));
        }
    }
}