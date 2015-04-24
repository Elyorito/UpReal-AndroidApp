package com.upreal.upreal.product;

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

import com.upreal.upreal.R;
import com.upreal.upreal.utils.Product;
import com.upreal.upreal.utils.RateComment;
import com.upreal.upreal.utils.SoapGlobalManager;
import com.upreal.upreal.utils.SoapUserManager;
import com.upreal.upreal.utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 12/03/2015.
 */


public class ProductFragmentCommentary extends android.support.v4.app.Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button but;
    private Product prod;
    private List<RateComment> listComment = new ArrayList<RateComment>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_commentary, container, false);
        Bundle b = getArguments();
        prod = b.getParcelable("product");
        but = (Button) v.findViewById(R.id.but_opinion);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_commentary);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        new RetrieveComment().execute();
        String USER[] = new String[] {Integer.toString(prod.getId()), "Elyo", "Kyo", "Nunkh", "JerEm", "AngelRoic", "SeanCor", "Sam"};
        String COMMENT[] = new String[]{"This is SHIT", "Quite Good", "I love that things, I mean I Like it, I mean...", "Good SHIT", "<3", "GS", "Corruption!", "Do you Know this product?"};
        String RATING[] = new String[]{"1", "3", "4", "5", "5", "3", "4", "2", "2"};

      //  mAdapter = new AdapterCommentary(USER, COMMENT, RATING);
        /*recyclerView.setAdapter(mAdapter);*/
        Toast.makeText(v.getContext(), "OK", Toast.LENGTH_SHORT).show();
        return v;
    }

    private class RetrieveComment extends AsyncTask<Void, Void, List<RateComment>> {

        User user = new User();

        @Override
        protected List<RateComment> doInBackground(Void... params) {
            SoapGlobalManager gm = new SoapGlobalManager();
            listComment = gm.getRateComment(0, prod.getId(), 0, 1, 0);
            SoapUserManager um = new SoapUserManager();
            for (int i = 0; i < listComment.size(); i++) {
                user = um.getAccountInfoUsername(Integer.parseInt(listComment.get(i).getmNameUser().toString()));
                listComment.get(i).setmNameUser(user.getUsername());
            }
            return listComment;
        }

        @Override
        protected void onPostExecute(List<RateComment> rateComments) {
            super.onPostExecute(rateComments);
            Toast.makeText(getActivity().getApplicationContext(), "userName[" + user.getUsername() +"]", Toast.LENGTH_SHORT).show();
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