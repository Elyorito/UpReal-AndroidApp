package com.upreal.product;

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
import com.upreal.utils.Characteristic;
import com.upreal.utils.SoapProductUtilManager;

import java.util.List;

/**
 * Created by Kyosukke on 05/11/2015.
 */
public class ProductFragmentInfo extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Characteristic> listCharacteristics;

    private int idProduct = 0;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
////        View v = inflater.inflate(R.layout.fragment_product_info, container, false);
////        Bundle b = getArguments();
////
////        idProduct = b.getInt("idProduct");
////
////        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_info);
////        recyclerView.setHasFixedSize(false);
////        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
////        recyclerView.setLayoutManager(mLayoutManager);
////
////        new RetrieveProductSpecification().execute();
////
////        return v;
//    }

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
            if (listCharacteristics != null || i > 0) {
                mAdapter = new AdapterSpecification(getActivity().getApplicationContext(), listCharacteristics);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }
}
