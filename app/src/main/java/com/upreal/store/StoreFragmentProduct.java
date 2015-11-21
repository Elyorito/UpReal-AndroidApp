package com.upreal.store;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.upreal.R;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.Product;
import com.upreal.utils.SoapProductManager;
import com.upreal.utils.SoapProductUtilManager;
import com.upreal.utils.StoreSell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class StoreFragmentProduct extends Fragment {

    private ConnectionDetector cd;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> listProduct;
    private List<StoreSell> listStoreSell;

    private int idStore = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_product_commentary, container, false);
        Bundle b = getArguments();

        cd = new ConnectionDetector(getContext());
        idStore = b.getInt("idStore");

        listProduct = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_commentary);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        if (cd.isConnectedToInternet()) {
            new RetrieveStoreSell().execute();
        } else
            Toast.makeText(getContext(), getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();

        return v;
    }

    private class RetrieveStoreSell extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            SoapProductUtilManager pum = new SoapProductUtilManager();

            listStoreSell = pum.getProductByStore(idStore);

            SoapProductManager pm = new SoapProductManager();

            for (StoreSell s : listStoreSell) {
                listProduct.add(pm.getProductInfo(s.getIdProduct()));
            }

            return listStoreSell.size();
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            if (listStoreSell != null && listProduct != null || i > 0) {
                mAdapter = new AdapterStoreProduct(getActivity().getApplicationContext(), listStoreSell, listProduct);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }
}
