package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

import java.io.Serializable;
import java.util.Date;

public class ServingCell implements Serializable {
    private String cellId;
    private String lac;
    private String level;
    private String mcc;
    private String mnc;
    private String mode;
    private String node;
    private String operatorName;
    private String qual;
    private int servingTime;
    private String state;
    private Date time;
    private String type;

    public String getOperatorName() {
        return this.operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMcc() {
        return this.mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return this.mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getLac() {
        return this.lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getCellId() {
        return this.cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getQual() {
        return this.qual;
    }

    public void setQual(String qual) {
        this.qual = qual;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getServingTime() {
        return this.servingTime;
    }

    public void setServingTime(int servingTime) {
        this.servingTime = servingTime;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ServingCell(String operatorName, Date time, String mcc, String mnc, String lac, String node, String cellId, String level, String qual, String mode, String type, int servingTime, String state) {
        this.operatorName = operatorName;
        this.time = time;
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.node = node;
        this.cellId = cellId;
        this.level = level;
        this.qual = qual;
        this.mode = mode;
        this.type = type;
        this.servingTime = servingTime;
        this.state = state;
    }
}
