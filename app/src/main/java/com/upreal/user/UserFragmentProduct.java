package com.upreal.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upreal.R;
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

        new RetrieveUserSell().execute();

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
            if (listUserSell != null && listProduct != null || i > 0) {
                mAdapter = new AdapterUserProduct(getActivity().getApplicationContext(), listUserSell, listProduct);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }
}
