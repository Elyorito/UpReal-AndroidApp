package com.upreal.news;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.Article;
import com.upreal.utils.ConnectionDetector;

/**
 * Created by Sofiane on 08/08/2015.
 */
public class NewsActivity extends AppCompatActivity {

    private ConnectionDetector cd;
    private Toolbar toolbar;
    private Article article;
    private ImageView imageArticle;
    private TextView titleArticle;
    private TextView bodyArticle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news);
        setContentView(R.layout.test_layout_news);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        imageArticle = (ImageView) findViewById(R.id.imagenews);
        bodyArticle = (TextView) findViewById(R.id.bodynews);
        article = getIntent().getExtras().getParcelable("listnews");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(article.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));

//        toolbar.setTitle(article.getTitle());
        bodyArticle.setText(article.getBody());
        cd = new ConnectionDetector(getApplicationContext());
        if (cd.isConnectedToInternet()) {
            Picasso.with(NewsActivity.this).load("http://163.5.84.202/Symfony/web/images/News/" + article.getPicture()).into(imageArticle);
        } else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
//        Toast.makeText(NewsActivity.this, article.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
