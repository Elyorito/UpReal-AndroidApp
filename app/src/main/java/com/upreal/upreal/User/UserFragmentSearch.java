package com.upreal.upreal.User;

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
import com.upreal.upreal.utils.SoapUserManager;
import com.upreal.upreal.utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 16/02/2015.
 */
public class UserFragmentSearch extends Fragment {

    //test
/*    private String sProduct[];*/
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private String mSearchName;
    private List<User> listUser = new ArrayList<User>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_search, container, false);
        Bundle b = getArguments();
        this.mSearchName = b.getString("searchname");
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_ProductSearch);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(v.getContext(), 2));
        //Test
        new RetrieveUser().execute();
/*//test
        mAdapter = new ProductSearchAdapter(sProduct);
*/
 //       mAdapter = new ProductSearchAdapter(listProd);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private class RetrieveUser extends AsyncTask<Void, Void, List<User>> {

        private String list_user;
        private List<User> listuser = new ArrayList<User>();
        @Override
        protected List<User> doInBackground(Void... params) {

            SoapUserManager pm =  new SoapUserManager();
            /*list_product = ex.getProduct(mSearchName);*/
            listuser = pm.getListUser(mSearchName);
            return listuser;
        }

        @Override
        protected void onPostExecute(List<User> list) {
            super.onPostExecute(list);
            int nb = list.size();

            listUser = list;
            mAdapter = new UserSearchAdapter(listUser);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}