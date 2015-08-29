package com.upreal.uprealwear.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.upreal.uprealwear.R;
import com.upreal.uprealwear.home.HomeActivity;
import com.upreal.uprealwear.server.UserManager;
import com.upreal.uprealwear.user.SessionManagerUser;
import com.upreal.uprealwear.utils.User;

/**
 * Created by Kyosukke on 17/08/2015.
 */

public class BridgeDeviceActivity extends Activity implements MessageApi.MessageListener, GoogleApiClient.ConnectionCallbacks {

    private static final String CONNECT_ACCOUNT = "/connect_account";

    private int idUser = 0;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);
        initGoogleApiClient();
        start(2);
    }

    private void initGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        if( client != null && !(client.isConnected() || client.isConnecting()))
            client.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(client != null && !(client.isConnected() || client.isConnecting()))
            client.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (client != null) {
            Wearable.MessageApi.removeListener(client, this);
            if (client.isConnected()) {
                client.disconnect();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(client != null)
            client.unregisterConnectionCallbacks(this);
        super.onDestroy();
    }

    // Our method to update the count
    private void start(int idUser) {
        Log.e("BridgeDeviceActivity", "ID USER:" + idUser);
        this.idUser = idUser;
        if (idUser > 0)
            new RetrieveUser().execute();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(client, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onMessageReceived(final MessageEvent messageEvent) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                if( messageEvent.getPath().equalsIgnoreCase(CONNECT_ACCOUNT)) {
                    start(Integer.parseInt(new String(messageEvent.getData())));
                }
            }
        });
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