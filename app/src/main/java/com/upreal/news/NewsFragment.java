package com.upreal.news;

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

import com.upreal.home.AdapterHomeNews;
import com.upreal.upreal.R;
import com.upreal.utils.Article;
import com.upreal.utils.SoapGlobalManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 24/09/15.
 */
public class NewsFragment extends Fragment {

    private RecyclerView mRecyclerViewNews;
    private RecyclerView.Adapter mAdapterNews;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View v = inflater.inflate(R.layout.fragment_home_news, container, false);
        mRecyclerViewNews = (RecyclerView) v.findViewById(R.id.recyclerview_home_news);
        mRecyclerViewNews.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        mRecyclerViewNews.setLayoutManager(mLayoutManager);

        new RetreiveNews().execute();
        return v;
    }

    private class RetreiveNews extends AsyncTask<Void, Void, List<Article>> {

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
            mAdapterNews = new AdapterHomeNews(articles, context);
            mRecyclerViewNews.setAdapter(mAdapterNews);
        }
    }
}
