package com.upreal.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upreal.R;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.User;

/**
 * Created by Eric on 26/05/2015.
 */
public class UserFragmentSocial extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private User user;
    private SessionManagerUser sessionManagerUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_social, container, false);
        Bundle b = getArguments();
        user = b.getParcelable("user");
        sessionManagerUser = new SessionManagerUser(getActivity().getApplicationContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_social);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        String SOCIALOPT[] = new String[] {"Suivre","Commenter","Produit à troquer","Partager","Envoyer un message","Signaler"};

        mAdapter = new UserAdapterSocial(SOCIALOPT, user, sessionManagerUser);
        recyclerView.setAdapter(mAdapter);

        return v;
    }
}
