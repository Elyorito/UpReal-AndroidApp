package com.upreal.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;

import com.upreal.upreal.R;

/**
 * Created by Elyo on 17/09/15.
 */
public class TestNews extends Activity {


    private RecyclerView mRecyclerViewHome;
    private RecyclerView.Adapter mAdapterHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout_news);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Collapsing");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

//        mRecyclerViewHome = (RecyclerView) findViewById(R.id.RecyclerView_Home);
//        mRecyclerViewHome.setHasFixedSize(true);
//        mRecyclerViewHome.setLayoutManager(new GridLayoutManager(this, 1));
//        List<Article> articles = new ArrayList<>();
//        Article article = new Article();
//        article.setTitle("Hello !");
//        article.setBody("LoremLoremLoremLoremLoremLoremLorem");
//
//        articles.add(article);
//        articles.add(article);
//        articles.add(article);
//        articles.add(article);
//        Toast.makeText(getApplicationContext(), "nb article" + articles.size(),Toast.LENGTH_SHORT).show();
//        mAdapterHome = new AdapterHomeNews(articles, getApplicationContext());
//        mRecyclerViewHome.setAdapter(mAdapterHome);

    }
}
