package com.upreal.upreal.bridge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.upreal.upreal.R;
import com.upreal.upreal.utils.SessionManagerUser;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class BridgeDeviceActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String USER_KEY = "com.upreal.uprealwear.key.user";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        SessionManagerUser sessionManagerUser = new SessionManagerUser(getApplicationContext());

        if (sessionManagerUser.isLogged())
            sendUser(sessionManagerUser.getUserId());
    }

    private void sendUser(int idUser) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/user");
        putDataMapReq.getDataMap().putInt(USER_KEY, idUser);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(client, putDataReq);
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
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/user") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    display(dataMap.getInt(USER_KEY));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    // Our method to update the count
    private void display(int c) {
        Log.e("BridgeDeviceActivity", "ID USER:" + c);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
