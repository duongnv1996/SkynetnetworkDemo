package com.skynetsoftware.skynetdemo.network.networkmonitor.sqllitehandler;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

import com.skynetsoftware.skynetdemo.R;
import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.Measurement;
import com.skynetsoftware.skynetdemo.network.networkmonitor.receiver.StartLogReceiver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.bmartel.protocol.http.constants.HttpHeader;
import fr.bmartel.protocol.http.constants.HttpMethod;


public class DBAdapter extends SQLiteOpenHelper {
    public static final String COL_ACCURACY = "accuracy";
    public static final String COL_ALTITUDE = "altitude";
    public static final String COL_AVG_PING = "avgping";
    public static final String COL_CELLID = "cellid";
    public static final String COL_CONN_INFO = "conninfo";
    public static final String COL_CONN_TYPE = "conntype";
    public static final String COL_CQI = "cqi";
    public static final String COL_DOWNLINK_BIT = "dl_bitrate";
    public static final String COL_EVENT = "event";
    public static final String COL_ID = "_id";
    public static final String COL_IMEI = "imei";
    public static final String COL_IMSI = "imsi";
    public static final String COL_LAC = "lac";
    public static final String COL_LATITUDE = "lat";
    public static final String COL_LEVEL = "nw_level";
    public static final String COL_LOCATION_SOURCE = "locationsource";
    public static final String COL_LONGITUDE = "lon";
    public static final String COL_LTERSSI = "lterssi";
    public static final String COL_MAX_PING = "maxping";
    public static final String COL_MCC = "mcc";
    public static final String COL_MIN_PING = "minping";
    public static final String COL_MNC = "mnc";
    public static final String COL_MSISDN = "msisdn";
    public static final String COL_NCELLID1 = "ncellid1";
    public static final String COL_NCELLID2 = "ncellid2";
    public static final String COL_NCELLID3 = "ncellid3";
    public static final String COL_NCELLID4 = "ncellid4";
    public static final String COL_NCELLID5 = "ncellid5";
    public static final String COL_NCELLID6 = "ncellid6";
    public static final String COL_NLAC1 = "nlac1";
    public static final String COL_NLAC2 = "nlac2";
    public static final String COL_NLAC3 = "nlac3";
    public static final String COL_NLAC4 = "nlac4";
    public static final String COL_NLAC5 = "nlac5";
    public static final String COL_NLAC6 = "nlac6";
    public static final String COL_NODE = "node";
    public static final String COL_NRXLEV1 = "nrslev1";
    public static final String COL_NRXLEV2 = "nrslev2";
    public static final String COL_NRXLEV3 = "nrslev3";
    public static final String COL_NRXLEV4 = "nrslev4";
    public static final String COL_NRXLEV5 = "nrslev5";
    public static final String COL_NRXLEV6 = "nrslev6";
    public static final String COL_NW_TYPE = "network_type";
    public static final String COL_OPERATOR_NAME = "operatorname";
    public static final String COL_PING_LOSS = "pingloss";
    public static final String COL_PSC = "psc";
    public static final String COL_QUAL = "qual";
    public static final String COL_SNR = "snr";
    public static final String COL_SPEED = "speed";
    public static final String COL_STDEV_PING = "stdevping";
    public static final String COL_TEST_DOWNLINK_BIT = "testdlbitrate";
    public static final String COL_TEST_UPLINK_BIT = "testulbitrate";
    public static final String COL_TIMESTAMP = "timestamp";
    public static final String COL_UPLINK_BIT = "ul_bitrate";
    public static final String DB_NAME = "measurement.db";
    public static final int DB_VERSION = 1;
    static DBAdapter INSTANCE = null;
    static Queue<MeasurementDetail> dataLog = null;
    static Queue<MeasurementDetail> dataSend = null;
    static int logBuffer = 20;
    private final String CREATE_TABLE_MEASUREMENT = "CREATE TABLE IF NOT EXISTS measurement    (_id                      INTEGER PRIMARY KEY AUTOINCREMENT,\n    timestamp                            TEXT,\n    lon                            TEXT,\n    lat                            TEXT,\n    nw_level                       TEXT,\n    speed                          TEXT,\n    operatorname                   TEXT,\n    mcc                            TEXT,\n    mnc                            TEXT,\n    node                           TEXT,\n    cellid                         TEXT,\n    lac                            TEXT,\n    network_type                   TEXT,\n    qual                           TEXT,\n    snr                            TEXT,\n    cqi                            TEXT,\n    lterssi                        TEXT,\n    psc                            TEXT,\n    dl_bitrate                     TEXT,\n    ul_bitrate                     TEXT,\n    nlac1                          TEXT,\n    ncellid1                       TEXT,\n    nrslev1                        TEXT,\n    nlac2                          TEXT,\n    ncellid2                       TEXT,\n    nrslev2                        TEXT,\n    nlac3                       TEXT,\n    ncellid3                        TEXT,\n    nrslev3                          TEXT,\n    nlac4                       TEXT,\n    ncellid4                        TEXT,\n    nrslev4                        TEXT,\n    nlac5                          TEXT,\n    ncellid5                       TEXT,\n    nrslev5                        TEXT,\n    nlac6                          TEXT,\n    ncellid6                       TEXT,\n    nrslev6                        TEXT,\n    event                          TEXT,\n    accuracy                       TEXT,\n    locationsource                TEXT,\n    altitude                       TEXT,\n    conntype                      TEXT,\n    conninfo                      TEXT,\n    avgping                       TEXT,\n    minping                       TEXT,\n    maxping                       TEXT,\n    stdevping                     TEXT,\n    pingloss                      TEXT,\n    testulbitrate                TEXT,\n    testdlbitrate              TEXT,\n    msisdn                         TEXT,\n    imei                           TEXT,\n    imsi                           TEXT)";
    public final String LOG_TAG = "DBAdapter_LOG";
    private final String TABLE_MEASUREMENT = "measurement";
    Context context;
    Runnable logDataJob = new C04362();
    Runnable sendDataJob = new C04351();
    private IOException e3;

    class C04351 implements Runnable {
        C04351() {
        }

        public void run() {
            IOException ex;
            Throwable th;
            HttpURLConnection conn = null;
            OutputStreamWriter outputStreamWriter = null;
            try {
                if (((ConnectivityManager) DBAdapter.this.context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
                    Log.i("SendDataOnline", "No network connection");
                    if (outputStreamWriter != null) {
                        try {
                            outputStreamWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                        return;
                    }
                    return;
                }
                conn = (HttpURLConnection) new URL(Utils.getFromPrefs(DBAdapter.this.context, DBAdapter.this.context.getString(R.string.key_sendData_URLSendData), "http://csm.vnpt.vn/measurement_receive.php")).openConnection();
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty(HttpHeader.CONNECTION, "Keep-Alive");
                conn.setRequestProperty(HttpHeader.CONTENT_TYPE, "application/x-www-form-urlencoded");
                conn.setRequestMethod(HttpMethod.POST_REQUEST);
                JSONArray jsonArray = new JSONArray();
                while (!DBAdapter.dataSend.isEmpty()) {
                    try {
                        JSONObject obj = new JSONObject();
                        MeasurementDetail detail = (MeasurementDetail) DBAdapter.dataSend.poll();
                        if (detail != null) {
                            obj.put("received_time", detail.getReceivedTime());
                            obj.put("content", detail.getDetail());
                            jsonArray.put(obj);
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                try {
                    writer.write("data=" + jsonArray.toString());
                    writer.flush();
                    Log.i("DBAdapter_LOG", "Sent data=" + jsonArray.toString());
                    if (conn.getResponseCode() == 200) {
                        Log.i("DBAdapter_LOG", "Sent data success ");
                    } else {
                        Log.i("DBAdapter_LOG", "Connecting fail! " + conn.getResponseMessage());
                    }
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                        outputStreamWriter = writer;
                        return;
                    }
                } catch (IOException e4) {
                    ex = e4;
                    outputStreamWriter = writer;
                    try {
                        ex.printStackTrace();
                        if (outputStreamWriter != null) {
                            try {
                                outputStreamWriter.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (outputStreamWriter != null) {
                            try {
                                outputStreamWriter.close();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        if (conn != null) {
                            conn.disconnect();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    outputStreamWriter = writer;
                    if (outputStreamWriter != null) {
                        outputStreamWriter.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                    throw th;
                }
            } catch (Throwable e5) {
                ex = (IOException) e5;
                ex.printStackTrace();
                if (outputStreamWriter != null) {
                    try {
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
    }

    class C04362 implements Runnable {
        C04362() {
        }

        public void run() {
            IOException e;
            Throwable th;
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fOut = new FileOutputStream(StartLogReceiver.file, true);
                while (!DBAdapter.dataLog.isEmpty()) {
                    try {
                        fOut.write(((MeasurementDetail) DBAdapter.dataLog.poll()).getDetail().getBytes());
                    } catch (IOException e2) {
                        e = e2;
                        fileOutputStream = fOut;
                    } catch (Throwable th2) {
                        th = th2;
                        fileOutputStream = fOut;
                    }
                }
                fOut.close();
                if (fOut != null) {
                    try {
                        fOut.close();
                        fileOutputStream = fOut;
                        return;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        fileOutputStream = fOut;
                        return;
                    }
                }
            } catch (IOException e4) {
                e3 = e4;
                try {
                    e3.printStackTrace();
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e322) {
                            e322.printStackTrace();
                        }
                    }
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
    }

    class MeasurementDetail {
        String detail;
        String receivedTime;

        public MeasurementDetail(String receivedTime, String detail) {
            this.receivedTime = receivedTime;
            this.detail = detail;
        }

        public String getReceivedTime() {
            return this.receivedTime;
        }

        public void setReceivedTime(String receivedTime) {
            this.receivedTime = receivedTime;
        }

        public String getDetail() {
            return this.detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

    public static DBAdapter getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DBAdapter(context);
        }
        return INSTANCE;
    }

    public DBAdapter(Context context) {
        super(context, "measurement.db", null, 1);
        this.context = context;
    }

    public DBAdapter(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public DBAdapter(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS measurement    (_id                      INTEGER PRIMARY KEY AUTOINCREMENT,\n    timestamp                            TEXT,\n    lon                            TEXT,\n    lat                            TEXT,\n    nw_level                       TEXT,\n    speed                          TEXT,\n    operatorname                   TEXT,\n    mcc                            TEXT,\n    mnc                            TEXT,\n    node                           TEXT,\n    cellid                         TEXT,\n    lac                            TEXT,\n    network_type                   TEXT,\n    qual                           TEXT,\n    snr                            TEXT,\n    cqi                            TEXT,\n    lterssi                        TEXT,\n    psc                            TEXT,\n    dl_bitrate                     TEXT,\n    ul_bitrate                     TEXT,\n    nlac1                          TEXT,\n    ncellid1                       TEXT,\n    nrslev1                        TEXT,\n    nlac2                          TEXT,\n    ncellid2                       TEXT,\n    nrslev2                        TEXT,\n    nlac3                       TEXT,\n    ncellid3                        TEXT,\n    nrslev3                          TEXT,\n    nlac4                       TEXT,\n    ncellid4                        TEXT,\n    nrslev4                        TEXT,\n    nlac5                          TEXT,\n    ncellid5                       TEXT,\n    nrslev5                        TEXT,\n    nlac6                          TEXT,\n    ncellid6                       TEXT,\n    nrslev6                        TEXT,\n    event                          TEXT,\n    accuracy                       TEXT,\n    locationsource                TEXT,\n    altitude                       TEXT,\n    conntype                      TEXT,\n    conninfo                      TEXT,\n    avgping                       TEXT,\n    minping                       TEXT,\n    maxping                       TEXT,\n    stdevping                     TEXT,\n    pingloss                      TEXT,\n    testulbitrate                TEXT,\n    testdlbitrate              TEXT,\n    msisdn                         TEXT,\n    imei                           TEXT,\n    imsi                           TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DBAdapter_LOG", "Update database from ver " + oldVersion + " to " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS measurement");
        onCreate(db);
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.finalize();
    }

    public long addMeasurement(Measurement msm) {
        long j = -1;
        if (!(msm == null || msm.getLongitude() == null || msm.getLatitude() == null || msm.getLatitude().isEmpty() || msm.getLongitude().isEmpty() || msm.getLatitude().equalsIgnoreCase("-") || msm.getLongitude().equalsIgnoreCase("-"))) {
            j = 1;
            String deviceName = Build.MODEL;
            String deviceMan = Build.MANUFACTURER;
            String androidCodeName = String.valueOf(VERSION.SDK_INT);
            if (StartLogReceiver.START_LOG_FILE_FLAG) {
                if (dataLog == null) {
                    dataLog = new LinkedList();
                }
                MeasurementDetail detail = new MeasurementDetail(msm.getTimestamp(), msm.getTimestamp() + "|" + msm.getLongitude() + "|" + msm.getLatitude() + "|" + msm.getSpeed() + "|" + msm.getOperatorname() + "|" + msm.getMcc() + msm.getMnc() + "|" + msm.getMcc() + msm.getMnc() + msm.getLac() + msm.getCellid() + "|" + msm.getNode() + "|" + msm.getCellid() + "|" + msm.getLac() + "|" + msm.getNetwork_mode() + "|" + msm.getNetwork_type() + "|" + msm.getNw_level() + "|" + msm.getQual() + "|" + msm.getSnr() + "|" + msm.getCqi() + "|" + msm.getLterssi() + "|" + msm.getDl_bitrate() + "|" + msm.getUl_bitrate() + "|" + msm.getPsc() + "|" + msm.getAltitude() + "|" + msm.getAccuracy() + "|" + msm.getLocationsource() + "|" + msm.getNlac1() + "|" + msm.getNcellid1() + "|" + msm.getNrxlev1() + "|" + msm.getNlac2() + "|" + msm.getNcellid2() + "|" + msm.getNrxlev2() + "|" + msm.getNlac3() + "|" + msm.getNcellid3() + "|" + msm.getNrxlev3() + "|" + msm.getNlac4() + "|" + msm.getNcellid4() + "|" + msm.getNrxlev4() + "|" + msm.getNlac5() + "|" + msm.getNcellid5() + "|" + msm.getNrxlev5() + "|" + msm.getNlac6() + "|" + msm.getNcellid6() + "|" + msm.getNrxlev6() + "|" + msm.getState() + "|" + msm.getAvgping() + "|" + msm.getMinping() + "|" + msm.getMaxping() + "|" + msm.getStdevping() + "|" + msm.getPingloss() + "|" + msm.getTestdlbitrate() + "|" + msm.getTestulbitrate() + "|" + msm.getDataconn_type() + "|" + msm.getDataconn_info() + "|" + msm.getMsisdn() + "|" + msm.getImsi() + "|" + msm.getImei() + "|" + deviceName + "|" + deviceMan + "|" + androidCodeName + "|" + msm.getAndroidCellType() + "|" + msm.getCellTechType() + "|" + msm.getBmk() + "|" + msm.getEvent() + "|" + (msm.getCallDuration() == null ? "" : msm.getCallDuration()) + "\n");
                dataLog.add(detail);
                if (Utils.getFromPrefs(this.context, this.context.getString(R.string.key_sendData_sendDataOnline), "TRUE").equalsIgnoreCase("TRUE")) {
                    if (dataSend == null) {
                        dataSend = new LinkedList();
                    }
                    dataSend.add(detail);
                    if (dataSend.size() >= logBuffer) {
                        Thread sendDataThread = new Thread(this.sendDataJob);
                        Log.d("ABCDEF", "sendDataThread start");
                        sendDataThread.start();
                    }
                }
                if (dataLog.size() >= logBuffer) {
                    new Thread(this.logDataJob).start();
                }
            } else {
                if (dataLog != null && dataLog.size() > 0) {
                    new Thread(this.logDataJob).start();
                }
                if (dataSend != null && dataSend.size() > 0) {
                    new Thread(this.sendDataJob).start();
                }
            }
        }
        return j;
    }

    public ArrayList<Measurement> getMeasurements(int numOfMs) {
        Exception e;
        ArrayList<Measurement> msList = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("measurement", new String[]{"timestamp", "lon", "lat", "nw_level", "speed", "operatorname", "mcc", "mnc", "node", "cellid", "lac", "network_type", "qual", "snr", "cqi", "lterssi", "psc", "dl_bitrate", "ul_bitrate", "nlac1", "ncellid1", "nrslev1", "nlac2", "ncellid2", "nrslev2", "nlac3", "ncellid3", "nrslev3", "nlac4", "ncellid4", "nrslev4", "nlac5", "ncellid5", "nrslev5", "nlac6", "ncellid6", "nrslev6", "event", "accuracy", "locationsource", "altitude", "conntype", "conninfo", "avgping", "minping", "maxping", "stdevping", "pingloss", "testulbitrate", "testdlbitrate", "msisdn", "imei", "imsi"}, null, null, null, null, "_id DESC", String.valueOf(numOfMs));
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            try {
                Measurement ms = new Measurement();
                Measurement measurement;
                try {
                    ms.setMcc(cursor.getString(cursor.getColumnIndex("mcc")));
                    ms.setMnc(cursor.getString(cursor.getColumnIndex("mnc")));
                    ms.setTimestamp(cursor.getString(cursor.getColumnIndex("timestamp")));
                    ms.setNetwork_mode(cursor.getString(cursor.getColumnIndex("network_type")));
                    ms.setLac(cursor.getString(cursor.getColumnIndex("lac")));
                    ms.setCellid(cursor.getString(cursor.getColumnIndex("cellid")));
                    ms.setNode(cursor.getString(cursor.getColumnIndex("node")));
                    ms.setNw_level(cursor.getString(cursor.getColumnIndex("nw_level")));
                    ms.setQual(cursor.getString(cursor.getColumnIndex("qual")));
                    ms.setTestdlbitrate(cursor.getString(cursor.getColumnIndex("testdlbitrate")));
                    ms.setTestulbitrate(cursor.getString(cursor.getColumnIndex("testulbitrate")));
                    ms.setDl_bitrate(cursor.getString(cursor.getColumnIndex("dl_bitrate")));
                    ms.setUl_bitrate(cursor.getString(cursor.getColumnIndex("ul_bitrate")));
                    ms.setSpeed(cursor.getString(cursor.getColumnIndex("speed")));
                    ms.setSnr(cursor.getString(cursor.getColumnIndex("snr")));
                    ms.setLongitude(cursor.getString(cursor.getColumnIndex("lon")));
                    ms.setLatitude(cursor.getString(cursor.getColumnIndex("lat")));
                    ms.setLocationsource(cursor.getString(cursor.getColumnIndex("locationsource")));
                    msList.add(ms);
                    cursor.moveToNext();
                    measurement = ms;
                } catch (Exception e2) {
                    e = e2;
                    measurement = ms;
                    e.printStackTrace();
                }
            } catch (Exception e3) {
                e = e3;
                e.printStackTrace();
            }
        }
        cursor.close();
        db.close();
        return msList;
    }

    public Cursor getLastestMeasurement() {
        return getReadableDatabase().rawQuery("SELECT * FROM  measurement order by _id DESC LIMIT 1;", null);
    }

    public void truncateTableMeasurement() {
    }
}
