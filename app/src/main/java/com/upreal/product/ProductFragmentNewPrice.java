package com.upreal.product;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.upreal.R;
import com.upreal.geolocalisation.WebLocationManager;
import com.upreal.utils.Address;
import com.upreal.utils.SoapProductUtilManager;
import com.upreal.utils.SoapStoreManager;
import com.upreal.utils.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 12/11/15.
 */
public class ProductFragmentNewPrice extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Double latitude;
    private Double longitude;
    private List<List<String>> storeNearby;

    private int idProduct;

    public static final String TAG = ProductNewViewPagerAdapter.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_price, container, false);
        Bundle b = getArguments();
        latitude = b.getDouble("latitude");
        longitude = b.getDouble("longitude");
        idProduct = b.getInt("idProduct");

        recyclerView = (RecyclerView) v.findViewById(R.id.address_list);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        Toast.makeText(getActivity().getApplicationContext(), "latitude View[" + latitude + "] | longitude View[" + longitude + "]", Toast.LENGTH_SHORT).show();
        new RetrieveShopFromLocation().execute();

        return v;
    }

    private class RetrieveShopFromLocation extends AsyncTask<Void, Void, List<List<String>>> {


        @Override
        protected List<List<String>> doInBackground(Void... voids) {
            SoapStoreManager sm = new SoapStoreManager();
            storeNearby = sm.getProductByLocation(latitude, longitude, 999, idProduct);
            return storeNearby;
        }

        @Override
        protected void onPostExecute(List<List<String>> stores) {
            super.onPostExecute(stores);
            Toast.makeText(getActivity().getApplicationContext(), "Store.size" + storeNearby.size(), Toast.LENGTH_SHORT).show();
            mAdapter = new AdapterListNewStore(stores);
            recyclerView.setAdapter(mAdapter);
        }
    }



//    private class RetrievePriceByProduct extends AsyncTask<Void, Void, List<Address>> {
//
//        @Override
//        protected List<Address> doInBackground(Void... params) {
//
//            SoapProductUtilManager pum = new SoapProductUtilManager();
//
//            if (idProduct != 0) {
//                absAddresses = pum.getAddressByProduct(idProduct);
//                absPrices = pum.getPriceByProduct(idProduct);
//            }
//
//            if (absAddresses.isEmpty()) {
//                return null;
//            }
//            else {
//                Log.i(TAG, "Address:" + absAddresses.get(0).getAddress() + ", " + absAddresses.get(0).getPostalCode() + " " + absAddresses.get(0).getCity() + ", " + absAddresses.get(0).getCountry());
//            }
//
//            return absAddresses;
//        }
//
//        protected void onPostExecute(List<Address> addressesToGet) {
//            if (absAddresses != null && !absAddresses.isEmpty()) {
//                for (Address a : absAddresses) {
//                    Log.i(TAG, "Address:" + a.toString());
//                }
//            }
//            if (addressesToGet != null && !addressesToGet.isEmpty()) {
//                for (int i = 0; i < addressesToGet.size(); i++) {
//                    addMarker(absAddresses.get(i).getAddress2(), addressesToGet.get(i).getLatitude(), addressesToGet.get(i).getLongitude());
//                    // Retrieve distance between the two points
//                }
//
//                updateAddressList(absAddresses);
//                updateDistanceList(absDistances);
//                updatePriceList(absPrices);
//                mAdapterList.notifyDataSetChanged();
//            } else {
//                Toast.makeText(getApplicationContext(), "Magasin non trouvé.", Toast.LENGTH_SHORT).show();
//                Log.i(TAG, "No location found.");
//            }
//        }
//    }
}
