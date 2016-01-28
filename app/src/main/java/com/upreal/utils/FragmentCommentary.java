package com.upreal.utils;

import android.content.Context;
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
import android.widget.Toast;

import com.upreal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 01/11/2015.
 */
public class FragmentCommentary extends Fragment {
    private ConnectionDetector cd;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Rate> listRate = new ArrayList<Rate>();
    private List<RateComment> listComment = new ArrayList<>();
    private Context context;

    private int id = 0;
    private int type = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_product_commentary, container, false);
        Bundle b = getArguments();

        id = b.getInt("id");
        type = b.getInt("type");

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_Product_commentary);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);

        context = getContext();
        cd = new ConnectionDetector(context);
        if (cd.isConnectedToInternet())
            new RetrieveComment().execute();
        else
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
        return v;
    }

    private class RetrieveComment extends AsyncTask<Void, Void, List<RateComment>> {

        User user = new User();
        RateComment rateComment;

        @Override
        protected List<RateComment> doInBackground(Void... params) {
            SoapGlobalManager gm = new SoapGlobalManager();

            listRate = gm.getRate(id, type);

            SoapUserManager um = new SoapUserManager();
            for (int i = 0; i < listRate.size(); i++) {
                user = um.getAccountInfoUsername(listRate.get(i).getmId_user());
                rateComment = new RateComment();
                rateComment.setId(listRate.get(i).getmId());
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
                mAdapter = new AdapterCommentary(context, rateComments);
                recyclerView.setAdapter(mAdapter);
            }
        }
    }

    public static class SendComment extends AsyncTask<Void, Void, Boolean> {
        private String comment;
        private Context mContext;
        private int mUserId;
        private int mId;
        private int mType;

        public SendComment(String comment, Context context, int userId, int id, int type) {
            this.comment = comment;
            this.mContext = context;
            mUserId = userId;
            mId = id;
            mType = type;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapGlobalManager sm = new SoapGlobalManager();
            sm.createComment(mUserId, mId, mType, comment);
            return true;
        }

        protected void onPostExecute(Boolean success) {
            Toast.makeText(mContext, "Votre commentaire a bien été envoyé", Toast.LENGTH_SHORT).show();
            Log.v("comment", comment);
            mContext = null;
        }
    }
}
