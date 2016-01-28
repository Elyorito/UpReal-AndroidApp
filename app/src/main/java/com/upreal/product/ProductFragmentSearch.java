package com.upreal.product;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductFragmentSearch extends Fragment {

    private ConnectionDetector cd;
    private Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String mSearchName;
    private List<Product> listProd = new ArrayList<Product>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        cd = new ConnectionDetector(context);
        View v = inflater.inflate(R.layout.fragment_base_layout, container, false);
        Bundle b = getArguments();
        this.mSearchName = b.getString("searchname");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        if (cd.isConnectedToInternet()) {
            new RetrieveProduct().execute();
        } else
        Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private class RetrieveProduct extends AsyncTask<Void, Void, List<Product>> {

        private List<Product> listprod = new ArrayList<Product>();
        @Override
        protected List<Product> doInBackground(Void... params) {

            SoapProductManager pm =  new SoapProductManager();
            listprod = pm.getProduct(mSearchName);
            return listprod;
        }

        @Override
        protected void onPostExecute(List<Product> list) {
            super.onPostExecute(list);
            listProd = list;
            if (context != null) {
                mAdapter = new ProductSearchAdapter(listProd, context);
            }
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}