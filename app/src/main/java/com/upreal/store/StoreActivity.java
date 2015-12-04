package com.upreal.store;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.Address;
import com.upreal.utils.CircleTransform;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.IPDefiner;
import com.upreal.utils.Refresh;
import com.upreal.utils.SoapStoreManager;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreActivity extends AppCompatActivity implements View.OnClickListener {
    private ConnectionDetector cd;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SupportMapFragment map;
    private MapFragment map_store;
    private ImageView imageStore;
    private TabLayout tabLayout;
    private Activity activity;
    private Context context;

    private ViewPager mViewPager;

    private StoreViewPagerAdapter adapter;

    private Store store;

    private FloatingActionButton menu;
    private AlertDialog dialog;
    private Address storeAddress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        context = getApplicationContext();
        cd = new ConnectionDetector(context);
        tabLayout = (TabLayout) findViewById(R.id.tabsproduct);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        imageStore = (ImageView) findViewById(R.id.imageproduct);
        store = getIntent().getExtras().getParcelable("store");
        new RetreiveAddressStore().execute();

        createMapView();

        collapsingToolbarLayout.setTitle(store.getName());
        if (cd.isConnectedToInternet()) {
            Picasso.with(getApplicationContext()).load(new IPDefiner().getIP() + "Symfony/web/images/Store/" + store.getPicture()).transform(new CircleTransform()).into(imageStore);
        } else
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
        CharSequence tab[] = {"Info", "Produits", "Avis"};

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new StoreViewPagerAdapter(getSupportFragmentManager(), tab, 3, store);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        menu = (FloatingActionButton) findViewById(R.id.fab);
        menu.setOnClickListener(this);
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
                                if (cd.isConnectedToInternet()) {
                                    new Refresh(activity, 3, store.getId()).execute();
                                }
                                else
                                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
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
//            if (map == null) {
//                map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_store));
//                if (map == null) {
//                    Toast.makeText(getApplicationContext(), "Error creating map", Toast.LENGTH_SHORT).show();
//                }
            if (map_store == null) {
                map_store = (MapFragment) getFragmentManager()
                        .findFragmentById(R.id.map_store);
                map_store.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
//                        addMarker(store.getName(), storeAddress.getLatitude(), storeAddress.getLongitude());
                    }
                });
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

    private class RetreiveAddressStore extends AsyncTask<Void, Void, Address> {

        @Override
        protected Address doInBackground(Void... params) {
            SoapStoreManager sm = new SoapStoreManager();
            storeAddress = sm.getAddressByStore(store.getId());
            return storeAddress;
        }

        @Override
        protected void onPostExecute(Address address) {
            super.onPostExecute(address);
            addMarker(store.getName(), storeAddress.getLatitude(), storeAddress.getLongitude());
        }
    }
}
