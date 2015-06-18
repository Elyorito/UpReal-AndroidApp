package com.upreal.upreal.geolocalisation;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.upreal.upreal.R;
import com.upreal.upreal.utils.Address;

import java.util.ArrayList;
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
    private Button seeAll;

    private RecyclerView mRecyclerViewList;
    private RecyclerView.Adapter mAdapterList;

    private int idProduct = -1;
    private int idStore = -1;

    private List<Address> stores = new ArrayList<>();
    private List<String> distances = new ArrayList<>();
    private List<String> prices = new ArrayList<>();

    private List<Address> storeToList(Address store) {
        List<Address> tmp = new ArrayList<Address>();

        tmp.add(store);
        return tmp;
    }

    private List<String> stringToList(String s) {
        List<String> tmp = new ArrayList<String>();

        tmp.add(s);
        return tmp;
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
            Log.i(TAG, "Adding new marker " + label + ".");
            map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(label).draggable(false));
        }
        else {
            Log.i(TAG, "Map is null.");
        }
    }

    private void updateCurrentPosition(Location location) {
        Log.i(TAG, "Position updated.");
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
            Log.i(TAG, "Trying to locate user.");
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

    class RetrieveAddress extends AsyncTask<List<Address>, Void, List<android.location.Address>> {

        @Override
        protected List<android.location.Address> doInBackground(List<Address>... params) {

            List<android.location.Address> addresses = new ArrayList<android.location.Address>();
            android.location.Address address;

            for (Address a : params[0]) {
                address = WebLocationManager.getLocationInfo(a.getAddress() + ", " + a.getPostalCode() + " " + a.getCity() + ", " + a.getCountry()).get(0);
                if (address != null) {
                    addresses.add(address);
                }
            }
            return addresses;
        }

        protected void onPostExecute(List<android.location.Address> addresses) {
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude())));
            if (addresses != null && !addresses.isEmpty()) {
                for (android.location.Address a : addresses) {
                    addMarker(a.getAddressLine(0), a.getLatitude(), a.getLongitude());
                }
            }
            else {
                Log.i(TAG, "No location found.");
            }
        }
    }
}
