package com.upreal.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.bridge.BridgeDeviceActivity;
import com.upreal.global.NewsResultActivity;
import com.upreal.search.SearchActivity;
import com.upreal.user.ListActivity;
import com.upreal.user.ProfileActivity;
import com.upreal.user.SessionManagerUser;

public class HomeActivity extends Activity implements View.OnClickListener {

    private TextView username;
    private ImageButton news;
    private ImageButton list;
    private ImageButton search;
    private ImageButton disconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = (TextView) findViewById(R.id.username);
        news = (ImageButton) findViewById(R.id.news);
        list = (ImageButton) findViewById(R.id.list);
        search = (ImageButton) findViewById(R.id.search);
        disconnect = (ImageButton) findViewById(R.id.disconnect);

        username.setOnClickListener(this);
        news.setOnClickListener(this);
        list.setOnClickListener(this);
        search.setOnClickListener(this);
        disconnect.setOnClickListener(this);

        SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

        if (!userSession.isLogged()) {
            Intent intent = new Intent(this, BridgeDeviceActivity.class);
            startActivity(intent);
            finish();
        } else {
            username.setText(userSession.getUser().getUsername());
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
            case R.id.disconnect:
                SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());
                userSession.deleteCurrentUser();
                intent = new Intent(this, BridgeDeviceActivity.class);
                startActivity(intent);
                finish();
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
