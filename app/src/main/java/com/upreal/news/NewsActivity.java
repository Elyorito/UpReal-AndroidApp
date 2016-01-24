package com.upreal.news;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.utils.Article;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.IPDefiner;
import com.upreal.utils.SessionManagerUser;
import com.upreal.utils.SoapGlobalManager;
import com.upreal.utils.SoapUserUtilManager;

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
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private NewsViewPagerAdapter adapter;
    private ImageView like;
    private ImageView dislike;
    private TextView likeV;
    private TextView dislikeV;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_news);
        setContentView(R.layout.test_layout_news);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        imageArticle = (ImageView) findViewById(R.id.imagenews);
        like = (ImageView) findViewById(R.id.logo_like);
        dislike = (ImageView) findViewById(R.id.logo_dislike);
        likeV = (TextView) findViewById(R.id.like_value);
        dislikeV = (TextView) findViewById(R.id.dislike_value);
        article = getIntent().getExtras().getParcelable("listnews");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(article.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));

//        toolbar.setTitle(article.getTitle());
//        bodyArticle.setText(article.getBody());
        cd = new ConnectionDetector(getApplicationContext());
        if (cd.isConnectedToInternet()) {
            Picasso.with(NewsActivity.this).load(new IPDefiner().getIP() + "Symfony/web/images/News/" + article.getPicture()).into(imageArticle);
        } else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
//        Toast.makeText(NewsActivity.this, article.getTitle(), Toast.LENGTH_SHORT).show();
        tabLayout = (TabLayout) findViewById(R.id.tabsnews);
        CharSequence Tab[] = {"Info", "Avis"};
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), Tab, 2, article, this);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendRateStatus().execute((like.getAlpha() >= 1f) ? (1) : (2));
                new RetrieveRateStatus().execute();
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendRateStatus().execute((dislike.getAlpha() >= 1f) ? (1) : (3));
                new RetrieveRateStatus().execute();
            }
        });
    }

    private class RetrieveRateStatus extends AsyncTask<Void, Void, Integer> {

        int nbLike = 0;
        int nbDislike = 0;

        @Override
        protected Integer doInBackground(Void... params) {

            SoapGlobalManager gm = new SoapGlobalManager();
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            nbLike = gm.countRate(article.getId(), 4, 2);
            nbDislike = gm.countRate(article.getId(), 4, 3);
            if (userSession.isLogged()) {
                return gm.getRateStatus(article.getId(), 4, userSession.getUserId());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);

            likeV.setText(nbLike + "");
            dislikeV.setText(nbDislike + "");

            switch (res) {
                case 1:
                    like.setAlpha(.5f);
                    dislike.setAlpha(.5f);
                    break ;
                case 2:
                    like.setAlpha(1f);
                    dislike.setAlpha(.5f);
                    break ;
                case 3:
                    like.setAlpha(.5f);
                    dislike.setAlpha(1f);
                    break ;
                default:
                    like.setAlpha(.5f);
                    dislike.setAlpha(.5f);
                    break ;
            }
        }
    }
    private class SendRateStatus extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("AdapterCommentary", "SendRateStatus called :" + params[0]);

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession.isLogged()) {
                SoapGlobalManager gm = new SoapGlobalManager();

                switch (params[0]) {
                    case 1:
                        gm.unLikeSomething(article.getId(), 4, userSession.getUserId());
                        break ;
                    case 2:
                        gm.likeSomething(article.getId(), 4, userSession.getUserId());
                        break ;
                    case 3:
                        gm.dislikeSomething(article.getId(), 4, userSession.getUserId());
                        break ;
                    default:
                        break ;
                }

                SoapUserUtilManager uum = new SoapUserUtilManager();

                switch (params[0]) {
                    case 1:
                        uum.createHistory(userSession.getUserId(), 2, 4, article.getId());
                        break ;
                    case 2:
                        uum.createHistory(userSession.getUserId(), 4, 4, article.getId());
                        break ;
                    case 3:
                        uum.createHistory(userSession.getUserId(), 3, 4, article.getId());
                        break ;
                    default:
                        break ;
                }
            }

            return null;
        }
    }

}
