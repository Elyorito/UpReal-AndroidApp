package com.upreal.bridge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.upreal.R;
import com.upreal.utils.SessionManagerUser;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class BridgeDeviceActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {

    int CONNECTION_TIME_OUT_MS = 5000;
    GoogleApiClient gClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        gClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

        if (userSession != null && userSession.isLogged()) {
            new SendToDataLayerThread("/connect", userSession.getUserId() + "").start();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
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

    public class SendToDataLayerThread extends Thread {
        String path;
        String message;

        SendToDataLayerThread(String p, String msg) {
            path = p;
            message = msg;
        }

        public void run() {
            gClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(gClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(gClient, node.getId(), path, message.getBytes()).await();
                if (result.getStatus().isSuccess()) {
                    Log.e("SendToDataLayerThread", "Message: {" + message + "} was sent to " + node.getDisplayName());
                }
                else {
                    Log.e("SendToDataLayerThread", "ERROR: failed to send message");
                }
            }
        }
    }
}