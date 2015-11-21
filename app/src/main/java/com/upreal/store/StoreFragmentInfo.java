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

import com.upreal.R;
import com.upreal.utils.Address;
import com.upreal.utils.Company;
import com.upreal.utils.SoapStoreManager;
import com.upreal.utils.Store;

/**
 * Created by Kyosukke on 21/11/2015.
 */
public class StoreFragmentInfo extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Store store;
    private Address address;
    private Company company;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_base_layout, container, false);
        Bundle b = getArguments();

        store = b.getParcelable("store");
        if (store == null)
            return null;

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        new RetrieveStoreInfo().execute();

        return v;
    }

    private class RetrieveStoreInfo extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapStoreManager sm = new SoapStoreManager();

            address = sm.getAddressByStore(store.getId());
            company = sm.getCompanyByStore(store.getId());

            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            mAdapter = new AdapterStoreInfo(getActivity().getApplicationContext(), store, address, company);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
