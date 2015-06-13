package com.upreal.upreal.geolocalisation;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.upreal.upreal.R;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.upreal.upreal.utils.Address;

import java.util.List;

/**
 * Created by Kyosukke on 13/06/2015.
 */
public class GeolocalisationActivity extends ActionBarActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final String TAG = GeolocalisationActivity.class.getSimpleName();

    private GoogleApiClient gClient;
    private LocationRequest mLocRequest;
    private GoogleMap map;

    private Toolbar toolbar;

    private RecyclerView mRecyclerViewList;
    private RecyclerView.Adapter mAdapterList;
    private RecyclerView.LayoutManager mLayoutManager;
    private String base_list[];
    private String delimiter[];

    private List<Address> stores;
    private int idProduct = -1;
    private int idStore = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocalisation);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        createMapView();
        addMarker("Test", 0, 0);

        gClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        idProduct = getIntent().getExtras().getInt("id_product");
        idStore = getIntent().getExtras().getInt("id_store");

        if (idProduct != -1) {

        }
        else if (idStore != -1) {

        }

        base_list = new String[] {getString(R.string.store_name),
                getString(R.string.store_distance),
                getString(R.string.price)};
        delimiter = new String[] {getString(R.string.customized_list)};

        mRecyclerViewList = (RecyclerView) findViewById(R.id.address_list);
        mRecyclerViewList.setHasFixedSize(true);
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        mAdapterList = new AdapterListStore(base_list, delimiter);
        mRecyclerViewList.setAdapter(mAdapterList);
        mRecyclerViewList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        gClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(gClient, this);
            gClient.disconnect();
        }
    }

    private void createMapView() {
        try {
            if (map == null) {
                map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_view)).getMap();

                if (map == null) {
                    Toast.makeText(getApplicationContext(), "Error creating map", Toast.LENGTH_SHORT).show();
                }

            }
        }
        catch (NullPointerException e) {
            Log.e("mapApp", e.toString());
        }
    }

    private void addMarker(String label, double latitude, double longitude) {
        if (map != null) {
            map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(label).draggable(false));
        }
    }

    private void updateCurrentPosition(Location location) {
        addMarker("Me", location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        Location location = LocationServices.FusedLocationApi.getLastLocation(gClient);

        if (location != null) {
            updateCurrentPosition(location);
        }
        else {
            LocationServices.FusedLocationApi.requestLocationUpdates(gClient, mLocRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }
            catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateCurrentPosition(location);
    }
}
