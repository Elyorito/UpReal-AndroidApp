package com.upreal.upreal.product;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.upreal.upreal.R;

import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.RateComment;
import com.upreal.upreal.utils.SoapGlobalManager;
import com.upreal.upreal.utils.SoapProductManager;
import com.upreal.upreal.view.SlidingTabLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Elyo on 11/02/2015.
 */
public class ProductActivity extends ActionBarActivity {

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private ProductViewPagerAdapter adapter;
    private SlidingTabLayout mSlidingTabLayout;
    private Product prod;
    private CharSequence title;

    private TextView prodName;
    private TextView prodCategorie;
    private TextView prodShortDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        prodName = (TextView) findViewById(R.id.product_name);/*
        prodCategorie = (TextView) findViewById(R.id.product_categorie);
        prodShortDesc = (TextView) findViewById(R.id.product_desc);
*/
        prod = getIntent().getExtras().getParcelable("listprod");

        title = new String(prod.getName());
        prodName.setText(prod.getName());
/*
        prodCategorie.setText("Categorie");
        prodShortDesc.setText("Short Description empty");
*/
        CharSequence Tab[] = {getString(R.string.commentary), getString(R.string.social), getString(R.string.options)};
        toolbar = (Toolbar) findViewById(R.id.app_bar);
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