package com.upreal.upreal.product;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.upreal.R;

import com.upreal.upreal.geolocalisation.GeolocalisationActivity;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.SoapProductManager;
import com.upreal.upreal.utils.database.DatabaseHelper;
import com.upreal.upreal.utils.database.DatabaseQuery;
import com.upreal.upreal.view.SlidingTabLayout;


/**
 * Created by Elyo on 11/02/2015.
 */
public class ProductActivity extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private ProductViewPagerAdapter adapter;
    private SlidingTabLayout mSlidingTabLayout;
    private Product prod;
    private CharSequence title;

    private TextView prodName;
    private TextView prodCategorie;
    private TextView prodShortDesc;
    private ImageView prodPicture;

    private Button geoloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        prodName = (TextView) findViewById(R.id.product_name);/*
        prodCategorie = (TextView) findViewById(R.id.product_categorie);
        prodShortDesc = (TextView) findViewById(R.id.product_desc);*/
        prodPicture = (ImageView) findViewById(R.id.product_picture);

        geoloc = (Button) findViewById(R.id.geoloc);
        geoloc.setOnClickListener(this);

        prod = getIntent().getExtras().getParcelable("listprod");

        title = new String(prod.getName());
        prodName.setText(prod.getName());
/*
        prodCategorie.setText("Categorie");
        prodShortDesc.setText("Short Description empty");
*/
        CharSequence Tab[] = {getString(R.string.commentary), getString(R.string.social), getString(R.string.options)};
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ProductViewPagerAdapter(getSupportFragmentManager(), Tab, 3, prod);
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

        new RetrievePicture().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.geoloc:
                Intent intent = new Intent(v.getContext(), GeolocalisationActivity.class);
                intent.putExtra("id_product", prod.getId());
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

            String path = pm.getPicture(prod.getId(), 0);
            return path;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            prodPicture.setImageBitmap(BitmapFactory.decodeFile(path, options));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recreate();
    }
}