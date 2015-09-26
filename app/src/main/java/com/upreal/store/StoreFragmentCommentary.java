package com.upreal.store;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.upreal.product.AdapterCommentary;
import com.upreal.R;
import com.upreal.utils.Rate;
import com.upreal.utils.RateComment;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.SoapUserManager;
import com.upreal.utils.Store;
import com.upreal.utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 23/06/2015.
 */
public class StoreFragmentCommentary extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button but;
    private Store store;
    private List<Rate> listRate = new ArrayList<>();
    private List<RateComment> listComment = new ArrayList<RateComment>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_commentary, container, false);
        Bundle b = getArguments();
        store = b.getParcelable("store");
        but = (Button) v.findViewById(R.id.but_opinion);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_commentary);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        new RetrieveComment().execute();
        Toast.makeText(v.getContext(), "OK", Toast.LENGTH_SHORT).show();
        return v;
    }

    private class RetrieveComment extends AsyncTask<Void, Void, List<RateComment>> {

        RateComment rateComment;
        User user = new User();

        @Override
        protected List<RateComment> doInBackground(Void... params) {
            SoapGlobalManager gm = new SoapGlobalManager();
            listRate = gm.getRate(user.getId(), gm.getTarget_user());
            SoapUserManager um = new SoapUserManager();
            for (int i = 0; i < listRate.size(); i++) {
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
            //Toast.makeText(getActivity().getApplicationContext(), "userName[" + user.getUsername() +"]", Toast.LENGTH_SHORT).show();
           /* Toast.makeText(getActivity().getApplicationContext(), "ProdID[" + Integer.toString(prod.getId()) +"]", Toast.LENGTH_SHORT).show();

            Toast.makeText(getActivity().getApplicationContext(), "Size Rate[" + Integer.toString(rateComments.size()) +"]", Toast.LENGTH_SHORT).show();*/
/*            new RetrieveUsernameComment().execute();*/
            mAdapter = new AdapterCommentary(rateComments);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private class RetrieveUsernameComment extends AsyncTask<Void, Void, Void> {

        User user = new User();

        @Override
        protected Void doInBackground(Void... params) {
            SoapUserManager um = new SoapUserManager();
            for (int i = 0; i < listComment.size(); i++) {
                user = um.getAccountInfoUsername(Integer.parseInt(listComment.get(i).getmNameUser()));
                listComment.get(i).setmNameUser(user.getUsername());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
