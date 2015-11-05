package com.upreal.utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.upreal.R;
import com.upreal.product.AdapterCommentary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class FragmentCommentary extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Rate> listRate = new ArrayList<Rate>();
    private List<RateComment> listComment = new ArrayList<>();

    private int idProduct = 0;
    private int idUser = 0;
    private int idStore = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_product_commentary, container, false);
        Bundle b = getArguments();

        idProduct = b.getInt("idProduct");
        idUser = b.getInt("idUser");
        idStore = b.getInt("idStore");

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_commentary);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        new RetrieveComment().execute();

        return v;
    }

    private class RetrieveComment extends AsyncTask<Void, Void, List<RateComment>> {

        User user = new User();
        RateComment rateComment;

        @Override
        protected List<RateComment> doInBackground(Void... params) {
            SoapGlobalManager gm = new SoapGlobalManager();

            Log.e("FC", idUser + " " + idProduct + " " + idStore + " ");
            if (idUser != 0)
                listRate = gm.getRate(idUser, 1);
            else if (idProduct != 0)
                listRate = gm.getRate(idProduct, 2);
            else if (idStore != 0)
                listRate = gm.getRate(idStore, 3);
            else
                return null;

            SoapUserManager um = new SoapUserManager();
            for (int i = 0; i < listRate.size(); i++) {
                Log.e("FC", "test:" + listRate.get(i).getmCommentary());
                user = um.getAccountInfoUsername(listRate.get(i).getmId_user());
                rateComment = new RateComment();
                rateComment.setmNameUser(user.getUsername());
                rateComment.setmTextComment(listRate.get(i).getmCommentary());
                listComment.add(rateComment);
            }
            return listComment;
        }

        @Override
        protected void onPostExecute(List<RateComment> rateComments) {
            super.onPostExecute(rateComments);
            if (rateComments != null) {
                mAdapter = new AdapterCommentary(rateComments);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }
}
