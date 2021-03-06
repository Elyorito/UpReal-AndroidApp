package com.upreal.product;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.upreal.R;
import com.upreal.view.SlidingTabLayout;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductSearchActivity extends AppCompatActivity {

    private CharSequence Title[];

    private Toolbar toolbar;

    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private ProductViewPagerSearchAdapter adapter;
    private String searchname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        searchname = getIntent().getStringExtra("searchname");

        Title = new CharSequence[]{getString(R.string.product), getString(R.string.user), getString(R.string.store)};
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.search);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ProductViewPagerSearchAdapter(getSupportFragmentManager(), Title, 3, searchname);
        mViewPager.setAdapter(adapter);

        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }
}