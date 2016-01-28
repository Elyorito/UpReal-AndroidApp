package com.upreal.user;

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
import com.upreal.utils.SoapUserManager;
import com.upreal.utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 16/02/2015.
 */
public class UserFragmentSearch extends Fragment {

    //test
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String mSearchName;
    private List<User> listUser = new ArrayList<User>();
    private ConnectionDetector cd;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_layout, container, false);
        Bundle b = getArguments();
        this.mSearchName = b.getString("searchname");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        //Test
        cd = new ConnectionDetector(v.getContext());
        if (cd.isConnectedToInternet())
            new RetrieveUser().execute();
        else
            Toast.makeText(v.getContext(), getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private class RetrieveUser extends AsyncTask<Void, Void, List<User>> {

        private String list_user;
        private List<User> listuser = new ArrayList<User>();
        @Override
        protected List<User> doInBackground(Void... params) {

            SoapUserManager pm =  new SoapUserManager();
            listuser = pm.getListUser(mSearchName);
            return listuser;
        }

        @Override
        protected void onPostExecute(List<User> list) {
            super.onPostExecute(list);
            int nb = list.size();

            listUser = list;
            if (listUser != null && getActivity().getApplicationContext() != null)
                mAdapter = new UserSearchAdapter(listUser, getActivity().getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}