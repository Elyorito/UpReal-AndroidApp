package com.upreal.upreal.store;

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
import com.upreal.upreal.utils.SoapStoreManager;
import com.upreal.upreal.utils.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 30/07/2015.
 */
public class StoreFragmentSearch extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String mSearchName;
    private List<Store> listStore = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_search, container, false);
        Bundle b = getArguments();
        this.mSearchName = b.getString("searchname");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_ProductSearch);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        //Test
        new RetrieveStore().execute();

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private class RetrieveStore extends AsyncTask<Void, Void, List<Store>> {

        private List<Store> liststore = new ArrayList<Store>();
        @Override
        protected List<Store> doInBackground(Void... params) {

            SoapStoreManager pm =  new SoapStoreManager();
            /*list_product = ex.getProduct(mSearchName);*/
            liststore = pm.getListStore(mSearchName);
            return liststore;
        }

        @Override
        protected void onPostExecute(List<Store> list) {
            super.onPostExecute(list);
            int nb = list.size();

            listStore = list;
            if (listStore != null)
                mAdapter = new StoreSearchAdapter(listStore, getActivity().getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
