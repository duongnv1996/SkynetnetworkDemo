package com.skynetsoftware.skynetdemo.network.networkmonitor.listener;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.LocationSource;
import com.skynetsoftware.skynetdemo.network.networkmonitor.receiver.ServingCellCapture;

import java.util.Timer;
import java.util.TimerTask;


public class MyLocationListener {
    public static final String BUNDLE = "MyLocationListener.BUNDLE";
    public static final String CHANGE_TYPE = "MyLocationListener.CHANGE_TYPE";
    public static final String LOCATION_CHANGED = "MyLocationListener.LOCATION_CHANGED";
    public static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0;
    public static final long MINIMUM_TIME_BETWEEN_UPDATES = 0;
    private static String accuracy = "";
    private static String altitude = "";
    private static String latitude = "";
    private static String locationSource = "";
    private static String longitude = "";
    private static String speed = "";
    private Activity activity;
    LocationListener gpsLLs = null;
    boolean gps_enabled = false;
    long lastTimeGPS;
    long lastTimeNW;
    LocationManager lm;
    LocationResult locationResult;
    LocationListener networkLLs = null;
    boolean network_enabled = false;
    Timer timer1;

    class C04331 implements LocationListener {
        C04331() {
        }

        public void onLocationChanged(Location location) {
        }

        public void onProviderDisabled(String provider) {
            MyLocationListener.this.gps_enabled = false;
        }

        public void onProviderEnabled(String provider) {
            MyLocationListener.this.gps_enabled = true;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    class C04342 implements LocationListener {
        C04342() {
        }

        public void onLocationChanged(Location location) {
        }

        public void onProviderDisabled(String provider) {
            MyLocationListener.this.network_enabled = false;
        }

        public void onProviderEnabled(String provider) {
            MyLocationListener.this.network_enabled = true;
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    class GetLastLocation extends TimerTask {
        GetLastLocation() {
        }

        public void run() {
            if (ContextCompat.checkSelfPermission(MyLocationListener.this.activity, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(MyLocationListener.this.activity, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
                Location net_loc = null;
                Location gps_loc = null;
                if (MyLocationListener.this.gps_enabled) {
                    gps_loc = MyLocationListener.this.lm.getLastKnownLocation("gps");
                }
                if (MyLocationListener.this.network_enabled) {
                    net_loc = MyLocationListener.this.lm.getLastKnownLocation("network");
                }
                if (gps_loc != null && net_loc != null) {
                    long e1 = gps_loc.getTime() - MyLocationListener.this.lastTimeGPS;
                    long e2 = net_loc.getTime() - MyLocationListener.this.lastTimeNW;
                    if (e1 != 0 || e2 != 0) {
                        if (e1 == 0 && e2 != 0) {
                            MyLocationListener.this.locationResult.gotLocation(net_loc);
                            MyLocationListener.this.lastTimeNW = net_loc.getTime();
                        } else if (e1 == 0 || e2 != 0) {
                            if (e1 < e2) {
                                MyLocationListener.this.locationResult.gotLocation(gps_loc);
                            } else {
                                MyLocationListener.this.locationResult.gotLocation(net_loc);
                            }
                            MyLocationListener.this.lastTimeGPS = gps_loc.getTime();
                            MyLocationListener.this.lastTimeNW = net_loc.getTime();
                        } else {
                            MyLocationListener.this.locationResult.gotLocation(gps_loc);
                            MyLocationListener.this.lastTimeGPS = gps_loc.getTime();
                        }
                    }
                } else if (gps_loc != null) {
                    MyLocationListener.this.locationResult.gotLocation(gps_loc);
                } else if (net_loc != null) {
                    MyLocationListener.this.locationResult.gotLocation(net_loc);
                } else {
                    MyLocationListener.this.locationResult.gotLocation(null);
                }
            }
        }
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

    class C06403 extends LocationResult {
        C06403() {
        }

        public void gotLocation(Location location) {
            if (location != null) {
                MyLocationListener.longitude = String.valueOf(location.getLongitude());
                MyLocationListener.latitude = String.valueOf(location.getLatitude());
                if (location.hasSpeed()) {
                    MyLocationListener.speed = String.valueOf(Math.round(location.getSpeed()));
                } else {
                    MyLocationListener.speed = "-";
                }
                if (location.hasAccuracy()) {
                    MyLocationListener.accuracy = String.valueOf(location.getAccuracy());
                } else {
                    MyLocationListener.accuracy = "";
                }
                if (location.hasAltitude()) {
                    MyLocationListener.altitude = String.valueOf(location.getAltitude());
                } else {
                    MyLocationListener.altitude = "-";
                }
                if (location.getProvider().equals("gps")) {
                    MyLocationListener.locationSource = LocationSource.GPS;
                } else {
                    MyLocationListener.locationSource = LocationSource.NETWORK;
                }
                MyLocationListener.this.sendInfoToServingCellBroadCastReceiver();
            }
        }
    }

    public MyLocationListener(Activity activity, LocationManager locationManager) {
        this.activity = activity;
        this.lm = locationManager;
        this.gpsLLs = new C04331();
        this.networkLLs = new C04342();
        try {
            this.gps_enabled = this.lm.isProviderEnabled("gps");
        } catch (Exception e) {
            this.gps_enabled = false;
        }
        try {
            this.network_enabled = this.lm.isProviderEnabled("network");
        } catch (Exception e2) {
            this.network_enabled = false;
        }
        if (ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(activity, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            if (this.gps_enabled) {
                this.lm.requestLocationUpdates("gps", 0, 0.0f, this.gpsLLs);
            }
            if (this.network_enabled) {
                this.lm.requestLocationUpdates("network", 0, 0.0f, this.networkLLs);
            }
            Location l1 = this.lm.getLastKnownLocation("gps");
            Location l2 = this.lm.getLastKnownLocation("network");
            if (l1 != null) {
                this.lastTimeGPS = l1.getTime();
            }
            if (l2 != null) {
                this.lastTimeNW = l2.getTime();
            }
            this.locationResult = new C06403();
        }
    }

    public void requestUpdateListener() {
        if (this.timer1 == null) {
            this.timer1 = new Timer();
            this.timer1.schedule(new GetLastLocation(), 1000, 1000);
        }
    }

    public void removeUpdateListener() {
        if (ContextCompat.checkSelfPermission(this.activity, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this.activity, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            if (this.timer1 != null) {
                this.timer1.cancel();
            }
            this.lm.removeUpdates(this.gpsLLs);
            this.lm.removeUpdates(this.networkLLs);
        }
    }

    private void sendInfoToServingCellBroadCastReceiver() {
        Intent intent = new Intent();
        intent.setAction(ServingCellCapture.RECEIVER);
        if (intent != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CHANGE_TYPE, LOCATION_CHANGED);
            bundle.putString(Utils.PREFS_LONGITUDE, longitude);
            bundle.putString(Utils.PREFS_LATITUDE, latitude);
            bundle.putString("accuracy", accuracy);
            bundle.putString("speed", speed);
            bundle.putString("altitude", altitude);
            bundle.putString("location", locationSource);
            intent.putExtra(BUNDLE, bundle);
            this.activity.sendBroadcast(intent);
        }
    }
}
