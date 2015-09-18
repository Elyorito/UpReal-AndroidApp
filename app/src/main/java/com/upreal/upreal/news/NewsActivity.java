package com.upreal.upreal.news;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.upreal.R;
import com.upreal.upreal.utils.Article;

/**
 * Created by Sofiane on 08/08/2015.
 */
public class NewsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Article article;
    ImageView imageArticle;
    TextView titleArticle;
    TextView bodyArticle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news);
        setContentView(R.layout.test_layout_news);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageArticle = (ImageView) findViewById(R.id.imagenews);
        bodyArticle = (TextView) findViewById(R.id.bodynews);
        article = getIntent().getExtras().getParcelable("listnews");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(article.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));

//        toolbar.setTitle(article.getTitle());
        bodyArticle.setText(article.getBody());
        Picasso.with(NewsActivity.this).load("http://163.5.84.202/Symfony/web/images/News/" + article.getPicture()).into(imageArticle);
        Toast.makeText(NewsActivity.this, article.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
