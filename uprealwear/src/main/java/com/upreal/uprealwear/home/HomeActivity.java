package com.upreal.uprealwear.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.bridge.BridgeDeviceActivity;
import com.upreal.uprealwear.global.NewsResultActivity;
import com.upreal.uprealwear.search.SearchActivity;
import com.upreal.uprealwear.user.ListActivity;
import com.upreal.uprealwear.user.ProfileActivity;
import com.upreal.uprealwear.user.SessionManagerUser;

public class HomeActivity extends Activity implements View.OnClickListener {

    private TextView username;
    private ImageButton news;
    private ImageButton list;
    private ImageButton search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = (TextView) findViewById(R.id.username);
        news = (ImageButton) findViewById(R.id.news);
        list = (ImageButton) findViewById(R.id.list);
        search = (ImageButton) findViewById(R.id.search);

        username.setOnClickListener(this);
        news.setOnClickListener(this);
        list.setOnClickListener(this);
        search.setOnClickListener(this);

        SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

        if (!userSession.isLogged()) {
            Intent intent = new Intent(this, BridgeDeviceActivity.class);
            startActivity(intent);
            finish();
        } else {
            username.setText(userSession.getUser().getFirstname() + " " + userSession.getUser().getLastname());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.username:
                Log.e("HomeActivity", "Username clicked.");
                intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break ;
            case R.id.news:
                Log.e("HomeActivity", "News clicked.");
                intent = new Intent(this, NewsResultActivity.class);
                startActivity(intent);
                break ;
            case R.id.list:
                Log.e("HomeActivity", "List clicked.");
                intent = new Intent(this, ListActivity.class);
                startActivity(intent);
                break ;
            case R.id.search:
                Log.e("HomeActivity", "Search clicked.");
                intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break ;
            default:
                Log.e("HomeActivity", "DEFAULT");
                break ;
        }
    }
}
