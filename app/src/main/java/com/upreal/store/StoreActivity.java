package com.upreal.store;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.Refresh;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreActivity extends AppCompatActivity implements View.OnClickListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SupportMapFragment map;
    private ImageView imageStore;
    private TabLayout tabLayout;
    private Activity activity;
    private Context context;

    private ViewPager mViewPager;

    private StoreViewPagerAdapter adapter;

    private Store store;

    private FloatingActionButton menu;
    private AlertDialog dialog;

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

        menu = (FloatingActionButton) findViewById(R.id.fab);
        menu.setOnClickListener(this);
        context = getApplicationContext();
        activity = this;

        final String[] option = new String[] { "Partager", "Rafraichir" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quel action voulez-vous faire ?");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Share
                                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Intent.ACTION_SEND);

                                i.putExtra(Intent.EXTRA_TEXT, "Venez voir le magasin : " + store.getName() + " sur UpReal");
                                i.setType("text/plain");
                                try {
                                    startActivity(Intent.createChooser(i, "Partager ce produit avec ..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(context, context.getString(R.string.need_mail_app)
                                            , Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1: // Refresh
                                new Refresh(activity, 3, store.getId()).execute();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        dialog = builder.create();
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
