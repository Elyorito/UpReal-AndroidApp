package com.upreal.user;

import android.app.Activity;
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
import com.upreal.home.AdapterHomeHistory;
import com.upreal.utils.Article;
import com.upreal.utils.History;
import com.upreal.utils.Product;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.SoapProductManager;
import com.upreal.utils.SoapStoreManager;
import com.upreal.utils.SoapUserManager;
import com.upreal.utils.SoapUserUtilManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyosukke on 20/10/2015.
 */
public class HistoryFragment  extends Fragment {

    private RecyclerView mRecyclerViewHistory;
    private RecyclerView.Adapter mAdapterHistory;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_layout, container, false);

        mRecyclerViewHistory = (RecyclerView) v.findViewById(R.id.recyclerView);
        mRecyclerViewHistory.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(v.getContext(), 1);
        mRecyclerViewHistory.setLayoutManager(mLayoutManager);

        new RetrieveHistory().execute();
        mRecyclerViewHistory.setAdapter(mAdapterHistory);
        return v;
    }

    private class RetrieveHistory extends AsyncTask<Void, Void, List<History>> {

        List<History> hList = new ArrayList<>();

        @Override
        protected List<History> doInBackground(Void... params) {

            SessionManagerUser userSession = new SessionManagerUser(getActivity().getApplicationContext());
            if (userSession != null && userSession.isLogged()) {
                SoapUserUtilManager uum = new SoapUserUtilManager();
                hList = uum.getUserHistory(userSession.getUserId());

                for (History h : hList) {
                    switch (h.getIdType()) {
                        case 1:
                            SoapUserManager um = new SoapUserManager();
                            h.setNameTarget(um.getAccountInfoUsername(h.getIdTarget()).getUsername());
                            break ;
                        case 2:
                            SoapProductManager pm = new SoapProductManager();
                            Product product = pm.getProductInfo(h.getIdTarget());
                            if (product != null) {
                                h.setNameTarget(product.getName());
                            }
                            break ;
                        case 3:
                            SoapStoreManager sm = new SoapStoreManager();
                            h.setNameTarget(sm.getStoreInfo(h.getIdTarget()).getName());
                            break ;
                        case 4:
                            SoapGlobalManager gm = new SoapGlobalManager();
                            List<Article> listNews = gm.getNews();
                            Article article = null;

                            for (Article a : listNews) {
                                if (a.getId() == h.getIdTarget()) {
                                    article = a;
                                    break ;
                                }
                            }
                            if (article != null)
                                h.setNameTarget(article.getTitle());
                            break ;
                    }
                }
            }
            return hList;
        }

        @Override
        protected void onPostExecute(List<History> hList) {
            super.onPostExecute(hList);
            Activity activity = getActivity();

            if (activity != null) {
                mAdapterHistory = new AdapterHomeHistory(hList, activity.getApplicationContext());
                mRecyclerViewHistory.setAdapter(mAdapterHistory);
            }
        }
    }
}
