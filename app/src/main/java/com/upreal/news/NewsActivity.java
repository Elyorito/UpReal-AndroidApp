package com.upreal.news;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.login.LoginActivity;
import com.upreal.utils.Article;
import com.upreal.utils.ConnectionDetector;
import com.upreal.utils.FragmentCommentary;
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
    private Context context;

    private NewsViewPagerAdapter adapter;
    private FloatingActionButton like;
    private FloatingActionButton comment;

    private android.app.AlertDialog.Builder builder;
    private View layout;

    private SessionManagerUser sessionManagerUser;

    private int status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout_news);
        context = getApplicationContext();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        imageArticle = (ImageView) findViewById(R.id.imagenews);
        like = (FloatingActionButton) findViewById(R.id.like);
        comment = (FloatingActionButton) findViewById(R.id.comment);

        sessionManagerUser = new SessionManagerUser(context);
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
        comment.setOnClickListener(this);
        builder = new android.app.AlertDialog.Builder(this);
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
            case R.id.comment:
                if (cd.isConnectedToInternet()) {
                    if (!sessionManagerUser.isLogged()) {
                        builder.setTitle("Vous voulez commenter cet utilisateur ?").setMessage("Connectez vous pour partager votre opinion")
                                .setPositiveButton(v.getContext().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                    } else {
                        builder.setTitle("Votre commentaire");
                        LayoutInflater inflater = LayoutInflater.from(context);
                        layout = inflater.inflate(R.layout.dialog_comment, null);
                        builder.setView(layout);
                        final EditText comment = (EditText) layout.findViewById(R.id.comment);
                        final TextView limit = (TextView) layout.findViewById(R.id.limit);
                        comment.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                limit.setText(s.length() + " / " + String.valueOf(250));
                                if (s.length() > 250)
                                    comment.setText(s.subSequence(0, 250));
                            }
                        });
                        builder.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (comment.getText().toString().equals(""))
                                    Toast.makeText(context, "Le commentaire ne peut etre vide", Toast.LENGTH_SHORT).show();
                                else
                                    new FragmentCommentary.SendComment(comment.getText().toString(), context, sessionManagerUser.getUserId(), article.getId(), 4).execute();
                            }
                        });
                        builder.setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.create().show();
                    }
                } else
                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection) + getResources().getString(R.string.retry_retrieve_connection), Toast.LENGTH_SHORT).show();
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
