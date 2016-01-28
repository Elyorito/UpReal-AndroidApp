package com.upreal.product;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.upreal.R;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.SoapStoreManager;

import java.util.List;

/**
 * Created by Elyo on 12/11/15.
 */
public class ProductFragmentNewPrice extends android.support.v4.app.Fragment {
    private ConnectionDetector cd;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Double latitude;
    private Double longitude;

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
        cd = new ConnectionDetector(recyclerView.getContext());
        if (cd.isConnectedToInternet())
            new RetrieveShopFromLocation().execute();
        else
            Toast.makeText(recyclerView.getContext(), getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();

        return v;
    }

    private class RetrieveShopFromLocation extends AsyncTask<Void, Void, List<List<String>>> {
        
        @Override
        protected List<List<String>> doInBackground(Void... voids) {
            SoapStoreManager sm = new SoapStoreManager();

            List<List<String>> storeNearby = sm.getProductByLocation(latitude, longitude, 2, idProduct);
            return storeNearby;
        }

        @Override
        protected void onPostExecute(List<List<String>> stores) {
            super.onPostExecute(stores);
            mAdapter = new AdapterListNewStore(stores);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
