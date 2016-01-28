package com.upreal.store;

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
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.SoapStoreManager;
import com.upreal.utils.Store;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 30/07/2015.
 */
public class StoreFragmentSearch extends Fragment {
    private ConnectionDetector cd;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String mSearchName;
    private List<Store> listStore = new ArrayList<>();
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_layout, container, false);
        Bundle b = getArguments();
        context = getActivity().getApplicationContext();
        cd = new ConnectionDetector(context);

        this.mSearchName = b.getString("searchname");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        //Test
        if (cd.isConnectedToInternet()) {
            new RetrieveStore().execute();
        } else
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private class RetrieveStore extends AsyncTask<Void, Void, List<Store>> {

        private List<Store> liststore = new ArrayList<Store>();
        @Override
        protected List<Store> doInBackground(Void... params) {

            SoapStoreManager pm =  new SoapStoreManager();
            liststore = pm.getListStore(mSearchName);
            return liststore;
        }

        @Override
        protected void onPostExecute(List<Store> list) {
            super.onPostExecute(list);
            int nb = list.size();

            listStore = list;
            if (context != null && listStore != null)
                mAdapter = new StoreSearchAdapter(listStore, context);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
