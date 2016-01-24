package com.upreal.product;

import android.app.Activity;
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
import com.upreal.utils.Characteristic;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.SoapProductUtilManager;

import java.util.List;

/**
 * Created by Kyosukke on 05/11/2015.
 */
public class ProductFragmentInfo extends Fragment {
    private ConnectionDetector cd;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Characteristic> listCharacteristics;

    private int idProduct = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_base_layout, container, false);
        Bundle b = getArguments();

        idProduct = b.getInt("idProduct");

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        cd = new ConnectionDetector(v.getContext());
        if (cd.isConnectedToInternet())
            new RetrieveProductSpecification().execute();
        else
            Toast.makeText(v.getContext(), getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();

        return v;
    }

    private class RetrieveProductSpecification extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            SoapProductUtilManager pum = new SoapProductUtilManager();

            listCharacteristics = pum.getSpecification(idProduct);

            return listCharacteristics.size();
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            Activity activity = getActivity();

            if (activity != null && listCharacteristics != null || i > 0) {
                mAdapter = new AdapterSpecification(activity.getApplicationContext(), listCharacteristics);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }
}
