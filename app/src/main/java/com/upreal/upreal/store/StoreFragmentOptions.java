package com.upreal.upreal.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upreal.upreal.R;
import com.upreal.upreal.product.AdapterOption;

/**
 * Created by Kyosukke on 23/06/2015.
 */
public class StoreFragmentOptions extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String OPTION[];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_options, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_options);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        OPTION = new String[] {"Suivre ce magasin"};
        mAdapter = new AdapterOption(OPTION);
        recyclerView.setAdapter(mAdapter);

        return v;
    }
}