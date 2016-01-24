package com.upreal.user;

import android.app.Activity;
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
import com.upreal.utils.UserSell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 04/11/2015.
 */
public class UserFragmentProduct extends Fragment {
    private ConnectionDetector cd;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> listProduct;
    private List<UserSell> listUserSell;

    private int idUser = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_product_commentary, container, false);
        Bundle b = getArguments();

        idUser = b.getInt("idUser");

        listProduct = new ArrayList<>();

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_commentary);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        cd = new ConnectionDetector(v.getContext());
        if (cd.isConnectedToInternet())
            new RetrieveUserSell().execute();
        else
            Toast.makeText(v.getContext(), getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();

        return v;
    }

    private class RetrieveUserSell extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            SoapProductUtilManager pum = new SoapProductUtilManager();

            listUserSell = pum.getProductByUser(idUser);

            SoapProductManager pm = new SoapProductManager();

            for (UserSell s : listUserSell) {
                listProduct.add(pm.getProductInfo(s.getIdProduct()));
            }

            return listUserSell.size();
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            Activity activity = getActivity();

            if (activity != null && listUserSell != null && listProduct != null || i > 0 && getActivity() != null) {
                mAdapter = new AdapterUserProduct(activity.getApplicationContext(), listUserSell, listProduct);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }
}
