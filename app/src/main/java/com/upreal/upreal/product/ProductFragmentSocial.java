package com.upreal.upreal.product;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upreal.upreal.R;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.SessionManagerUser;

/**
 * Created by Elyo on 12/03/2015.
 */
public class ProductFragmentSocial extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Product prod;
    private SessionManagerUser sessionManagerUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_social, container, false);
        Bundle b = getArguments();
        prod = b.getParcelable("product");
        sessionManagerUser = new SessionManagerUser(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_social);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        String SOCIALOPT[] = new String[] {"Aimer","Commenter","Produit Similaire","Partager"};

        mAdapter = new AdapterSocial(SOCIALOPT, prod, sessionManagerUser);
        recyclerView.setAdapter(mAdapter);

        return v;
    }
}
