package com.upreal.user;

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

import com.upreal.R;
import com.upreal.home.AdapterHomeNews;
import com.upreal.utils.Article;
import com.upreal.utils.SoapGlobalManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 20/10/2015.
 */
public class HistoryFragment  extends Fragment {

    private RecyclerView mRecyclerViewHistory;
    private RecyclerView.Adapter mAdapterHistory;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_news, container, false);

        mRecyclerViewHistory = (RecyclerView) v.findViewById(R.id.recyclerview_home_news);
        mRecyclerViewHistory.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        mRecyclerViewHistory.setLayoutManager(mLayoutManager);

        new RetrieveHistory().execute();
        mRecyclerViewHistory.setAdapter(mAdapterHistory);
        return v;
    }

    private class RetrieveHistory extends AsyncTask<Void, Void, List<Article>> {

        List<Article> listnews = new ArrayList<>();

        @Override
        protected List<Article> doInBackground(Void... params) {
            SoapGlobalManager gm = new SoapGlobalManager();
            listnews = gm.getNews();
            return listnews;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);
            context = getActivity().getApplicationContext();
            mAdapterHistory = new AdapterHomeNews(articles, context);
            mRecyclerViewHistory.setAdapter(mAdapterHistory);
        }
    }
}
