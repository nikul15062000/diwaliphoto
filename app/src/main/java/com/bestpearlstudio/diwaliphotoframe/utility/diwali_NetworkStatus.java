package com.bestpearlstudio.diwaliphotoframe.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class diwali_NetworkStatus {


    public static boolean getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                if (activeNetwork.isConnectedOrConnecting())
                    return true;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                if (activeNetwork.isConnectedOrConnecting())
                    return true;
        }
        return false;
    }
}
