package com.skynetsoftware.skynetdemo.network.networkmonitor.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.os.StrictMode.VmPolicy;
import android.provider.Settings;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.skynetsoftware.skynetdemo.R;
import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;
import com.skynetsoftware.skynetdemo.network.networkmonitor.asyncTask.LoadCellAsyncTask;
import com.skynetsoftware.skynetdemo.network.networkmonitor.listener.MyLocationListener;
import com.skynetsoftware.skynetdemo.network.networkmonitor.listener.NMPhoneStateListener;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.Cell;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.Measurement;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.ServingCell;
import com.skynetsoftware.skynetdemo.network.networkmonitor.receiver.AddMeasurementReceiver;
import com.skynetsoftware.skynetdemo.network.networkmonitor.receiver.ConnectionMonitorReceiver;
import com.skynetsoftware.skynetdemo.network.networkmonitor.receiver.DataSequenceReceiver;
import com.skynetsoftware.skynetdemo.network.networkmonitor.receiver.EndVoiceCallReceiver;
import com.skynetsoftware.skynetdemo.network.networkmonitor.receiver.StartVoiceCallReceiver;
import com.skynetsoftware.skynetdemo.network.networkmonitor.sqllitehandler.DBAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    public static String APP_LOG_DIR = null;
    public static String CELL_FILE_DIR = null;
    static final String CONFIG_TAG = "CONFIG_TAG";
    static final String DATA_TEST_TAG = "DATA_TEST_TAG";
    public static final String EXT_STORAGE_DIR = Environment.getExternalStorageDirectory().getPath();
    public static final String LOG_TAG = "MAINACTIVITY_LOG";
    static final String MAP_TAG = "MAP_TAG";
    static final String NEI_CELL_TAG = "NEI_CELL_TAG";
    static final int PERMISSIONS_REQUEST_ACCESS_PHONE_STATE = 500;
    static final int PERMISSION_ALL = 1;
    public static final int REQUEST_CODE = 10101;
    static final String SPEEDTEST_TAG = "SPEEDTEST_TAG";
    static final String SRV_CELL_TAG = "SRV_CELL_TAG";
    private static boolean callEndFlag = false;
    private static boolean callStartFlag = false;
    public static HashMap<String, Cell> cells2G = new HashMap();
    public static HashMap<String, Cell> cells3G = new HashMap();
    public static HashMap<String, Cell> cells4G = new HashMap();
    public static ArrayList<ServingCell> listServingCells = new ArrayList();
    private static LocationManager locationManager;
    public static Measurement measurement = new Measurement();
    static int numOfSubs;
    private static long voiceCallStartTime;
    protected Context context;
    private DrawerLayout drawerLayout;
    FragmentManager fm = getSupportFragmentManager();
    ConnectionMonitorReceiver mConnReceiver = new ConnectionMonitorReceiver();
    private Activity mCurrentActivity = null;
    MyLocationListener myLocationListener;
    Timer phoneStateListenerTimer;
    private NMPhoneStateListener psListener;
    private final ServingCellCapture servingCellCapture = new ServingCellCapture();
    ServingCellFragment servingCellFragment = new ServingCellFragment();
    SubscriptionInfo sub1 = null;
    SubscriptionInfo sub2 = null;
    SubscriptionManager subscriptionManager = null;
    private TelephonyManager telManager;
    private TelephonyManager telManagerFirst;
    private TelephonyManager telManagerSecond;
    private Toolbar toolbar;

    class C04251 implements UncaughtExceptionHandler {
        C04251() {
        }

        public void uncaughtException(Thread thread, Throwable ex) {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(MainActivity.APP_LOG_DIR + "rt.log", true));
                ex.printStackTrace(pw);
                pw.flush();
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C04262 extends TimerTask {
        C04262() {
        }

        public void run() {
            MainActivity.this.telManager.listen(MainActivity.this.psListener, 1520);
        }
    }


    public static void endCallFlag() {
        callStartFlag = false;
        callEndFlag = true;
    }

    public static void startCallFlag() {
        callStartFlag = true;
        voiceCallStartTime = System.currentTimeMillis();
        callEndFlag = false;
    }

    public static boolean isStartCallFlag() {
        return callStartFlag;
    }

    public static long getVoiceCallStartTime() {
        return voiceCallStartTime;
    }

    public static boolean isEndCallFlag() {
        return callEndFlag;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        String[] PERMISSIONS = new String[]{"android.permission.READ_PHONE_STATE", "android.permission.READ_SMS", "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CALL_PHONE", "android.permission.READ_EXTERNAL_STORAGE"};
        if (hasPermissions(this, PERMISSIONS)) {
            startAction();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (!(VERSION.SDK_INT < 23 || context == null || permissions == null)) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    void startAction() {
        Locale.setDefault(Locale.US);
        this.context = this;
        listServingCells = new ArrayList();
        getWindow().addFlags(128);
        setCurrentActivity(this);
        issetClientDir();
        StrictMode.setThreadPolicy(new Builder().permitAll().build());
        DBAdapter.getINSTANCE(this.context).truncateTableMeasurement();
        getDefaultTelephonyManager();
        registerListener();
        registerReceiver(this.mConnReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        IntentFilter filter = new IntentFilter();
        filter.addAction(ServingCellCapture.RECEIVER);
        registerReceiver(this.servingCellCapture, filter);
        Utils.scheduleAlarm(this.context, AddMeasurementReceiver.class, 1, 5);
        //ComponentName component = new ComponentName(this.context, OutgoingCallReceiver.class);
//        int status = this.context.getPackageManager().getComponentEnabledSetting(component);
//        if (status == 1) {
//            Log.d("onDestroy MainActivity", "OutgoingCallReceiver is enabled");
//        } else if (status == 2) {
//            Log.d("onDestroy MainActivity", "OutgoingCallReceiver is disable ");
//            this.context.getPackageManager().setComponentEnabledSetting(component, 1, 1);
//        }
        try {
            if (Utils.getFromPrefs(getBaseContext(), getResources().getString(R.string.key_siteView_checkCloestSiteRange), "").trim().equals("1")) {
            }
            if (new File(CELL_FILE_DIR + "cellfile.txt").exists()) {
                new LoadCellAsyncTask(this).execute(new Void[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServingCellFragment servingCell = new ServingCellFragment();
        FragmentTransaction fragmentTransaction = this.fm.beginTransaction();
        fragmentTransaction.add(R.id.frame,servingCell).commit();
//        String value = "";
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
////            value = extras.getString("fragment");
////            if (value != null) {
////                Log.d("HOANNT", "NOI DUNG PUTEXTRA" + value);
////                Object obj = -1;
////                switch (value.hashCode()) {
////                    case -1722335428:
////                        if (value.equals("DATATEST")) {
////                            obj = 1;
////                            break;
////                        }
////                        break;
////                    case 76092:
////                        if (value.equals("MAP")) {
////                            obj = 3;
////                            break;
////                        }
////                        break;
////                    case 1145035065:
////                        if (value.equals("SPEEDTEST")) {
////                            obj = 2;
////                            break;
////                        }
////                        break;
////                    case 1483402975:
////                        if (value.equals("NEIGHBOURCELL")) {
////                            obj = null;
////                            break;
////                        }
////                        break;
////                }
////
//
////            }
//            switchFragment(SRV_CELL_TAG);
//        } else {
//            switchFragment(SRV_CELL_TAG);
//        }
       // resetVoiceSequenceSharedPreferences();
    }

    private void getDefaultTelephonyManager() {
        this.telManager = (TelephonyManager) this.context.getSystemService(TELEPHONY_SERVICE);
    }

    private void getTelephonyManagersForLevel22_23(int subscriptionId) {
        try {
            String manufacture = Build.MANUFACTURER.trim().toUpperCase();
            if (manufacture.contains("SAMSUNG")) {
                if (subscriptionId == 0) {
                    Method m1 = TelephonyManager.class.getDeclaredMethod("getFirst", new Class[0]);
                    m1.setAccessible(true);
                    this.telManager = (TelephonyManager) m1.invoke(null, new Object[0]);
                } else if (subscriptionId == 1) {
                    Method m2 = TelephonyManager.class.getDeclaredMethod("getSecondary", new Class[0]);
                    m2.setAccessible(true);
                    this.telManager = (TelephonyManager) m2.invoke(null, new Object[0]);
                }
            } else if (!manufacture.contains("ASUS")) {
            }
        } catch (Exception e) {
        }
    }

    private void issetClientDir() {
        APP_LOG_DIR = EXT_STORAGE_DIR + "/NetworkMonitorLOGs/applicationLog/";
        File mydir1 = new File(APP_LOG_DIR);
        if (!mydir1.exists()) {
            mydir1.mkdirs();
        }
        CELL_FILE_DIR = EXT_STORAGE_DIR + "/NetworkMonitorLOGs/cellfile/";
        File mydir2 = new File(CELL_FILE_DIR);
        if (!mydir2.exists()) {
            mydir2.mkdirs();
        }
    }

    private void debugOnClient() {
        StrictMode.setThreadPolicy(new Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        Thread.currentThread().setUncaughtExceptionHandler(new C04251());
    }

    @TargetApi(23)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && !Settings.canDrawOverlays(this)) {
        }
    }


    private void registerListener() {
        this.phoneStateListenerTimer = new Timer();
        if (this.psListener == null) {
            this.psListener = new NMPhoneStateListener(this);
        }
        this.phoneStateListenerTimer.schedule(new C04262(), 0, 2000);
        if (locationManager == null) {
            locationManager = (LocationManager) this.context.getSystemService(LOCATION_SERVICE);
            if (!(ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this.context, "android.permission.ACCESS_COARSE_LOCATION") == 0)) {
                return;
            }
        }
        this.myLocationListener = new MyLocationListener(this, locationManager);
        this.myLocationListener.requestUpdateListener();
    }

    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    protected void onResume() {
        setCurrentActivity(this);
        super.onResume();
    }

    protected void onRestart() {
        super.onRestart();
    }

//
//    public void startVoiceSequence() {
//        if (this.voiceCallSequence == null) {
//            this.voiceCallSequence = new VoiceCallSequence(getBaseContext());
//        }
//        this.voiceCallSequence.startSequence();
//    }
//
//    public void stopVoiceSequence() {
//        if (this.voiceCallSequence != null) {
//            this.voiceCallSequence.endSequence();
//        }
//    }

    private void resetVoiceSequenceSharedPreferences() {
        Editor editor = getSharedPreferences(getString(R.string.voiceSequence_sharedPreference), 0).edit();
        editor.putInt(getString(R.string.voiceSequence_numOfCall), 0);
        editor.putInt(getString(R.string.voiceSequence_callDuration), Integer.parseInt(Utils.getFromPrefs(this, getString(R.string.key_voiceSequence_callDuration), "5")));
        editor.putInt(getString(R.string.voiceSequence_delayTime), Integer.parseInt(Utils.getFromPrefs(this, getString(R.string.key_voiceSequence_pauseCall), "10")));
        editor.putInt(getString(R.string.voiceSequence_totalCall), Integer.parseInt(Utils.getFromPrefs(this, getString(R.string.key_voiceSequence_numOfCall), "5")));
        editor.putString(getString(R.string.voiceSequence_callNumber), Utils.getFromPrefs(this, getString(R.string.key_voiceSequence_callednumber), "18001091"));
        editor.commit();
    }

    protected void onDestroy() {
        clearReferences();
        try {
            unregisterReceiver(this.servingCellCapture);
            unregisterReceiver(this.mConnReceiver);
//            ComponentName component = new ComponentName(this.context, OutgoingCallReceiver.class);
//            int status = this.context.getPackageManager().getComponentEnabledSetting(component);
//            try {
//                Log.d("onDestroy MainActivity", "OutgoingCallReceiver is enable");
//                this.context.getPackageManager().setComponentEnabledSetting(component, 2, 1);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (!(this.telManager == null || this.psListener == null)) {
            this.telManager.listen(this.psListener, 0);
        }
        if (this.phoneStateListenerTimer != null) {
            this.phoneStateListenerTimer.purge();
        }
        this.myLocationListener.removeUpdateListener();
        Utils.cancelAlarm(this.context, StartVoiceCallReceiver.class, 0);
        Utils.cancelAlarm(this.context, EndVoiceCallReceiver.class, 0);
        Utils.cancelAlarm(this.context, AddMeasurementReceiver.class, 1);
        Utils.cancelAlarm(this.context, DataSequenceReceiver.class, 2);
        DBAdapter.getINSTANCE(this.context).truncateTableMeasurement();
        Log.i(LOG_TAG, "onDestroy, Stop insert into and Truncate all Data in Measurement table");
//        if (this.myMapFragment != null) {
//            this.myMapFragment.releaseAllMapResource();
//        }
        super.onDestroy();
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                startAction();
                return;
            default:
                return;
        }
    }

    public Activity getCurrentActivity() {
        return this.mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    private void clearReferences() {
        if (equals(getCurrentActivity())) {
            setCurrentActivity(null);
        }
    }

    private void openUploadLogFileWeb() {
        Intent i = new Intent("android.intent.action.VIEW");
        i.setData(Uri.parse("http://csm.vnpt.vn/Vinaphone/insertfile.php"));
        startActivity(i);
    }

    private void switchFragment(String fragmentTag) {
//        FragmentTransaction fragmentTransaction = this.fm.beginTransaction();
////        this.myMapFragment = (MyMapFragment) this.fm.findFragmentByTag(MAP_TAG);
//        this.servingCellFragment = (ServingCellFragment) this.fm.findFragmentByTag(SRV_CELL_TAG);
////        this.neighborCellFragment = (NeighborCellFragment) this.fm.findFragmentByTag(NEI_CELL_TAG);
////        this.dataTestFragment = (DataTestFragment) this.fm.findFragmentByTag(DATA_TEST_TAG);
////        this.speedTestFragment = (SpeedTestFragment) this.fm.findFragmentByTag(SPEEDTEST_TAG);
////        this.configFragment = (ConfigFragment) this.fm.findFragmentByTag(CONFIG_TAG);
//        Object obj = -1;
//        switch (fragmentTag.hashCode()) {
//            case 33228660:
//                if (fragmentTag.equals(SPEEDTEST_TAG)) {
//                    obj = 4;
//                    break;
//                }
//                break;
//            case 539195210:
//                if (fragmentTag.equals(NEI_CELL_TAG)) {
//                    obj = 2;
//                    break;
//                }
//                break;
//            case 542990146:
//                if (fragmentTag.equals(DATA_TEST_TAG)) {
//                    obj = 3;
//                    break;
//                }
//                break;
//            case 1039369349:
//                if (fragmentTag.equals(SRV_CELL_TAG)) {
//                    obj = 1;
//                    break;
//                }
//                break;
//            case 1317894397:
//                if (fragmentTag.equals(CONFIG_TAG)) {
//                    obj = 5;
//                    break;
//                }
//                break;
//            case 1555996151:
//                if (fragmentTag.equals(MAP_TAG)) {
//                    obj = null;
//                    break;
//                }
//                break;
//        }
//        if (this.servingCellFragment != null) {
//            fragmentTransaction.hide(this.servingCellFragment);
//        }
////        switch (obj) {
////            case null:
////                if (this.servingCellFragment != null) {
////                    fragmentTransaction.hide(this.servingCellFragment);
////                }
////                if (this.neighborCellFragment != null) {
////                    fragmentTransaction.hide(this.neighborCellFragment);
////                }
////                if (this.dataTestFragment != null) {
////                    fragmentTransaction.hide(this.dataTestFragment);
////                }
////                if (this.speedTestFragment != null) {
////                    fragmentTransaction.hide(this.speedTestFragment);
////                }
////                if (this.configFragment != null) {
////                    fragmentTransaction.hide(this.configFragment);
////                }
////                if (this.myMapFragment != null) {
////                    fragmentTransaction.show(this.myMapFragment);
////                    break;
////                }
////                this.myMapFragment = new MyMapFragment();
////                fragmentTransaction.add(R.id.frame, this.myMapFragment, MAP_TAG);
////                break;
////            case 1:
////                if (this.myMapFragment != null) {
////                    fragmentTransaction.hide(this.myMapFragment);
////                }
////                if (this.neighborCellFragment != null) {
////                    fragmentTransaction.hide(this.neighborCellFragment);
////                }
////                if (this.dataTestFragment != null) {
////                    fragmentTransaction.hide(this.dataTestFragment);
////                }
////                if (this.speedTestFragment != null) {
////                    fragmentTransaction.hide(this.speedTestFragment);
////                }
////                if (this.configFragment != null) {
////                    fragmentTransaction.hide(this.configFragment);
////                }
////                if (this.servingCellFragment != null) {
////                    fragmentTransaction.show(this.servingCellFragment);
////                    break;
////                }
////                this.servingCellFragment = new ServingCellFragment();
////                fragmentTransaction.add(R.id.frame, this.servingCellFragment, SRV_CELL_TAG);
////                break;
////            case 2:
////                if (this.servingCellFragment != null) {
////                    fragmentTransaction.hide(this.servingCellFragment);
////                }
////                if (this.myMapFragment != null) {
////                    fragmentTransaction.hide(this.myMapFragment);
////                }
////                if (this.dataTestFragment != null) {
////                    fragmentTransaction.hide(this.dataTestFragment);
////                }
////                if (this.speedTestFragment != null) {
////                    fragmentTransaction.hide(this.speedTestFragment);
////                }
////                if (this.configFragment != null) {
////                    fragmentTransaction.hide(this.configFragment);
////                }
////                if (this.neighborCellFragment != null) {
////                    fragmentTransaction.show(this.neighborCellFragment);
////                    break;
////                }
////                this.neighborCellFragment = new NeighborCellFragment();
////                fragmentTransaction.add(R.id.frame, this.neighborCellFragment, NEI_CELL_TAG);
////                break;
////            case 3:
////                if (this.servingCellFragment != null) {
////                    fragmentTransaction.hide(this.servingCellFragment);
////                }
////                if (this.neighborCellFragment != null) {
////                    fragmentTransaction.hide(this.neighborCellFragment);
////                }
////                if (this.myMapFragment != null) {
////                    fragmentTransaction.hide(this.myMapFragment);
////                }
////                if (this.configFragment != null) {
////                    fragmentTransaction.hide(this.configFragment);
////                }
////                if (this.dataTestFragment == null) {
////                    this.dataTestFragment = new DataTestFragment();
////                    fragmentTransaction.add(R.id.frame, this.dataTestFragment, DATA_TEST_TAG);
////                } else {
////                    fragmentTransaction.show(this.dataTestFragment);
////                }
////                if (this.speedTestFragment != null) {
////                    fragmentTransaction.hide(this.speedTestFragment);
////                    break;
////                }
////                break;
////            case 4:
////                if (this.servingCellFragment != null) {
////                    fragmentTransaction.hide(this.servingCellFragment);
////                }
////                if (this.neighborCellFragment != null) {
////                    fragmentTransaction.hide(this.neighborCellFragment);
////                }
////                if (this.myMapFragment != null) {
////                    fragmentTransaction.hide(this.myMapFragment);
////                }
////                if (this.configFragment != null) {
////                    fragmentTransaction.hide(this.configFragment);
////                }
////                if (this.speedTestFragment == null) {
////                    this.speedTestFragment = new SpeedTestFragment();
////                    fragmentTransaction.add(R.id.frame, this.speedTestFragment, SPEEDTEST_TAG);
////                } else {
////                    fragmentTransaction.show(this.speedTestFragment);
////                }
////                if (this.dataTestFragment != null) {
////                    fragmentTransaction.hide(this.dataTestFragment);
////                    break;
////                }
////                break;
////            case 5:
////                if (this.servingCellFragment != null) {
////                    fragmentTransaction.hide(this.servingCellFragment);
////                }
////                if (this.neighborCellFragment != null) {
////                    fragmentTransaction.hide(this.neighborCellFragment);
////                }
////                if (this.dataTestFragment != null) {
////                    fragmentTransaction.hide(this.dataTestFragment);
////                }
////                if (this.speedTestFragment != null) {
////                    fragmentTransaction.hide(this.speedTestFragment);
////                }
////                if (this.myMapFragment != null) {
////                    fragmentTransaction.hide(this.myMapFragment);
////                }
////                if (this.configFragment != null) {
////                    fragmentTransaction.show(this.configFragment);
////                    break;
////                }
////                this.configFragment = new ConfigFragment();
////                fragmentTransaction.add(R.id.frame, this.configFragment, CONFIG_TAG);
////                break;
////        }
        fm.beginTransaction().replace(R.id.frame,servingCellFragment).commit();
     //   fragmentTransaction.commit();
    }
}
