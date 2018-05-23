package com.skynetsoftware.skynetdemo.network.networkmonitor.listener;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;
import com.skynetsoftware.skynetdemo.network.networkmonitor.activity.MainActivity;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.NetworkMode;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.PhoneState;
import com.skynetsoftware.skynetdemo.network.networkmonitor.receiver.ServingCellCapture;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class NMPhoneStateListener extends PhoneStateListener {
    public static final String BUNDLE = "NMPhoneStateListener.BUNDLE";
    public static final String CELL_INFO_CHANGED = "NMPhoneStateListener.CELL_INFO_CHANGED";
    public static final String CELL_LOCATION_CHANGED = "NMPhoneStateListener.CELL_LOCATION_CHANGED";
    public static final String CHANGE_TYPE = "NMPhoneStateListener.CHANGE_TYPE";
    public static final String DATA_ACTIVITY_CHANGED = "NMPhoneStateListener.DATA_ACTIVITY_CHANGED";
    public static final String DATA_CONNECTION_STATE_CHANGED = "NMPhoneStateListener.DATA_CONNECTION_STATE_CHANGED";
    public static final String MODE_CHANGED = "NMPhoneStateListener.MODE_CHANGED";
    public static final String SIGNAL_STRENGTH_CHANGED = "NMPhoneStateListener.SIGNAL_STRENGTH_CHANGED";
    public static final String TAG = NMPhoneStateListener.class.getName();
    private static String cellId = "-";
    private static String cqi = "-";
    private static String lac = "-";
    private static String level = "-";
    public static List<String[]> li = new ArrayList();
    private static String mode = "-";
    private static String node = "-";
    private static String qual = "-";
    private static String snr = "-";
    public static String state = "";
    private static String type = "-";
    private Activity activity;
    List<NeighboringCellInfo> neiCellInfoList = new ArrayList();
    private int networkType;
    private TelephonyManager telManager;

    public NMPhoneStateListener(Activity activity) {
        this.activity = activity;
        this.telManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        getPhoneAndOperatorInfo();
        getNetworkMode();
        getNeighborCellList();
    }

    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        snr = "";
        cqi = "";
        if (signalStrength.isGsm()) {
            if (mode.equals(NetworkMode.MODE_4G.toString())) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                List<CellInfo> cellInfoList = this.telManager.getAllCellInfo();
                if (cellInfoList != null) {
                    for (CellInfo cellInfo : cellInfoList) {
                        if (cellInfo.isRegistered() && (cellInfo instanceof CellInfoLte)) {
                            ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
                            int cellSig = ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
                            if (cellSig >= 0) {
                                cellSig *= -1;
                            }
                            level = String.valueOf(cellSig);
                            String[] parts = signalStrength.toString().split(" ");
                            qual = String.valueOf(parts[10]);
                            snr = String.valueOf(parts[11]);
                            cqi = String.valueOf(parts[12]);
                        }
                    }
                }
            } else {
                int signalASU = signalStrength.getGsmSignalStrength();
                int signaldBm = signalASU;
                if (signalASU >= 0) {
                    signaldBm = (signalASU * 2) - 113;
                }
                if (signalASU == 99 || signalASU == 9999) {
                    level = "-200";
                } else {
                    level = String.valueOf(signaldBm);
                }
                int qual = signalStrength.getGsmBitErrorRate();
                int qual3g = signalStrength.getGsmBitErrorRate() * -1;
                if (qual == 99) {
                    qual = qual;
                } else if (mode.equals(NetworkMode.MODE_2G.toString())) {
                    qual = qual;
                } else if (mode.equals(NetworkMode.MODE_3G.toString())) {
                    qual =qual3g;
                } else {
                    qual = qual;
                }
            }
        }
        if (level.equals("-200") || level.equals("-")) {
            int strength;
            if (this.networkType == 3 || this.networkType == 8 || this.networkType == 9 || this.networkType == 10 || this.networkType == 15) {
                strength = signalStrength.getCdmaDbm();
                if (strength < -120 || strength > -32) {
                    level = String.valueOf(-200);
                } else {
                    level = String.valueOf(strength);
                }
            } else if (this.networkType == 4 || this.networkType == 7) {
                strength = signalStrength.getCdmaDbm();
                if (strength == -1 || strength == -120) {
                    level = String.valueOf(-200);
                } else {
                    level = String.valueOf(strength);
                }
            } else if (this.networkType == 5 || this.networkType == 6 || this.networkType == 12 || this.networkType == 14) {
                strength = signalStrength.getCdmaDbm();
                if (strength == -1 || strength == -120) {
                    level = String.valueOf(-200);
                } else {
                    level = String.valueOf(strength);
                }
            } else if (this.networkType == 13) {
                try {
                    for (Method mthd : SignalStrength.class.getMethods()) {
                        if (mthd.getName().equals("getLteRsrp")) {
                            level = String.valueOf(mthd.invoke(signalStrength, new Object[0]));
                        }
                        if (mthd.getName().equals("getLteRsrq")) {
                            qual = String.valueOf(mthd.invoke(signalStrength, new Object[0]));
                        }
                        if (mthd.getName().equals("getLteRssnr")) {
                            snr = String.valueOf(mthd.invoke(signalStrength, new Object[0]));
                        }
                        if (mthd.getName().equals("getLteCqi")) {
                            cqi = String.valueOf(mthd.invoke(signalStrength, new Object[0]));
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                } catch (InvocationTargetException e4) {
                    e4.printStackTrace();
                }
            } else if (signalStrength.getEvdoDbm() < 0) {
                level = String.valueOf(signalStrength.getEvdoDbm());
            } else if (signalStrength.getCdmaDbm() < 0) {
                level = String.valueOf(signalStrength.getCdmaDbm());
            } else {
                level = String.valueOf(-200);
            }
        }
        MainActivity.measurement.setCellTechType(this.networkType);
        Intent intent = new Intent();
        intent.setAction(ServingCellCapture.RECEIVER);
        if (intent != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CHANGE_TYPE, SIGNAL_STRENGTH_CHANGED);
            bundle.putString("level", level);
            bundle.putString("qual", qual);
            bundle.putString("snr", snr);
            bundle.putString("cqi", cqi);
            bundle.putString("androidCellType", signalStrength.toString().replaceAll("\\|", ":"));
            intent.putExtra(BUNDLE, bundle);
            this.activity.sendBroadcast(intent);
        }
        getNeighborCellList();
    }

    public void onCellLocationChanged(CellLocation location) {
        getPhoneAndOperatorInfo();
        getNetworkMode();
        super.onCellLocationChanged(location);
        try {
            PrintWriter pw = new PrintWriter(new FileWriter((Environment.getExternalStorageDirectory().getPath() + "/NetworkMonitorLOGs/applicationLog/") + "rt.log", true));
            Calendar ca = GregorianCalendar.getInstance();
            ca.setTimeInMillis(System.currentTimeMillis());
            pw.write(ca.getTime().toString() + "|onCellLocationChanged|" + MainActivity.measurement.getOperatorname() + "|" + MainActivity.measurement.getNetwork_mode() + "|" + MainActivity.measurement.getNetwork_type() + "|" + location.getClass().toString() + "\n");
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GsmCellLocation cellLocation = (GsmCellLocation) location;
        if (cellLocation != null) {
            if (cellLocation.getCid() == -1) {
                cellId = "-";
                node = "-";
            } else if (mode.equals(NetworkMode.MODE_4G.toString())) {
                int cid = cellLocation.getCid();
                cellId = String.valueOf(cid % 256);
                node = String.valueOf(cid / 256);
            } else {
                cellId = String.valueOf(cellLocation.getCid() & 65535);
            }
            if (cellLocation.getLac() != -1) {
                lac = String.valueOf(cellLocation.getLac() & 65535);
            } else {
                lac = "-";
                node = "-";
            }
            if (mode.equals(NetworkMode.MODE_2G.toString())) {
                node = "-";
            } else if (mode.equals(NetworkMode.MODE_3G.toString())) {
                node = String.valueOf((cellLocation.getCid() >> 16) & 65535);
            }
        }
        Intent intent = new Intent();
        intent.setAction(ServingCellCapture.RECEIVER);
        if (intent != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CHANGE_TYPE, CELL_LOCATION_CHANGED);
            bundle.putString("node", node);
            bundle.putString("lac", lac);
            bundle.putString("cellId", cellId);
            intent.putExtra(BUNDLE, bundle);
            this.activity.sendBroadcast(intent);
        }
        getNeighborCellList();
    }

    public void onCellInfoChanged(List<CellInfo> cellInfo) {
        super.onCellInfoChanged(cellInfo);
    }

    public void onDataConnectionStateChanged(int state) {
        super.onDataConnectionStateChanged(state);
        if (state == 1) {
            state = 2;
        }
        if (state == 0) {
            state = 0;
        }
        Intent intent = new Intent();
        intent.setAction(ServingCellCapture.RECEIVER);
        if (intent != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CHANGE_TYPE, DATA_CONNECTION_STATE_CHANGED);
            bundle.putString(Utils.PREFS_STATE, String.valueOf(state));
            intent.putExtra(BUNDLE, bundle);
            this.activity.sendBroadcast(intent);
        }
    }

    public void onDataActivity(int direction) {
        super.onDataActivity(direction);
        if (direction == 0) {
            state = PhoneState.IDLE.toString();
        }
        if (direction == 3) {
            state = PhoneState.DATA.toString();
        }
        Intent intent = new Intent();
        intent.setAction(ServingCellCapture.RECEIVER);
        if (intent != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CHANGE_TYPE, DATA_ACTIVITY_CHANGED);
            bundle.putString(Utils.PREFS_STATE, state);
            intent.putExtra(BUNDLE, bundle);
            this.activity.sendBroadcast(intent);
        }
    }

    private void getPhoneAndOperatorInfo() {
        MainActivity.measurement.setOperatorname(this.telManager.getNetworkOperatorName());
        String nwOperator = this.telManager.getNetworkOperator();
        if (nwOperator == null || nwOperator.trim().equals("")) {
            MainActivity.measurement.setMcc("-");
            MainActivity.measurement.setMnc("-");
        } else {
            MainActivity.measurement.setMcc(nwOperator.substring(0, 3));
            MainActivity.measurement.setMnc(nwOperator.substring(3, nwOperator.length()));
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        MainActivity.measurement.setImsi(this.telManager.getSubscriberId());

        MainActivity.measurement.setImei(this.telManager.getDeviceId());
        MainActivity.measurement.setMsisdn(this.telManager.getLine1Number());
    }

    private void getNetworkMode() {
        this.networkType = this.telManager.getNetworkType();
        switch (this.networkType) {
            case 1:
                type = "GPRS";
                mode = NetworkMode.MODE_2G.toString();
                break;
            case 2:
                type = "EDGE";
                mode = NetworkMode.MODE_2G.toString();
                break;
            case 3:
                type = "UMTS";
                mode = NetworkMode.MODE_3G.toString();
                break;
            case 4:
                type = "CDMA";
                mode = NetworkMode.MODE_2G.toString();
                break;
            case 5:
                type = "EVDO-0";
                mode = NetworkMode.MODE_3G.toString();
                break;
            case 6:
                type = "EVDO-A";
                mode = NetworkMode.MODE_3G.toString();
                break;
            case 7:
                type = "1xRTT";
                mode = NetworkMode.MODE_2G.toString();
                break;
            case 8:
                type = "HSDPA";
                mode = NetworkMode.MODE_3G.toString();
                break;
            case 9:
                type = "HSUPA";
                mode = NetworkMode.MODE_3G.toString();
                break;
            case 10:
                type = "HSPA";
                mode = NetworkMode.MODE_3G.toString();
                break;
            case 11:
                type = "IDEN";
                mode = NetworkMode.MODE_2G.toString();
                break;
            case 12:
                type = "EVDO-B";
                mode = NetworkMode.MODE_3G.toString();
                break;
            case 13:
                type = "LTE";
                mode = NetworkMode.MODE_4G.toString();
                break;
            case 14:
                type = "EHRPD";
                mode = NetworkMode.MODE_3G.toString();
                break;
            case 15:
                type = "HSPA+";
                mode = NetworkMode.MODE_3G.toString();
                break;
            default:
                type = "-";
                mode = "-";
                break;
        }
        Intent intent = new Intent();
        intent.setAction(ServingCellCapture.RECEIVER);
        if (intent != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CHANGE_TYPE, MODE_CHANGED);
            bundle.putString("mode", mode);
            bundle.putString("type", type);
            intent.putExtra(BUNDLE, bundle);
            this.activity.sendBroadcast(intent);
        }
    }

    public void getNeighborCellList() {
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            this.neiCellInfoList = this.telManager.getNeighboringCellInfo();
            if (this.neiCellInfoList != null) {
                if (!(li.isEmpty() || this.neiCellInfoList.isEmpty())) {
                    for (int i = 0; i < li.size(); i++) {
                        li.remove(i);
                    }
                }
                for (NeighboringCellInfo item : this.neiCellInfoList) {
                    String[] one = new String[5];
                    one[0] = String.valueOf(item.getNetworkType());
                    one[1] = String.valueOf(item.getLac());
                    one[2] = String.valueOf(item.getCid());
                    int rssi = item.getRssi();
                    if (rssi == 99) {
                        one[3] = "-";
                    } else {
                        one[3] = String.valueOf((rssi * 2) - 113);
                    }
                    li.add(one);
                }
                if (li.size() >= 1) {
                    MainActivity.measurement.setNlac1(((String[]) li.get(0))[1]);
                    MainActivity.measurement.setNcellid1(((String[]) li.get(0))[2]);
                    MainActivity.measurement.setNrxlev1(((String[]) li.get(0))[3]);
                }
                if (li.size() >= 2) {
                    MainActivity.measurement.setNlac2(((String[]) li.get(1))[1]);
                    MainActivity.measurement.setNcellid2(((String[]) li.get(1))[2]);
                    MainActivity.measurement.setNrxlev2(((String[]) li.get(1))[3]);
                }
                if (li.size() >= 3) {
                    MainActivity.measurement.setNlac3(((String[]) li.get(2))[1]);
                    MainActivity.measurement.setNcellid3(((String[]) li.get(2))[2]);
                    MainActivity.measurement.setNrxlev3(((String[]) li.get(2))[3]);
                }
                if (li.size() >= 4) {
                    MainActivity.measurement.setNlac4(((String[]) li.get(3))[1]);
                    MainActivity.measurement.setNcellid4(((String[]) li.get(3))[2]);
                    MainActivity.measurement.setNrxlev4(((String[]) li.get(3))[3]);
                }
                if (li.size() >= 5) {
                    MainActivity.measurement.setNlac5(((String[]) li.get(4))[1]);
                    MainActivity.measurement.setNcellid5(((String[]) li.get(4))[2]);
                    MainActivity.measurement.setNrxlev5(((String[]) li.get(4))[3]);
                }
                if (li.size() >= 6) {
                    MainActivity.measurement.setNlac6(((String[]) li.get(5))[1]);
                    MainActivity.measurement.setNcellid6(((String[]) li.get(5))[2]);
                    MainActivity.measurement.setNrxlev6(((String[]) li.get(5))[3]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
