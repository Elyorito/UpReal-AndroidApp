package com.upreal.product;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.upreal.R;
import com.upreal.global.RateActivity;
import com.upreal.server.GlobalManager;
import com.upreal.server.UserUtilManager;
import com.upreal.user.ListActivity;
import com.upreal.user.SessionManagerUser;
import com.upreal.utils.Item;

/**
 * Created by Kyosukke on 08/08/2015.
 */
public class ProductActivity extends Activity implements View.OnClickListener {

    private int id;
    private TextView name;
    private ImageView image;
    private ImageButton comment;
    private ImageButton add;
    private ImageButton like;
    private TextView likeValue;
    private TextView dislikeValue;

    private Item item;

    @Override
    protected void onCreate(Bundle savecInstanceState) {
        super.onCreate(savecInstanceState);
        setContentView(R.layout.util_details);

        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        comment = (ImageButton) findViewById(R.id.comment);
        add = (ImageButton) findViewById(R.id.add);
        like = (ImageButton) findViewById(R.id.like);
        likeValue = (TextView) findViewById(R.id.like_value);
        dislikeValue = (TextView) findViewById(R.id.dislike_value);

        item = getIntent().getExtras().getParcelable("item");

        id = item.getId();
        name.setText(item.getName());
        Picasso.with(getApplicationContext()).load(item.getImagePath()).placeholder(R.drawable.picture_unavailable).into(image);

        comment.setOnClickListener(this);
        add.setOnClickListener(this);
        like.setOnClickListener(this);

        new RetrieveRateStatus().execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.comment:
                Log.e("ProductActivity", "Comment clicked.");
                intent = new Intent(this, RateActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
                break ;
            case R.id.add:
                Log.e("ProductActivity", "Add clicked.");
                intent = new Intent(this, ListActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
                break ;
            case R.id.like:
                Log.e("ProductActivity", "Like clicked.");
                new SendRateStatus().execute((like.getAlpha() >= 1f) ? (1) : (2));
                new RetrieveRateStatus().execute();
                break ;
            default:
                Log.e("ProductActivity", "DEFAULT");
                break ;
        }
    }

    private class RetrieveRateStatus extends AsyncTask<Void, Void, Integer> {

        int likeV = 0;
        int dislikeV = 0;

        @Override
        protected Integer doInBackground(Void... params) {

            GlobalManager gm = new GlobalManager();
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            likeV = gm.countRate(item.getId(), item.getTargetType(), 2);
            dislikeV = gm.countRate(item.getId(), item.getTargetType(), 3);
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

            likeValue.setText(likeV + "");
            dislikeValue.setText(dislikeV + "");
        }
    }

    private class SendRateStatus extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            Log.e("ProductActivity", "SendRateStatus called :" + params[0]);

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
