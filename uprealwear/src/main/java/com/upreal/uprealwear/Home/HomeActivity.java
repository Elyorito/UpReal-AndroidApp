package com.upreal.uprealwear.home;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import com.upreal.uprealwear.R;
import com.upreal.uprealwear.utils.NotificationActivity;

public class HomeActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent notifIntent = new Intent(this, NotificationActivity.class);
        PendingIntent notifPendingIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notif = new Notification();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }
}
