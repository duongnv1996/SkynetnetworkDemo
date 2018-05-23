package com.skynetsoftware.skynetdemo.network.networkmonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import com.skynetsoftware.skynetdemo.R;
import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.Measurement;
import com.skynetsoftware.skynetdemo.network.networkmonitor.sqllitehandler.DBAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddMeasurementReceiver extends BroadcastReceiver {
    public static final String BUNDLE = "ADD_MEASUMENT_BUNDLE";
    private static final String LOG_TAG = "ADDMEASUREMENT_LOG";
    public static final int PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    public static final int REQUEST_CODE = 1;
    private static LocationManager locationManager;

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Measurement object = Utils.getMeasurement();
            if (object != null) {
                DBAdapter dbAdapter = DBAdapter.getINSTANCE(context);
                object.setTimestamp(new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date()));
                if (dbAdapter.addMeasurement(object) != -1) {
                    sendBroadcastToMapReceiver(context, object);
                }
            }
        }
        Utils.scheduleAlarm(context, AddMeasurementReceiver.class, 1, (long) (Integer.parseInt(Utils.getFromPrefs(context, context.getString(R.string.key_logParameter_timeInterval), "3")) * 1000));
    }

    public static void sendBroadcastToMapReceiver(Context context, Measurement msm) {
        if (msm.getLongitude() != null && !msm.getLongitude().trim().equals("") && !msm.getLongitude().trim().equals("-") && msm.getLatitude() != null && !msm.getLatitude().trim().equals("") && !msm.getLatitude().trim().equals("-")) {
            Intent sendMeasurement = new Intent();
           // sendMeasurement.setAction(MyMapFragment.MAP_RECEIVER);
            if (sendMeasurement != null) {
                Bundle bundle = new Bundle();
                bundle.putString("mcc", msm.getMcc());
                bundle.putString("mnc", msm.getMnc());
                bundle.putString("timestamp", msm.getTimestamp());
                bundle.putString(Utils.PREFS_NW_MODE, msm.getNetwork_mode());
                bundle.putString("network_type", msm.getNetwork_type());
                bundle.putString("lac", msm.getLac());
                bundle.putString("cellid", msm.getCellid());
                bundle.putString("node", msm.getNode());
                bundle.putString("nw_level", msm.getNw_level());
                bundle.putString("qual", msm.getQual());
                bundle.putString("testdlbitrate", msm.getTestdlbitrate());
                bundle.putString("testulbitrate", msm.getTestulbitrate());
                bundle.putString("dl_bitrate", msm.getDl_bitrate());
                bundle.putString("ul_bitrate", msm.getUl_bitrate());
                bundle.putString("speed", msm.getSpeed());
                bundle.putString("snr", msm.getSnr());
                bundle.putString(Utils.PREFS_LONGITUDE, msm.getLongitude());
                bundle.putString(Utils.PREFS_LATITUDE, msm.getLatitude());
                bundle.putString("locationsource", msm.getLocationsource());
                sendMeasurement.putExtra(BUNDLE, bundle);
                context.sendBroadcast(sendMeasurement);
            }
        }
    }
}
