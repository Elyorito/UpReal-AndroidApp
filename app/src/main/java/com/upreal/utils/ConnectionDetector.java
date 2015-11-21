package com.upreal.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Eric on 21/11/2015.
 */
public class ConnectionDetector {
    private Context mContext;

    public ConnectionDetector(Context context) {
        mContext = context;
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null)
                    if (info.getState() == NetworkInfo.State.CONNECTED || info.isConnectedOrConnecting())
                        return true;
        }
        return false;
    }
}
