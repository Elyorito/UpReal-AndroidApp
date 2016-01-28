package com.upreal.product;

import android.app.Activity;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.upreal.geolocalisation.WebLocationManager;
import com.upreal.utils.FragmentCommentary;
import com.upreal.utils.LocationService;
import com.upreal.utils.Product;
import com.upreal.utils.SoapProductUtilManager;
import com.upreal.utils.SoapStoreManager;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 01/10/15.
 */
public class ProductNewViewPagerAdapter extends FragmentStatePagerAdapter implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    CharSequence Title[];
    int nbTab;
    Product mProduct;
    Bundle bundle;
    private Activity mActivity;

    public static final String TAG = ProductNewViewPagerAdapter.class.getSimpleName();

    private GoogleApiClient gClient;
    private LocationRequest mLocRequest;
    private Location mLocation;
    private WeakReference<Activity> activityWeakReference;

    private List<com.upreal.utils.Address> addresses = new ArrayList<>();
    private List<String> distances = new ArrayList<>();
    private List<String> prices = new ArrayList<>();

    public ProductNewViewPagerAdapter(FragmentManager fm, CharSequence mTitle[], int mNbTab, Product product, Activity activity, LocationService locationService) {
        super(fm);
        this.Title = mTitle;
        this.nbTab = mNbTab;
        this.mProduct = product;
        bundle = new Bundle();
        bundle.putParcelable("product", product);
        bundle.putInt("idProduct", product.getId());
        bundle.putDouble("latitude", locationService.latitude
        );
        bundle.putDouble("longitude", locationService.longitude);
        bundle.putInt("id", product.getId());
        bundle.putInt("type", 2);
        mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: //Info
                ProductFragmentInfo comd = new ProductFragmentInfo();
                comd.setArguments(bundle);
                return comd;
            case 1: //Prix
                ProductFragmentNewPrice pfnp = new ProductFragmentNewPrice();
                pfnp.setArguments(bundle);
                return pfnp;
            case 2: //Avis
                FragmentCommentary com = new FragmentCommentary();
                com.setArguments(bundle);
                return com;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Title[position];
    }

    @Override
    public int getCount() {
        return nbTab;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        mLocation = LocationServices.FusedLocationApi.getLastLocation(gClient);

        if (mLocation == null) {
            Log.i(TAG, "Trying to locate user.");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
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
        return Radius * c;
    }

    class RetrieveAddress extends AsyncTask<Void, Void, List<Address>> {

        @Override
        protected List<android.location.Address> doInBackground(Void... params) {

            List<android.location.Address> addressesToGet = new ArrayList<Address>();
            android.location.Address address;

            if (mProduct.getId() != 0) {
                SoapProductUtilManager pum = new SoapProductUtilManager();
                addresses = pum.getAddressByProduct(mProduct.getId());
                for (com.upreal.utils.Address a : addresses) {
                    Log.e(TAG, a.getAddress() + ", " + a.getPostalCode() + " " + a.getCity() + ", " + a.getCountry());
                }

                List<Double> pricesInD = pum.getPriceByProduct(mProduct.getId());

                for (Double d : pricesInD) {
                    prices.add(d.toString());
                    Log.e(TAG, d.toString());
                }
            }
            else if (false) {
                SoapStoreManager sm = new SoapStoreManager();
                addresses.add(sm.getAddressByStore(0));
            }

            if (addresses.isEmpty()) {
                return null;
            }

            for (com.upreal.utils.Address a : addresses) {
                address = WebLocationManager.getLocationInfo(a.getAddress() + ", " + a.getPostalCode() + " " + a.getCity() + ", " + a.getCountry()).get(0);
                if (address != null) {
                    Log.e(TAG, "Found:" + a.getAddress() + ", " + a.getPostalCode() + " " + a.getCity() + ", " + a.getCountry());
                    addressesToGet.add(address);
                }
            }
            return addressesToGet;
        }

        protected void onPostExecute(List<android.location.Address> addressesToGet) {
            if (addresses != null && !addresses.isEmpty()) {
                for (com.upreal.utils.Address a : addresses) {
                    Log.i(TAG, "Address:" + a.toString());
                }
            }
            if (addressesToGet != null && !addressesToGet.isEmpty()) {
                for (int i = 0; i < addressesToGet.size(); i++) {
                    distances.add("" + CalculationByDistance(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()),
                            new LatLng(addressesToGet.get(i).getLatitude(), addressesToGet.get(i).getLongitude())));
                }
            } else {
                Log.i(TAG, "No location found.");
            }

            bundle.putParcelableArrayList("addresses", (ArrayList<? extends Parcelable>) addresses);
            bundle.putStringArrayList("distances", (ArrayList<String>) distances);
            bundle.putStringArrayList("prices", (ArrayList<String>) prices);
        }
    }
}