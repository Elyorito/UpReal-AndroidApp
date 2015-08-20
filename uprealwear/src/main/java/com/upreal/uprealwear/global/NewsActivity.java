package com.upreal.uprealwear.global;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.server.GlobalManager;
import com.upreal.uprealwear.utils.Article;
import com.upreal.uprealwear.utils.Item;

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
        if (item.getImagePath() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            picture.setImageBitmap(BitmapFactory.decodeFile(item.getImagePath(), options));
        }
        else
            picture.setImageDrawable(getResources().getDrawable(R.drawable.picture_unavailable));
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
            return gm.getRateStatus(item.getId(), item.getTargetType(), 2);
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            switch (res) {
                case 0:
                    like.setAlpha(.5f);
                    like.setColorFilter(R.color.black);
                    break ;
                case 1:
                    like.setAlpha(.5f);
                    like.setColorFilter(R.color.black);
                    break ;
                case 2:
                    like.setAlpha(1f);
                    like.setColorFilter(R.color.red);
                    break ;
                case 3:
                    like.setAlpha(1f);
                    like.setColorFilter(R.color.black);
                    break ;
                default:
                    break ;
            }
        }
    }

    private class SendRateStatus extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            GlobalManager gm = new GlobalManager();

            Log.e("NewsActivity", "SendRateStatus called :" + params[0]);

            switch (params[0]) {
                case 1:
                    gm.unLikeSomething(item.getId(), item.getTargetType(), 2);
                    break ;
                case 2:
                    gm.likeSomething(item.getId(), item.getTargetType(), 2);
                    break ;
                default:
                    break ;
            }

            return null;
        }
    }
}
