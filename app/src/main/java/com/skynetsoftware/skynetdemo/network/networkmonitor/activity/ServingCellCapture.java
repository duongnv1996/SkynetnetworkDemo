package com.skynetsoftware.skynetdemo.network.networkmonitor.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;
import com.skynetsoftware.skynetdemo.network.networkmonitor.listener.MyLocationListener;
import com.skynetsoftware.skynetdemo.network.networkmonitor.listener.NMPhoneStateListener;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.Measurement;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.ServingCell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ServingCellCapture extends BroadcastReceiver {
    public static final String RECEIVER = "ServingCellCapture.RECEIVER";
    private Context context;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Bundle bundle = intent.getBundleExtra(NMPhoneStateListener.BUNDLE);
        if (bundle != null) {
            handlePhoneStateChange(bundle, context);
        }
        bundle = intent.getBundleExtra(MyLocationListener.BUNDLE);
        if (bundle != null) {
            handleLocationChange(bundle, context);
        }
    }

    private void handlePhoneStateChange(Bundle bundle, Context context) {
        String type = bundle.getString(NMPhoneStateListener.CHANGE_TYPE);
        int obj = -1;
        switch (type.hashCode()) {
            case -1558437584:
                if (type.equals(NMPhoneStateListener.MODE_CHANGED)) {
                    obj = 0;
                    break;
                }
                break;
            case -587331307:
                if (type.equals(NMPhoneStateListener.SIGNAL_STRENGTH_CHANGED)) {
                    obj = 1;
                    break;
                }
                break;
            case -168303857:
                if (type.equals(NMPhoneStateListener.CELL_LOCATION_CHANGED)) {
                    obj = 2;
                    break;
                }
                break;
            case -119213688:
                if (type.equals(NMPhoneStateListener.CELL_INFO_CHANGED)) {
                    obj = 3;
                    break;
                }
                break;
            case 522202913:
                if (type.equals(NMPhoneStateListener.DATA_ACTIVITY_CHANGED)) {
                    obj = 5;
                    break;
                }
                break;
            case 1147300322:
                if (type.equals(NMPhoneStateListener.DATA_CONNECTION_STATE_CHANGED)) {
                    obj = 4;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0:
                MainActivity.measurement.setNetwork_mode(bundle.getString("mode"));
                MainActivity.measurement.setNetwork_type(bundle.getString("type"));
                break;
            case 1:
                MainActivity.measurement.setNw_level(bundle.getString("level"));
                MainActivity.measurement.setQual(bundle.getString("qual"));
                if(!bundle.getString("snr").isEmpty())
                MainActivity.measurement.setSnr(bundle.getString("snr"));
                MainActivity.measurement.setAndroidCellType(bundle.getString("androidCellType"));
                break;
            case 2:
                MainActivity.measurement.setCellid(bundle.getString("cellId"));
                MainActivity.measurement.setLac(bundle.getString("lac"));
                MainActivity.measurement.setNode(bundle.getString("node"));
                if (bundle.getString("cellId") == null || bundle.getString("cellId").isEmpty() || bundle.getString("cellId").trim().equals("-") || bundle.getString("cellId").trim().equals("--")) {
                    MainActivity.measurement.setNw_level("-");
                    MainActivity.measurement.setQual("-");
                    MainActivity.measurement.setSnr("-");
                    MainActivity.measurement.setNetwork_mode("-");
                    MainActivity.measurement.setNetwork_type("-");
                    break;
                }
            case 4:
                MainActivity.measurement.setState(bundle.getString(Utils.PREFS_STATE));
                break;
            case 5:
                MainActivity.measurement.setState(bundle.getString(Utils.PREFS_STATE));
                break;
        }
        if (MainActivity.listServingCells == null) {
            MainActivity.listServingCells = new ArrayList();
        } else if (MainActivity.listServingCells.size() == 0) {
            addNewServingCel();
        } else {
            ServingCell currentServingCell = (ServingCell) MainActivity.listServingCells.get(MainActivity.listServingCells.size() - 1);
            String mode = MainActivity.measurement.getNetwork_mode();
            String lac = MainActivity.measurement.getLac();
            String cellId = MainActivity.measurement.getCellid();
            if (!currentServingCell.getMode().equals(mode) || !currentServingCell.getLac().equals(lac) || !currentServingCell.getCellId().equals(cellId)) {
                updateCurrentServingCell();
                addNewServingCel();
            }
        }
    }

    private void handleLocationChange(Bundle bundle, Context context) {
        String type = bundle.getString(MyLocationListener.CHANGE_TYPE);
        int obj = -1;
        switch (type.hashCode()) {
            case 323584867:
                if (type.equals(MyLocationListener.LOCATION_CHANGED)) {
                    obj = 0;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0:
                MainActivity.measurement.setLongitude(bundle.getString(Utils.PREFS_LONGITUDE));
                MainActivity.measurement.setLatitude(bundle.getString(Utils.PREFS_LATITUDE));
                MainActivity.measurement.setSpeed(bundle.getString("speed"));
                MainActivity.measurement.setAccuracy(bundle.getString("accuracy"));
                MainActivity.measurement.setAltitude(bundle.getString("altitude"));
                MainActivity.measurement.setLocationsource(bundle.getString("location"));
                return;
            default:
                return;
        }
    }

    private void addNewServingCel() {
        Date currentDate = Calendar.getInstance().getTime();
        Measurement ms = MainActivity.measurement;
        MainActivity.listServingCells.add(new ServingCell(ms.getOperatorname(), currentDate, ms.getMcc(), ms.getMnc(), ms.getLac(), ms.getNode(), ms.getCellid(), ms.getNw_level(), ms.getQual(), ms.getNetwork_mode(), ms.getNetwork_type(), 1, ""));
    }

    private void updateCurrentServingCell() {
        if (MainActivity.listServingCells.size() > 0) {
            ServingCell sc = (ServingCell) MainActivity.listServingCells.get(MainActivity.listServingCells.size() - 1);
            sc.setServingTime((int) ((System.currentTimeMillis() - sc.getTime().getTime()) / 1000));
        }
    }
}
