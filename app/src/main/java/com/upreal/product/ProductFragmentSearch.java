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

import com.upreal.upreal.R;
import com.upreal.utils.Product;
import com.upreal.utils.SoapProductManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductFragmentSearch extends Fragment {

    //test
/*    private String sProduct[];*/
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String mSearchName;
    private List<Product> listProd = new ArrayList<Product>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_search, container, false);
        Bundle b = getArguments();
        this.mSearchName = b.getString("searchname");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_ProductSearch);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        //Test
        new RetrieveProduct().execute();
/*//test
        mAdapter = new ProductSearchAdapter(sProduct);
*/
 //       mAdapter = new ProductSearchAdapter(listProd);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private class RetrieveProduct extends AsyncTask<Void, Void, List<Product>> {

        private List<Product> listprod = new ArrayList<Product>();
        @Override
        protected List<Product> doInBackground(Void... params) {

            SoapProductManager pm =  new SoapProductManager();
            /*list_product = ex.getProduct(mSearchName);*/
            listprod = pm.getProduct(mSearchName);
            return listprod;
        }

        @Override
        protected void onPostExecute(List<Product> list) {
            super.onPostExecute(list);
/*
            int nb = list.size();
            for (int i = 0; i < nb; i++) {
                Toast.makeText(getActivity().getApplicationContext(), Integer.toString(list.size()), Toast.LENGTH_SHORT).show();

                Toast.makeText(getActivity().getApplicationContext(), "SEARCH Name[" + list.get(i).getName() + "]", Toast.LENGTH_SHORT).show();

                Toast.makeText(getActivity().getApplicationContext(), "SEARCH Brand[" + list.get(i).getBrand()+ "]", Toast.LENGTH_SHORT).show();

                Toast.makeText(getActivity().getApplicationContext(), "SEARCH EAN[" + list.get(i).getEan() + "]", Toast.LENGTH_SHORT).show();
            }
*/
            listProd = list;
            Context context;

            context = getActivity().getApplicationContext();
            if (context != null) {
                mAdapter = new ProductSearchAdapter(listProd, context);
            }
            mRecyclerView.setAdapter(mAdapter);
        }
    }


/*    private class RetreiveProduct extends AsyncTask<Void, Void, String> {

        private String list_product;
        private List<ArrayList<Product>> listprod;
        @Override
        protected String doInBackground(Void... params) {

            SoapProductManager ex =  new SoapProductManager();
            *//*list_product = ex.getProduct(mSearchName);*//*
            listprod = ex.getProduct(mSearchName);
            return list_product;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity().getApplicationContext(), "SEARCH LIST[" + s + "]", Toast.LENGTH_SHORT).show();
        }
    }*/
}