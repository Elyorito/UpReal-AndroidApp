package com.upreal.upreal.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upreal.upreal.R;

/**
 * Created by Elyo on 16/02/2015.
 */
public class ProductFragmentSearch extends Fragment {

    //test
    private String sProduct[];
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_search, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_ProductSearch);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        //Test
        sProduct = new String[]{"Coca", "Coca", "Coca", "Coca", "Coca", "Coca", "Coca"};
        mAdapter = new ProductSearchAdapter(sProduct);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }
}