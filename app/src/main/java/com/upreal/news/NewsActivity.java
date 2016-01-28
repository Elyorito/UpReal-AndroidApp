package com.upreal.news;

import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
public class NewsActivity extends AppCompatActivity implements View.OnClickListener {

    private ConnectionDetector cd;
    private Toolbar toolbar;
    private Article article;
    private ImageView imageArticle;
    private TextView titleArticle;
    private TextView bodyArticle;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private NewsViewPagerAdapter adapter;
    private FloatingActionButton like;

    private int status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout_news);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        imageArticle = (ImageView) findViewById(R.id.imagenews);
        like = (FloatingActionButton) findViewById(R.id.like);
        article = getIntent().getExtras().getParcelable("listnews");

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(article.getTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.black));

        cd = new ConnectionDetector(getApplicationContext());
        if (cd.isConnectedToInternet()) {
            Picasso.with(NewsActivity.this).load(new IPDefiner().getIP() + "Symfony/web/images/News/" + article.getPicture()).into(imageArticle);
        } else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.please_reload), Toast.LENGTH_SHORT).show();
        tabLayout = (TabLayout) findViewById(R.id.tabsnews);
        CharSequence Tab[] = {"Info", "Avis"};
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new NewsViewPagerAdapter(getSupportFragmentManager(), Tab, 2, article, this);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        like.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like:
                if (cd.isConnectedToInternet()) {
                    switch (status) {
                        case 1:
                            new SendRateStatus().execute(2);
                            break;
                        case 2:
                            new SendRateStatus().execute(1);
                            break;
                        default:
                            new SendRateStatus().execute(2);
                            break;
                    }
                } else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection) + " " + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
                new RetrieveRateStatus().execute();
                break;
            default:
                break;
        }
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

            status = res;
            switch (status) {
                case 1:
                    like.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.result_minor_text)));
                    break;
                case 2:
                    like.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    break;
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
                    default:
                        break ;
                }
            }

            return null;
        }
    }

}
