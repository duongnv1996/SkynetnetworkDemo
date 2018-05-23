package com.skynetsoftware.skynetdemo.network.networkmonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.skynetsoftware.skynetdemo.network.networkmonitor.activity.MainActivity;


public class ConnectionMonitorReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            NetworkInfo activeNetwork = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            String networkInfo = "";
            if (isConnected) {
                MainActivity.measurement.setDataconn_type(Integer.valueOf(activeNetwork.getType()));
                String networkTypeName = activeNetwork.getTypeName();
                String networkSubTypeName = activeNetwork.getSubtypeName();
                if (activeNetwork.getType() == 1) {
                    MainActivity.measurement.setDataconn_info(networkTypeName + "-\"" + activeNetwork.getExtraInfo() + "\"");
                    return;
                } else if (activeNetwork.getType() != 0) {
                    MainActivity.measurement.setDataconn_info(networkTypeName + "-" + networkSubTypeName);
                    return;
                } else if (MainActivity.measurement.getOperatorname() == null || MainActivity.measurement.getOperatorname().trim().equals("")) {
                    MainActivity.measurement.setDataconn_info("MOBILE-" + networkSubTypeName);
                    return;
                } else {
                    MainActivity.measurement.setDataconn_info(MainActivity.measurement.getOperatorname() + "-" + networkSubTypeName);
                    return;
                }
            }
            MainActivity.measurement.setDataconn_type(null);
            MainActivity.measurement.setDataconn_info("No connection");
        }
    }
}
