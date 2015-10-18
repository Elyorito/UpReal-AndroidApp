package com.upreal.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upreal.R;
import com.upreal.utils.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 05/10/2015.
 */
public class ProductFragmentPrice extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerViewList;
    private RecyclerView.Adapter mAdapterList;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Address> addresses = new ArrayList<>();
    private List<String> distances = new ArrayList<>();
    private List<String> prices = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_price, container, false);
        Bundle b = getArguments();
        addresses = b.getParcelableArrayList("addresses");
        distances = b.getStringArrayList("distances");
        prices = b.getStringArrayList("prices");
        if (addresses == null || addresses.isEmpty() || distances == null || distances.isEmpty() || prices == null || prices.isEmpty())
            return null;
        mRecyclerViewList = (RecyclerView) v.findViewById(R.id.address_list);
        mRecyclerViewList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerViewList.setLayoutManager(mLayoutManager);

        mAdapterList = new AdapterListStore(addresses, distances, prices);
        mRecyclerViewList.setAdapter(mAdapterList);

        return v;
    }
}
