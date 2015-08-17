package com.upreal.uprealwear.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.global.RateActivity;
import com.upreal.uprealwear.server.GlobalManager;
import com.upreal.uprealwear.utils.Item;

/**
 * Created by Kyosukke on 11/08/2015.
 */
public class UserActivity extends Activity implements View.OnClickListener {

    private int id;
    private TextView name;
    private ImageView image;
    private ImageButton comment;
    private ImageButton add;
    private ImageButton like;

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

        item = getIntent().getExtras().getParcelable("item");

        id = item.getId();
        name.setText(item.getName());
        if (item.getImagePath() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            image.setImageBitmap(BitmapFactory.decodeFile(item.getImagePath(), options));
        }
        else
            image.setImageDrawable(getResources().getDrawable(R.drawable.picture_unavailable));

        comment.setOnClickListener(this);
        add.setOnClickListener(this);
        like.setOnClickListener(this);

        new RetrieveRateStatus().execute();
        add.setAlpha(.5f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment:
                Log.e("UserActivity", "Comment clicked.");
                Intent intent = new Intent(this, RateActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
                break ;
            case R.id.add:
                Log.e("UserActivity", "Add clicked.");
                break ;
            case R.id.like:
                Log.e("UserActivity", "Like clicked.");
                new SendRateStatus().execute((like.getAlpha() >= 1f) ? (1) : (2));
                new RetrieveRateStatus().execute();
                break ;
            default:
                Log.e("UserActivity", "DEFAULT");
                break ;
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

            Log.e("ProductActivity", "SendRateStatus called :" + params[0]);

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession.isLogged()) {
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
            }

            return null;
        }
    }
}
