package com.upreal.user;

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
import com.upreal.server.UserManager;
import com.upreal.R;
import com.upreal.global.RateActivity;
import com.upreal.server.GlobalManager;
import com.upreal.utils.Item;
import com.upreal.utils.User;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class ProfileActivity extends Activity implements View.OnClickListener {

    private TextView username;
    private ImageView image;
    private TextView likeValue;
    private TextView dislikeValue;
    private ImageButton comment;
    private ImageButton history;
    private ImageButton achievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (TextView) findViewById(R.id.username);
        image = (ImageView) findViewById(R.id.image);
        likeValue = (TextView) findViewById(R.id.like_value);
        dislikeValue = (TextView) findViewById(R.id.dislike_value);
        comment = (ImageButton) findViewById(R.id.comment);
        history = (ImageButton) findViewById(R.id.history);
        achievement = (ImageButton) findViewById(R.id.achievement);

        comment.setOnClickListener(this);
        history.setOnClickListener(this);
        achievement.setOnClickListener(this);

        new RetrieveUser().execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.comment:
                Log.e("ProfileActivity", "Comment clicked.");
                intent = new Intent(this, RateActivity.class);
                intent.putExtra("item", new Item(2, 1, "Je sais pas", null));
                startActivity(intent);
                break ;
            case R.id.history:
                Log.e("ProfileActivity", "History clicked.");
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break ;
            case R.id.achievement:
                Log.e("ProfileActivity", "Achievement clicked.");
                intent = new Intent(this, AchievementListActivity.class);
                startActivity(intent);
                break ;
            default:
                Log.e("ProfileActivity", "DEFAULT");
                break ;
        }
    }

    private class RetrieveUser extends AsyncTask<Void, Void, User> {

        int likeV = 0;
        int dislikeV = 0;
        String imageV;

        @Override
        protected User doInBackground(Void... params) {
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession != null && userSession.isLogged()) {
                GlobalManager gm = new GlobalManager();
                likeV = gm.countRate(userSession.getUserId(), 1, 2);
                dislikeV = gm.countRate(userSession.getUserId(), 1, 3);
                imageV = gm.getPicture(userSession.getUserId(), 1);
                UserManager um = new UserManager();
                return um.getAccountInfo(userSession.getUserId());
            }
            return null;
        }

        protected void onPostExecute(User res) {
            username.setText(res.getUsername());
            likeValue.setText(likeV + "");
            dislikeValue.setText(dislikeV + "");
            Picasso.with(getApplicationContext()).load(imageV).placeholder(R.drawable.picture_unavailable).into(image);
        }
    }
}
