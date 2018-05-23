package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

import android.graphics.Color;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;

public class Cell implements Serializable {
    public static final String CELL_2G_COLOR = "#0000FF";
    public static final String CELL_3G_COLOR = "#FF0000";
    public static final String CELL_4G_COLOR = "#00FF00";
    public static final double MAP_20_METERS = 1.9E-4d;
    private String azimuth;
    private String cellName;
    private String cellid;
    private CircleOptions circleOptions;
    private String lac;
    private LatLng latLng;
    private String latitude;
    private String longitude;
    private String node;
    private PolylineOptions polylineOptions;
    private String tech;
    private String tilt;

    public Cell(String cellName, String latitude, String longitude, String lac, String cellid, String azimuth, String tech, String node, String tilt) {
        this.cellName = cellName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lac = lac;
        this.cellid = cellid;
        this.azimuth = azimuth;
        this.tech = tech;
        this.node = node;
        this.tilt = tilt;
        if (this.cellName.toUpperCase().contains("IBS")) {
            this.azimuth = "360";
        }
        this.latLng = new LatLng(Double.valueOf(getLatitude().trim()).doubleValue(), Double.valueOf(getLongitude().trim()).doubleValue());
        if (getAzimuth() != null && !getAzimuth().trim().equals("") && getAzimuth().equals("360")) {
            setLatLong(this.latLng);
            createCircleOptions();
        } else if (getAzimuth() != null && !getAzimuth().equals("null") && !getAzimuth().trim().equals("")) {
            createPolylineOptions();
        }
    }

    public String getCellName() {
        return this.cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLac() {
        return this.lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getCellid() {
        return this.cellid;
    }

    public void setCellid(String cellid) {
        this.cellid = cellid;
    }

    public String getAzimuth() {
        return this.azimuth;
    }

    public void setAzimuth(String azimuth) {
        this.azimuth = azimuth;
    }

    public String getTech() {
        return this.tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getTilt() {
        return this.tilt;
    }

    public void setTilt(String tilt) {
        this.tilt = tilt;
    }

    public CircleOptions getCircleOptions() {
        return this.circleOptions;
    }

    public void setCircleOptions(CircleOptions circleOptions) {
        this.circleOptions = circleOptions;
    }

    public PolylineOptions getPolylineOptions() {
        return this.polylineOptions;
    }

    public void setPolylineOptions(PolylineOptions polylineOptions) {
        this.polylineOptions = polylineOptions;
    }

    public CircleOptions createCircleOptions() {
        String color = "#FF0000";
        if (this.tech.equals("2G")) {
            color = "#0000FF";
        } else if (this.tech.equals("4G")) {
            color = "#00FF00";
        }
        this.circleOptions = new CircleOptions().center(getLatLong()).radius(30.0d).fillColor(0).strokeColor(Color.parseColor(color)).strokeWidth(4.0f);
        return this.circleOptions;
    }

    public PolylineOptions createPolylineOptions() {
        String color = "#FF0000";
        if (this.tech.equals("2G")) {
            color = "#0000FF";
        } else if (this.tech.equals("4G")) {
            color = "#00FF00";
        }
        this.polylineOptions = new PolylineOptions().add(getLatLong()).add(getSecondPoint(getLatLong(), Double.parseDouble(getAzimuth()), 5.7E-4d)).color(Color.parseColor(color)).width(4.0f);
        return this.polylineOptions;
    }

    public LatLng getLatLong() {
        if (this.latLng == null) {
            this.latLng = new LatLng(Double.valueOf(getLatitude().trim()).doubleValue(), Double.valueOf(getLongitude().trim()).doubleValue());
        }
        return this.latLng;
    }

    public void setLatLong(LatLng latLng) {
        this.latLng = latLng;
    }

    private LatLng getSecondPoint(LatLng first, double degress, double distance) {
        double radian = Math.toRadians(degress);
        return new LatLng(first.latitude + (Math.cos(radian) * distance), first.longitude + (Math.sin(radian) * distance));
    }
}
