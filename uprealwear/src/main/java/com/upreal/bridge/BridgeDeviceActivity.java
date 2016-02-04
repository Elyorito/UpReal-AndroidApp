package com.upreal.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.upreal.R;
import com.upreal.home.HomeActivity;
import com.upreal.server.UserManager;
import com.upreal.user.SessionManagerUser;
import com.upreal.utils.User;

/**
 * Created by Kyosukke on 17/08/2015.
 */

public class BridgeDeviceActivity extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {

    private int idUser = 11;
    private GoogleApiClient gClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);
        gClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        new RetrieveUser().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        gClient.connect();
    }

    @Override
    protected void onStop() {
        if (gClient != null && gClient.isConnected()) {
            gClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gClient.disconnect();
    }

    @Override
    public void onConnected( Bundle bundle ) {
        Wearable.MessageApi.addListener(gClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onMessageReceived(MessageEvent mEvent) {
        Log.e("BridgeDeviceActivity", mEvent.getPath());
        if (mEvent.getPath().equals("/connect")) {
            final String message = new String(mEvent.getData());
            Log.e("ListenerService", "Message path received on watch is : " + mEvent.getPath());
            Log.e("ListenerService", "Message is:" + message);
            start(Integer.parseInt(message));
        }
        else {
            Log.e("ListenerService", "Error.");
            Log.e("ListenerService", "Message path received on watch is : " + mEvent.getPath());
        }
    }

    // Our method to update the count
    private void start(int idUser) {
        Log.e("BridgeDeviceActivity", "ID USER:" + idUser);
        this.idUser = idUser;
        if (idUser > 0)
            new RetrieveUser().execute();
    }

    private class RetrieveUser extends AsyncTask<Void, Void, User> {
        UserManager um = new UserManager();

        @Override
        protected User doInBackground(Void... params) {
            return um.getAccountInfo(idUser);
        }

        protected void onPostExecute(User res) {
            SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());
            userSession.setUser(res);
            userSession.login();

            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}