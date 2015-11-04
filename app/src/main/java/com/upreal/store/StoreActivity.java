package com.upreal.store;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreActivity extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SupportMapFragment map;
    private ImageView imageStore;
    private TabLayout tabLayout;

    private ViewPager mViewPager;

    private StoreViewPagerAdapter adapter;

    private Store store;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        tabLayout = (TabLayout) findViewById(R.id.tabsproduct);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageStore = (ImageView) findViewById(R.id.imageproduct);
        store = getIntent().getExtras().getParcelable("store");

        createMapView();

        collapsingToolbarLayout.setTitle(store.getName());
        Picasso.with(getApplicationContext()).load("http://163.5.84.202/Symfony/web/images/Store/" + store.getPicture()).transform(new CircleTransform()).into(imageStore);
        CharSequence tab[] = {"Info", "Produits", "Avis"};

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new StoreViewPagerAdapter(getSupportFragmentManager(), tab, 3, store);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void createMapView() {
        try {
            if (map == null) {
                map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view));
                if (map == null) {
                    Toast.makeText(getApplicationContext(), "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException e) {
            Log.e("mapApp", e.toString());
        }
    }

    private void addMarker(String label, double latitude, double longitude) {
        if (map != null) {
            Log.e("StoreActivity", "Adding new marker " + label + ".");
            map.getMap().addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(label).draggable(false));
        } else {
            Log.e("StoreActivity", "Map is null.");
        }
    }

    @Override
    public void onClick(View v) {

    }
}
