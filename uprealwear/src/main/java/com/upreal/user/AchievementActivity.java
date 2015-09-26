package com.upreal.user;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.upreal.server.UserUtilManager;
import com.upreal.uprealwear.R;
import com.upreal.utils.Item;

/**
 * Created by Kyosukke on 18/08/2015.
 */
public class AchievementActivity extends Activity {

    private TextView name;
    private TextView desc;
    private ImageView status;

    private Item item;

    @Override
    protected void onCreate(Bundle savecInstanceState) {
        super.onCreate(savecInstanceState);
        setContentView(R.layout.activity_achievement);

        name = (TextView) findViewById(R.id.name);
        desc = (TextView) findViewById(R.id.desc);
        status = (ImageView) findViewById(R.id.status);

        item = getIntent().getExtras().getParcelable("item");

        name.setText(item.getName());
        new RetrieveAchievement().execute();
    }

    private class RetrieveAchievement extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {

            UserUtilManager uum = new UserUtilManager();

            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

            if (userSession.isLogged()) {
                if (uum.hasAchievement(userSession.getUserId(), item.getId()))
                    return 1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            switch (res) {
                case 0:
                    status.setAlpha(.5f);
                    break ;
                case 1:
                    status.setAlpha(1f);
                    break ;
                default:
                    break ;
            }
        }
    }
}
