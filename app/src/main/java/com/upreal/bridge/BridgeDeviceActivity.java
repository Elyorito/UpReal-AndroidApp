package com.upreal.bridge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.upreal.upreal.R;
import com.upreal.utils.SessionManagerUser;

/**
 * Created by Kyosukke on 17/08/2015.
 */
public class BridgeDeviceActivity extends Activity implements GoogleApiClient.ConnectionCallbacks {

    private static final String CONNECT_ACCOUNT = "/connect_account";

    private GoogleApiClient client;

    private TextView msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        msg = (TextView) findViewById(R.id.msg);

        Log.e("BridgeDeviceActivity", "Start");
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        client.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.disconnect();
        Log.e("BridgeDeviceActivity", "Destroy");
    }

    private void sendMessage(final String path, final String text) {
        Log.e("BridgeDeviceActivity", "Send");
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(client).await();
                for(Node node : nodes.getNodes()) {
                    Wearable.MessageApi.sendMessage(client, node.getId(), path, text.getBytes()).await();
                }
            }
        }).start();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("BridgeDeviceActivity", "Connect");
        SessionManagerUser userSession = new SessionManagerUser(getApplicationContext());

        if (userSession.isLogged()) {
            sendMessage(CONNECT_ACCOUNT, "" + userSession.getUserId());
            msg.setText(getString(R.string.connected));
        }
        else
            msg.setText(getString(R.string.result_failed));
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}