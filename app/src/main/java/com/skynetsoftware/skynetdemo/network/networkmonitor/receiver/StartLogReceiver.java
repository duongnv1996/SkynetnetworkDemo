package com.skynetsoftware.skynetdemo.network.networkmonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.skynetsoftware.skynetdemo.network.networkmonitor.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StartLogReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "STARTLOGFILE_LOG";
    public static boolean START_LOG_FILE_FLAG = false;
    public static File file = null;
    Context context;
    File dir = null;
    private IOException e2;

    public void onReceive(Context context, Intent intent) {
        IOException e;
        Throwable th;
        this.context = context;
        if (intent != null) {
            Date date = new Date();
            FileOutputStream fOut = null;
            String fileName = "LOG-" + new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(date) + "-" + MainActivity.measurement.getImei() + ".txt";
            try {
                this.dir = new File(Environment.getExternalStorageDirectory() + File.separator + "NetworkMonitorLOGs");
                if (this.dir.exists()) {
                    file = new File(Environment.getExternalStorageDirectory() + File.separator + "NetworkMonitorLOGs/" + fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } else {
                    this.dir.mkdirs();
                    file = new File(Environment.getExternalStorageDirectory() + File.separator + "NetworkMonitorLOGs/" + fileName);
                    file.createNewFile();
                }
                START_LOG_FILE_FLAG = true;
                String header = "Timestamp|Longitude|Latitude|Speed|Operatorname|Operator|CGI|Node|CellID|LAC|NetworkTech|NetworkMode|Level|Qual|SNR|CQI|LTERSSI|DL_bitrate|UL_bitrate|PSC|Altitude|Accuracy|Location|NLAC1|NCellid1|NRxLev1|NLAC2|NCellid2|NRxLev2|NLAC3|NCellid3|NRxLev3|NLAC4|NCellid4|NRxLev4|NLAC5|NCellid5|NRxLev5|NLAC6|NCellid6|NRxLev6|State|PINGAVG|PINGMIN|PINGMAX|PINGSTDEV|PINGLOSS|TESTDOWNLINK|TESTUPLINK|DataConnection_Type|DataConnection_Info|MSISDN|IMSI|IMEI\n";
                FileOutputStream fOut2 = new FileOutputStream(file, true);
                try {
                    fOut2.write(header.getBytes());
                    Toast.makeText(context, "Start writing data on Log File", Toast.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "Log File created at: " + file.getAbsolutePath() + " -- Start logging...");
                    if (fOut2 != null) {
                        try {
                            fOut2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (IOException e3) {
                    e2 = e3;
                    fOut = fOut2;
                    try {
                        e2.printStackTrace();
                        if (fOut != null) {
                            try {
                                fOut.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (fOut != null) {
                            try {
                                fOut.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    fOut = fOut2;
                    if (fOut != null) {
                        fOut.close();
                    }
                    throw th;
                }
            } catch (Throwable e4) {
                e4.printStackTrace();
                if (fOut != null) {
                    try {
                        fOut.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }
}
