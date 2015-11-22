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
import android.widget.Toast;

import com.upreal.home.AdapterHomeNews;
import com.upreal.R;
import com.upreal.utils.Article;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.SoapGlobalManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elyo on 24/09/15.
 */
public class NewsFragment extends Fragment {
    private ConnectionDetector cd;
    private RecyclerView mRecyclerViewNews;
    private RecyclerView.Adapter mAdapterNews;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_layout, container, false);

        mRecyclerViewNews = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerViewNews.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        mRecyclerViewNews.setLayoutManager(mLayoutManager);
        context = getActivity().getApplicationContext();
        cd = new ConnectionDetector(context);
        if (cd.isConnectedToInternet()) {
            new RetrieveNews().execute();
        } else
            Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
        mRecyclerViewNews.setAdapter(mAdapterNews);
        return v;
    }

    private class RetrieveNews extends AsyncTask<Void, Void, List<Article>> {

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
