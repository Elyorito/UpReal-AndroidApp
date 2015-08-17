package com.upreal.uprealwear.user;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.global.RateActivity;
import com.upreal.uprealwear.server.UserManager;
import com.upreal.uprealwear.utils.Item;
import com.upreal.uprealwear.utils.User;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class ProfileActivity extends Activity implements View.OnClickListener {

    private TextView username;
    private ImageView image;
    private TextView like;
    private TextView dislike;
    private ImageButton comment;
    private ImageButton history;
    private ImageButton achievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (TextView) findViewById(R.id.username);
        image = (ImageView) findViewById(R.id.image);
        like = (TextView) findViewById(R.id.like_value);
        dislike = (TextView) findViewById(R.id.dislike_value);
        comment = (ImageButton) findViewById(R.id.comment);
        history = (ImageButton) findViewById(R.id.history);
        achievement = (ImageButton) findViewById(R.id.achievement);

        comment.setOnClickListener(this);
        history.setOnClickListener(this);
        achievement.setOnClickListener(this);

        SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

        if (userSession.isLogged()) {
            username.setText(userSession.getUser().getFirstname() + " " + userSession.getUser().getLastname());
            new RetrieveUser().execute(userSession.getUserId());
        }
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
                break ;
            case R.id.achievement:
                Log.e("ProfileActivity", "Achievement clicked.");
                break ;
            default:
                Log.e("ProfileActivity", "DEFAULT");
                break ;
        }
    }

    private class RetrieveUser extends AsyncTask<Integer, Void, User> {
        UserManager um = new UserManager();

        @Override
        protected User doInBackground(Integer... params) {
            return null;
        }

        protected void onPostExecute(User res) {

        }
    }
}
