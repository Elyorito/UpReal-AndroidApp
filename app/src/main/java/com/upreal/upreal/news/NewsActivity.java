package com.upreal.upreal.news;

import android.os.Bundle;
import android.os.PersistableBundle;
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
        setContentView(R.layout.activity_news);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        imageArticle = (ImageView) findViewById(R.id.imagenews);
        bodyArticle = (TextView) findViewById(R.id.bodynews);
        article = getIntent().getExtras().getParcelable("listnews");

        toolbar.setTitle(article.getTitle());
        bodyArticle.setText(article.getBody());
        Picasso.with(NewsActivity.this).load("http://millbafs.com/wp-content/uploads/2014/01/community.png").into(imageArticle);
        Toast.makeText(NewsActivity.this, article.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
