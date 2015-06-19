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

import java.text.DecimalFormat;
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


    private List<Address> absAddresses = new ArrayList<>();
    private List<String> absDistances = new ArrayList<>();
    private List<String> absPrices = new ArrayList<>();

    private List<Address> addresses = new ArrayList<>();
    private List<String> distances = new ArrayList<>();
    private List<String> prices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocalisation);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        seeAll = (Button) findViewById(R.id.see_all);

        createMapView();

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

        } else if (idStore != -1) {

        }

        absAddresses.add(new Address(1, "24 Rue Pasteur", "", "France", "Le Kremlin Bicêtre", 94270));
        absDistances.add("20");
        absPrices.add("20");
        absAddresses.add(new Address(2, "85 Rue du Jardin public", "", "France", "Bordeaux", 33000));
        absDistances.add("20");
        absPrices.add("20");
        absAddresses.add(new Address(3, "5-9 Rue du Palais Rihour", "", "France", "Lille", 59000));
        absDistances.add("20");
        absPrices.add("20");
        absAddresses.add(new Address(4, "156 Rue Paul Bert", "", "France", "Lyon", 69003));
        absDistances.add("20");
        absPrices.add("20");

        updateAddressList(absAddresses);
        updateDistanceList(absDistances);
        updatePriceList(absPrices);

        mRecyclerViewList = (RecyclerView) findViewById(R.id.address_list);
        mRecyclerViewList.setHasFixedSize(true);
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(this));
        mAdapterList = new AdapterListStore(addresses, distances, prices);
        mRecyclerViewList.setAdapter(mAdapterList);
        mRecyclerViewList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                int pos = rv.getChildAdapterPosition(rv.findChildViewUnder(e.getX(), e.getY()));

                Log.i(TAG, "Item touched at " + pos + ".");
/*                Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
                intent.putExtra("id_adress", addresses.get(pos).getId());
                getApplicationContext().startActivity(intent);*/
            }
        });

        new RetrieveAddress().execute(addresses);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, "Marker " + marker.getTitle() + " touched.");
                map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                marker.showInfoWindow();
                for (int i = 0; i < absAddresses.size(); i++) {
                    if (marker.getTitle().contains(absAddresses.get(i).getAddress())) {
                        updateAddressList(adressToList(absAddresses.get(i)));
                        updateDistanceList(stringToList(absDistances.get(i)));
                        updatePriceList(stringToList(absPrices.get(i)));
                        break;
                    }
                }
                mAdapterList.notifyDataSetChanged();
                return true;
            }
        });

        seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddressList(absAddresses);
                updateDistanceList(absDistances);
                updatePriceList(absPrices);
                mAdapterList.notifyDataSetChanged();
            }
        });
    }

    private List<Address> adressToList(Address adr) {
        List<Address> tmp = new ArrayList<Address>();

        tmp.add(adr);
        return tmp;
    }

    private List<String> stringToList(String s) {
        List<String> tmp = new ArrayList<String>();

        tmp.add(s);
        return tmp;
    }

    private void updateAddressList(List<Address> adr) {
        addresses.clear();
        for (Address a : adr) {
            addresses.add(a);
        }
    }

    private void updateDistanceList(List<String> str) {
        distances.clear();
        for (String s : str) {
            distances.add(s);
        }
    }

    private void updatePriceList(List<String> str) {
        prices.clear();
        for (String s : str) {
            prices.add(s);
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
        } catch (NullPointerException e) {
            Log.e("mapApp", e.toString());
        }
    }

    private void addMarker(String label, double latitude, double longitude) {
        if (map != null) {
            Log.i(TAG, "Adding new marker " + label + ".");
            map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(label).draggable(false));
        } else {
            Log.i(TAG, "Map is null.");
        }
    }

    private void updateCurrentPosition(Location location) {
        Log.i(TAG, "Position updated.");
        addMarker("Me", location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }

    private double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        Location location = LocationServices.FusedLocationApi.getLastLocation(gClient);

        if (location != null) {
            updateCurrentPosition(location);
        } else {
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
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
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
                    // Retrieve distance between the two points
                }
            } else {
                Log.i(TAG, "No location found.");
            }
        }
    }
}
