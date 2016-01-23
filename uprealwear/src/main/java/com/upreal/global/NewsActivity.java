package com.upreal.global;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upreal.server.GlobalManager;
import com.upreal.server.UserUtilManager;
import com.upreal.R;
import com.upreal.user.SessionManagerUser;
import com.upreal.utils.Article;
import com.upreal.utils.Item;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class NewsActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private TextView body;
    private ImageView picture;
    private ImageButton comment;
    private ImageButton like;

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        item = getIntent().getExtras().getParcelable("item");

        title = (TextView) findViewById(R.id.title);
        body = (TextView) findViewById(R.id.body);
        picture = (ImageView) findViewById(R.id.picture);
        comment = (ImageButton) findViewById(R.id.comment);
        like = (ImageButton) findViewById(R.id.like);

        body.setMovementMethod(new ScrollingMovementMethod());
        comment.setOnClickListener(this);
        like.setOnClickListener(this);

        title.setText(item.getName());
        Picasso.with(getApplicationContext()).load(item.getImagePath()).placeholder(R.drawable.picture_unavailable).into(picture);
        new RetrieveArticle().execute();
        new RetrieveRateStatus().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment:
                Log.e("NewsActivity", "Comment clicked.");
                Intent intent = new Intent(this, RateActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
                break ;
            case R.id.like:
                Log.e("NewsActivity", "Like clicked.");
                new SendRateStatus().execute((like.getAlpha() >= 1f) ? (1) : (2));
                new RetrieveRateStatus().execute();
                break ;
            default:
                Log.e("NewsActivity", "DEFAULT");
                break ;
        }
    }

    private class RetrieveArticle extends AsyncTask<Void, Void, Article> {

        @Override
        protected Article doInBackground(Void... params) {
            GlobalManager gm = new GlobalManager();

            return gm.getNewsInfo(item.getId());
        }

        @Override
        protected void onPostExecute(Article res) {
            super.onPostExecute(res);
            if (res != null) {
                body.setText(res.getBody());
            } else {
                Intent intent = new Intent(getApplicationContext(), ConfirmationActivity.class);
                intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.FAILURE_ANIMATION);
                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.search_empty));
                startActivity(intent);
                finish();
            }
        }
    }

    private class RetrieveRateStatus extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {

            GlobalManager gm = new GlobalManager();
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession != null && userSession.isLogged()) {
                return gm.getRateStatus(item.getId(), item.getTargetType(), userSession.getUserId());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            if (res == 2)
                like.setAlpha(1f);
            else
                like.setAlpha(.5f);
        }
    }

    private class SendRateStatus extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("NewsActivity", "SendRateStatus called :" + params[0]);

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession != null && userSession.isLogged()) {
                GlobalManager gm = new GlobalManager();

                switch (params[0]) {
                    case 1:
                        gm.unLikeSomething(item.getId(), item.getTargetType(), userSession.getUserId());
                        break ;
                    case 2:
                        gm.likeSomething(item.getId(), item.getTargetType(), userSession.getUserId());
                        break ;
                    default:
                        break ;
                }

                UserUtilManager uum = new UserUtilManager();

                switch (params[0]) {
                    case 1:
                        uum.createHistory(userSession.getUserId(), 2, item.getTargetType(), item.getId());
                        break ;
                    case 2:
                        uum.createHistory(userSession.getUserId(), 4, item.getTargetType(), item.getId());
                        break ;
                    default:
                        break ;
                }
            }

            return null;
        }
    }
}
