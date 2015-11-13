package com.upreal.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.upreal.R;
import com.upreal.bridge.BridgeDeviceActivity;
import com.upreal.global.NewsResultActivity;
import com.upreal.search.SearchActivity;
import com.upreal.search.SearchResultActivity;
import com.upreal.user.ListActivity;
import com.upreal.user.ProfileActivity;
import com.upreal.user.SessionManagerUser;
import com.upreal.utils.Item;

import java.util.List;

public class HomeActivity extends Activity implements View.OnClickListener, View.OnLongClickListener {

    private TextView username;
    private ImageButton news;
    private ImageButton list;
    private ImageButton search;
    private ImageButton disconnect;
    private static final int SPEECH_REQUEST_CODE = 0;

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
        search.setOnLongClickListener(this);
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

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.username:
                break ;
            case R.id.disconnect:
                break ;
            case R.id.news:
                break ;
            case R.id.list:
                break ;
            case R.id.search:
                Log.e("HomeActivity", "Search long clicked.");
                recordSpeech();
                break ;
            default:
                Log.e("HomeActivity", "DEFAULT");
                break ;
        }
        return true;
    }

    private void recordSpeech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);

            Log.e("SPOKE?", spokenText);

            Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
            intent.putExtra("item", new Item(0, 0, spokenText, null));
            getApplicationContext().startActivity(intent);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
