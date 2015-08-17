package com.upreal.uprealwear.bridge;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.upreal.uprealwear.R;
import com.upreal.uprealwear.home.HomeActivity;
import com.upreal.uprealwear.server.UserManager;
import com.upreal.uprealwear.user.SessionManagerUser;
import com.upreal.uprealwear.utils.User;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class BridgeDeviceActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String USER_KEY = "com.upreal.uprealwear.key.user";

    private GoogleApiClient client;

    private int idUser = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        start(2);
    }

    @Override
    protected void onResume() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(client, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(client, this);
        client.disconnect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.e("BridgeDeviceActivity", "Item Changed.");
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/user") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    start(dataMap.getInt(USER_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.e("BridgeDeviceActivity", "Item Deleted.");
            }
        }
    }

    // Our method to update the count
    private void start(int idUser) {
        Log.e("BridgeDeviceActivity", "ID USER:" + idUser);
        this.idUser = idUser;
        if (idUser != 0)
            new RetrieveUser().execute();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
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
