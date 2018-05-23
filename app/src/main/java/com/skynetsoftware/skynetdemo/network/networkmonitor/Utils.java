package com.skynetsoftware.skynetdemo.network.networkmonitor;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.VmPolicy;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.WindowManager.BadTokenException;
import com.google.android.gms.maps.model.LatLng;
import com.skynetsoftware.skynetdemo.R;
import com.skynetsoftware.skynetdemo.network.networkmonitor.activity.MainActivity;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.DataEvent;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.Measurement;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.SpeedInfo;
import com.skynetsoftware.skynetdemo.network.networkmonitor.sqllitehandler.DBAdapter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import static android.app.PendingIntent.FLAG_NO_CREATE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class Utils {
    private static final double BYTE_TO_KILOBIT = 0.0078125d;
    public static final int IO_BUFFER_SIZE = 8192;
    private static final double KILOBIT_TO_MEGABIT = 9.765625E-4d;
    public static final String PREFS_ACCURACY = "accuracy";
    public static final String PREFS_ALTITUDE = "altitude";
    public static final String PREFS_AVG_PING = "avgping";
    public static final String PREFS_CELLID = "cellid";
    public static final String PREFS_CONN_INFO = "conninfo";
    public static final String PREFS_CONN_TYPE = "conntype";
    public static final String PREFS_CQI = "cqi";
    public static final String PREFS_DOWNLINK_BIT = "dl_bitrate";
    public static final String PREFS_EVENT = "event";
    public static final String PREFS_IMEI = "imei";
    public static final String PREFS_IMSI = "imsi";
    public static final String PREFS_LAC = "lac";
    public static final String PREFS_LATITUDE = "latitude";
    public static final String PREFS_LOCATION_SOURCE = "locationsource";
    public static final String PREFS_LONGITUDE = "longitude";
    public static final String PREFS_LTERSSI = "lterssi";
    public static final String PREFS_MAX_PING = "maxping";
    public static final String PREFS_MCC = "mcc";
    public static final String PREFS_MIN_PING = "minping";
    public static final String PREFS_MNC = "mnc";
    public static final String PREFS_MSISDN = "msisdn";
    public static final String PREFS_NCELLID1 = "ncellid1";
    public static final String PREFS_NCELLID2 = "ncellid2";
    public static final String PREFS_NCELLID3 = "ncellid3";
    public static final String PREFS_NCELLID4 = "ncellid4";
    public static final String PREFS_NCELLID5 = "ncellid5";
    public static final String PREFS_NCELLID6 = "ncellid6";
    public static final String PREFS_NLAC1 = "nlac1";
    public static final String PREFS_NLAC2 = "nlac2";
    public static final String PREFS_NLAC3 = "nlac3";
    public static final String PREFS_NLAC4 = "nlac4";
    public static final String PREFS_NLAC5 = "nlac5";
    public static final String PREFS_NLAC6 = "nlac6";
    public static final String PREFS_NODE = "node";
    public static final String PREFS_NRXLEV1 = "nrxlev1";
    public static final String PREFS_NRXLEV2 = "nrxlev2";
    public static final String PREFS_NRXLEV3 = "nrxlev3";
    public static final String PREFS_NRXLEV4 = "nrxlev4";
    public static final String PREFS_NRXLEV5 = "nrxlev5";
    public static final String PREFS_NRXLEV6 = "nrxlev6";
    public static final String PREFS_NW_LEVEL = "nw_level";
    public static final String PREFS_NW_MODE = "network_mode";
    public static final String PREFS_NW_TYPE = "network_type";
    public static final String PREFS_OPERATOR_NAME = "operatorname";
    public static final String PREFS_PING_LOSS = "pingloss";
    public static final String PREFS_PSC = "psc";
    public static final String PREFS_QUAL = "qual";
    public static final String PREFS_SNR = "snr";
    public static final String PREFS_SPEED = "speed";
    public static final String PREFS_STATE = "state";
    public static final String PREFS_STDEV_PING = "stdevping";
    public static final String PREFS_TEST_DOWNLINK_BIT = "testdlbitrate";
    public static final String PREFS_TEST_UPLINK_BIT = "testulbitrate";
    public static final String PREFS_TIMESPAMP = "timestamp";
    public static final String PREFS_UPLINK_BIT = "ul_bitrate";
    public final String LOG_TAG = "Utils_LOG";

    static class C03041 implements OnClickListener {
        C03041() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public static Measurement getMeasurement() {
        try {
            return MainActivity.measurement.clone();
        } catch (CloneNotSupportedException e) {
            return MainActivity.measurement;
        }
    }

    public static void alertInfo(Context context, String info) {
        try {
            Builder builder = new Builder(context);
            builder.setMessage((CharSequence) info).setCancelable(false).setPositiveButton((int) R.string.confirm_ok, new C03041());
            builder.create().show();
        } catch (BadTokenException ex) {
            ex.printStackTrace();
        }
    }

    public static void removeFromPrefs(Context context, String key) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }

    public static void saveToPrefs(Context context, String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (value != null) {
            editor.putString(key, value);
        } else {
            editor.putString(key, "-");
        }
        editor.commit();
    }

    public static String getFromPrefs(Context context, String key, String defaultValue) {
        try {
            defaultValue = PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static void scheduleAlarm(Context context, Class receiverClass, int requestCode, long repeatTime) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, receiverClass);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + repeatTime, PendingIntent.getBroadcast(context, requestCode, intent, FLAG_UPDATE_CURRENT));
    }

    public static void cancelAlarm(Context context, Class receiverClass, int requestCode) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, new Intent(context, receiverClass), FLAG_NO_CREATE);
        if (pi != null) {
            am.cancel(pi);
        }
    }

    public static boolean checkAlarmUp(Context context, Class chkClass, int requestCode) {
        return PendingIntent.getBroadcast(context, requestCode, new Intent(new Intent(context, chkClass)), FLAG_NO_CREATE) != null;
    }

    public static void createDumpFile(long fileSizeBytes) throws Throwable {
        IOException ex;
        Throwable th;
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "NetworkMonitorLOGs");
        RandomAccessFile newSparseFile = null;
        String fileName = "dumpFile.db";
        try {
            File file;
            File file2;
            if (dir.exists()) {
                file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "NetworkMonitorLOGs/" + fileName);
                if (!file2.exists()) {
                    file2.createNewFile();
                    file = file2;
                } else if (file2.length() != fileSizeBytes) {
                    file2.delete();
                    file = new File(Environment.getExternalStorageDirectory() + File.separator + "NetworkMonitorLOGs/" + fileName);
                    file.createNewFile();
                } else {
                    file = file2;
                }
            } else {
                dir.mkdir();
                file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "NetworkMonitorLOGs/" + fileName);
                try {
                    file2.createNewFile();
                    file = file2;
                } catch (IOException e) {
                    ex = e;
                    file = file2;
                    try {
                        ex.printStackTrace();
                        if (newSparseFile == null) {
                            try {
                                newSparseFile.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                                return;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (newSparseFile != null) {
                            try {
                                newSparseFile.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    file = file2;
                    if (newSparseFile != null) {
                        newSparseFile.close();
                    }
                    throw th;
                }
            }
            RandomAccessFile newSparseFile2 = new RandomAccessFile(file.getAbsolutePath(), "rw");
            try {
                newSparseFile2.setLength(fileSizeBytes);
                if (newSparseFile2 != null) {
                    try {
                        newSparseFile2.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                        newSparseFile = newSparseFile2;
                        return;
                    }
                }
                newSparseFile = newSparseFile2;
            } catch (IOException e3) {
                ex = e3;
                newSparseFile = newSparseFile2;
                ex.printStackTrace();
                if (newSparseFile == null) {
                    newSparseFile.close();
                }
            } catch (Throwable th4) {
                th = th4;
                newSparseFile = newSparseFile2;
                if (newSparseFile != null) {
                    newSparseFile.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            ex = e4;
            ex.printStackTrace();
            if (newSparseFile == null) {
                newSparseFile.close();
            }
        }
    }

    public static SpeedInfo calculate(long downloadTime, long bytesIn) {
        SpeedInfo info = new SpeedInfo();
        long bytespersecond = (bytesIn / downloadTime) * 1000;
        double kilobits = ((double) bytespersecond) * BYTE_TO_KILOBIT;
        double megabits = kilobits * KILOBIT_TO_MEGABIT;
        info.downspeed = (double) bytespersecond;
        info.kilobits = kilobits;
        info.megabits = megabits;
        return info;
    }

    public static double[] degrees2meters(double lon, double lat) {
        double x = (lon * 2.003750834E7d) / 180.0d;
        double y = ((Math.log(Math.tan(((90.0d + lat) * 3.141592653589793d) / 360.0d)) / 0.017453292519943295d) * 2.003750834E7d) / 180.0d;
        return new double[]{x, y};
    }

    public static double[] meters2degress(double x, double y) {
        double lon = (180.0d * x) / 2.003750834E7d;
        double lat = 57.29577951308232d * ((2.0d * Math.atan(Math.exp((3.141592653589793d * y) / 180.0d))) - 1.5707963267948966d);
        lat = ((Math.atan(Math.exp((3.141592653589793d * y) / 2.003750834E7d)) * 360.0d) / 3.141592653589793d) - 90.0d;
        return new double[]{lon, lat};
    }

    @TargetApi(11)
    public static void enableStrictMode() {
        if (hasGingerbread()) {
            ThreadPolicy.Builder threadPolicyBuilder = new ThreadPolicy.Builder().detectAll().penaltyLog();
            VmPolicy.Builder vmPolicyBuilder = new VmPolicy.Builder().detectAll().penaltyLog();
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasFroyo() {
        return VERSION.SDK_INT >= 8;
    }

    public static boolean hasGingerbread() {
        return VERSION.SDK_INT >= 9;
    }

    public static boolean hasHoneycomb() {
        return VERSION.SDK_INT >= 11;
    }

    public static boolean hasHoneycombMR1() {
        return VERSION.SDK_INT >= 12;
    }

    public static boolean hasJellyBean() {
        return VERSION.SDK_INT >= 16;
    }

    public static boolean hasKitKat() {
        return VERSION.SDK_INT >= 19;
    }

    public static boolean isExternalStorageRemovable() {
        if (VERSION.SDK_INT >= 9) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    public static File getExternalCacheDir(Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalCacheDir();
        }
        return new File(Environment.getExternalStorageDirectory().getPath() + ("/Android/data/" + context.getPackageName() + "/cache/"));
    }

    public static boolean hasExternalCacheDir() {
        return VERSION.SDK_INT >= 8;
    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(1);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Align.LEFT);
        float baseline = -paint.ascent();
        Bitmap image = Bitmap.createBitmap((int) (paint.measureText(text) + 0.5f), (int) ((paint.descent() + baseline) + 0.5f), Config.ARGB_8888);
        new Canvas(image).drawText(text, 0.0f, baseline, paint);
        return image;
    }

    public LinkedHashMap<Integer, String> sortHashMapByValues(HashMap<Integer, String> passedMap) {
        List<Integer> mapKeys = new ArrayList(passedMap.keySet());
        List<String> mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
        LinkedHashMap<Integer, String> sortedMap = new LinkedHashMap();
        for (String val : mapValues) {
            Iterator<Integer> keyIt = mapKeys.iterator();
            while (keyIt.hasNext()) {
                Integer key = (Integer) keyIt.next();
                if (((String) passedMap.get(key)).equals(val)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    public static LatLng getSecondPoint(LatLng first, double degress, double distance) {
        double radian = Math.toRadians(degress);
        return new LatLng(first.latitude + (Math.cos(radian) * distance), first.longitude + (Math.sin(radian) * distance));
    }

    public static boolean checkConnectionToServer(String host, int port) {
        boolean z = false;
        Socket s = null;
        try {
            Socket s2 = new Socket(host, port);
            z = true;
            if (s2 != null) {
                try {
                    s2.close();
                } catch (Exception e) {
                }
            }

            s = s2;
        } catch (Exception e2) {
            z = false;
            if (s != null) {
                try {
                    s.close();
                } catch (Exception e3) {
                }
            }
        } catch (Throwable th) {
            if (s != null) {
                try {
                    s.close();
                } catch (Exception e4) {
                }
            }
        }
        return z;
    }

    public static void addNewMeasurement(Context context, DataEvent event, Float downloadThroughput, Float uploadThroughput) {
        Measurement object = getMeasurement();
        if (object != null) {
            DBAdapter dbAdapter = DBAdapter.getINSTANCE(context);
            object.setTimestamp(new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss").format(new Date()));
            if (event != null) {
                object.setEvent(event.toString());
                if (event.equals(DataEvent.DOWNLOADING) && downloadThroughput != null) {
                    object.setDl_bitrate(downloadThroughput.toString());
                    object.setTestdlbitrate(downloadThroughput.toString());
                }
                if (event.equals(DataEvent.UPLOADING) && uploadThroughput != null) {
                    object.setUl_bitrate(uploadThroughput.toString());
                    object.setTestulbitrate(uploadThroughput.toString());
                }
            }
            Log.i("Downlink Sequence", "addNewMeasurement!");
            if (dbAdapter.addMeasurement(object) == -1) {
            }
        }
    }

    public static String[] getPingStats(String s) {
        ArrayList<String> result = new ArrayList();
        int start = s.indexOf(" ---\n");
        for (String item : s.substring(start + 5, s.indexOf("ms\n", start) + 2).split(", ")) {
            String item2 = null;
            if (item2.contains(" packets transmitted")) {
                item2 = item2.replace(" packets transmitted", "");
            }
            if (item2.contains(" received")) {
                item2 = item2.replace(" received", "");
            }
            if (item2.contains(" packet loss")) {
                item2 = item2.replace(" packet loss", "");
            }
            if (item2.contains("time ")) {
                item2 = item2.replace("time ", "");
            }
            result.add(item2);
        }
        int start2 = s.indexOf("/mdev = ");
        for (String item22 : s.substring(start2 + 8, s.indexOf(" ms", start2)).split("/")) {
            result.add(item22);
        }
        return (String[]) result.toArray(new String[result.size()]);
    }
}
