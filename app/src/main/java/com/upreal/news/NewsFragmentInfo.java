package com.upreal.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.utils.Article;

/**
 * Created by Eric on 05/12/2015.
 */
public class NewsFragmentInfo extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView newsBody;

    private Article article;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_textview, container, false);
        Bundle b = getArguments();

        article = b.getParcelable("article");
        newsBody = (TextView) v.findViewById(R.id.textview);
        newsBody.setText(article.getBody());

        return v;
    }
}
