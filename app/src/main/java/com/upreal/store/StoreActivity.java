package com.upreal.store;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.BlurImages;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreActivity extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageBlurred;
    private ImageView imageStore;
    private TabLayout tabLayout;

    private ViewPager mViewPager;

    private StoreViewPagerAdapter adapter;

    private Store store;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tabLayout = (TabLayout) findViewById(R.id.tabsproduct);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageBlurred = (ImageView) findViewById(R.id.imageproductblurred);
        imageStore = (ImageView) findViewById(R.id.imageproduct);
        store = getIntent().getExtras().getParcelable("liststore");

        collapsingToolbarLayout.setTitle(store.getName());
        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/Store/" + store.getPicture()).transform(new BlurImages(getApplicationContext(), 25)).into(imageBlurred);
        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/Store/" + store.getPicture()).transform(new CircleTransform()).into(imageStore);
        CharSequence tab[] = {"Info.", "Prix", "Avis"};
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new StoreViewPagerAdapter(getSupportFragmentManager(), tab, 3, store, this);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {

    }
}
