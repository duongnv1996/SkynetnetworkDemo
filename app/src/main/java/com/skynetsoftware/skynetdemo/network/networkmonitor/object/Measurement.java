package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

public class Measurement implements Cloneable {
    String accuracy = "";
    String altitude = "";
    String androidCellType = "";
    String avgping = "";
    int bmk = 0;
    String callDuration;
    int cellTechType = 0;
    String cellid = "-";
    String conninfo = "";
    String conntype = "";
    String cqi = "-";
    String dataconn_info = "";
    Integer dataconn_type;
    String dl_bitrate = "-";
    String event = "";
    String imei = "";
    String imsi = "";
    String lac = "-";
    String latitude = "";
    String locationsource = "";
    String longitude = "";
    String lterssi = "-";
    String maxping = "";
    String mcc = "-";
    String minping = "";
    String mnc = "-";
    String msisdn = "";
    String ncellid1 = "";
    String ncellid2 = "";
    String ncellid3 = "";
    String ncellid4 = "";
    String ncellid5 = "";
    String ncellid6 = "";
    String network_mode = "-";
    String network_type = "-";
    String nlac1 = "";
    String nlac2 = "";
    String nlac3 = "";
    String nlac4 = "";
    String nlac5 = "";
    String nlac6 = "";
    String node = "-";
    String nrxlev1 = "";
    String nrxlev2 = "";
    String nrxlev3 = "";
    String nrxlev4 = "";
    String nrxlev5 = "";
    String nrxlev6 = "";
    String nw_level = "-";
    String operatorname = "-";
    String pingloss = "";
    String psc = "-";
    String qual = "-";
    String snr = "-";
    String speed = "-";
    String state = "";
    String stdevping = "";
    String testDbm = "-";
    String testdlbitrate = "";
    String testulbitrate = "";
    String timestamp = "";
    String ul_bitrate = "-";
    public Measurement(){};
    public Measurement(String timestamp, String longitude, String latitude, String nw_level, String speed, String operatorname, String mcc, String mnc, String node, String cellid, String lac, String network_type, String network_mode, String qual, String snr, String cqi, String lterssi, String psc, String dl_bitrate, String ul_bitrate, String nlac1, String ncellid1, String nrxlev1, String nlac2, String ncellid2, String nrxlev2, String nlac3, String ncellid3, String nrxlev3, String nlac4, String ncellid4, String nrxlev4, String nlac5, String ncellid5, String nrxlev5, String nlac6, String ncellid6, String nrxlev6, String state, String event, String accuracy, String locationsource, String altitude, String conntype, String conninfo, String avgping, String minping, String maxping, String stdevping, String pingloss, String testdlbitrate, String testulbitrate, Integer dataconn_type, String dataconn_info, String msisdn, String imei, String imsi, String androidCellType, int bmk) {
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
        this.nw_level = nw_level;
        this.speed = speed;
        this.operatorname = operatorname;
        this.mcc = mcc;
        this.mnc = mnc;
        this.node = node;
        this.cellid = cellid;
        this.lac = lac;
        this.network_type = network_type;
        this.network_mode = network_mode;
        this.qual = qual;
        this.snr = snr;
        this.cqi = cqi;
        this.lterssi = lterssi;
        this.psc = psc;
        this.dl_bitrate = dl_bitrate;
        this.ul_bitrate = ul_bitrate;
        this.nlac1 = nlac1;
        this.ncellid1 = ncellid1;
        this.nrxlev1 = nrxlev1;
        this.nlac2 = nlac2;
        this.ncellid2 = ncellid2;
        this.nrxlev2 = nrxlev2;
        this.nlac3 = nlac3;
        this.ncellid3 = ncellid3;
        this.nrxlev3 = nrxlev3;
        this.nlac4 = nlac4;
        this.ncellid4 = ncellid4;
        this.nrxlev4 = nrxlev4;
        this.nlac5 = nlac5;
        this.ncellid5 = ncellid5;
        this.nrxlev5 = nrxlev5;
        this.nlac6 = nlac6;
        this.ncellid6 = ncellid6;
        this.nrxlev6 = nrxlev6;
        this.state = state;
        this.event = event;
        this.accuracy = accuracy;
        this.locationsource = locationsource;
        this.altitude = altitude;
        this.conntype = conntype;
        this.conninfo = conninfo;
        this.avgping = avgping;
        this.minping = minping;
        this.maxping = maxping;
        this.stdevping = stdevping;
        this.pingloss = pingloss;
        this.testdlbitrate = testdlbitrate;
        this.testulbitrate = testulbitrate;
        this.dataconn_type = dataconn_type;
        this.dataconn_info = dataconn_info;
        this.msisdn = msisdn;
        this.imei = imei;
        this.imsi = imsi;
        this.androidCellType = androidCellType;
        this.bmk = bmk;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getNw_level() {
        return this.nw_level;
    }

    public void setNw_level(String nw_level) {
        this.nw_level = nw_level;
    }

    public String getSpeed() {
        return this.speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getOperatorname() {
        return this.operatorname;
    }

    public void setOperatorname(String operatorname) {
        this.operatorname = operatorname;
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

    public String getNode() {
        return this.node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getCellid() {
        return this.cellid;
    }

    public void setCellid(String cellid) {
        this.cellid = cellid;
    }

    public String getLac() {
        return this.lac;
    }

    public void setLac(String lac) {
        this.lac = lac;
    }

    public String getNetwork_type() {
        return this.network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public String getQual() {
        return this.qual;
    }

    public void setQual(String qual) {
        this.qual = qual;
    }

    public String getSnr() {
        return this.snr;
    }

    public void setSnr(String snr) {
        this.snr = snr;
    }

    public String getCqi() {
        return this.cqi;
    }

    public void setCqi(String cqi) {
        this.cqi = cqi;
    }

    public String getLterssi() {
        return this.lterssi;
    }

    public void setLterssi(String lterssi) {
        this.lterssi = lterssi;
    }

    public String getPsc() {
        return this.psc;
    }

    public void setPsc(String psc) {
        this.psc = psc;
    }

    public String getDl_bitrate() {
        return this.dl_bitrate;
    }

    public void setDl_bitrate(String dl_bitrate) {
        this.dl_bitrate = dl_bitrate;
    }

    public String getUl_bitrate() {
        return this.ul_bitrate;
    }

    public void setUl_bitrate(String ul_bitrate) {
        this.ul_bitrate = ul_bitrate;
    }

    public String getNlac1() {
        return this.nlac1;
    }

    public void setNlac1(String nlac1) {
        this.nlac1 = nlac1;
    }

    public String getNcellid1() {
        return this.ncellid1;
    }

    public void setNcellid1(String ncellid1) {
        this.ncellid1 = ncellid1;
    }

    public String getNrxlev1() {
        return this.nrxlev1;
    }

    public void setNrxlev1(String nrxlev1) {
        this.nrxlev1 = nrxlev1;
    }

    public String getNlac2() {
        return this.nlac2;
    }

    public void setNlac2(String nlac2) {
        this.nlac2 = nlac2;
    }

    public String getNcellid2() {
        return this.ncellid2;
    }

    public void setNcellid2(String ncellid2) {
        this.ncellid2 = ncellid2;
    }

    public String getNrxlev2() {
        return this.nrxlev2;
    }

    public void setNrxlev2(String nrxlev2) {
        this.nrxlev2 = nrxlev2;
    }

    public String getNlac3() {
        return this.nlac3;
    }

    public void setNlac3(String nlac3) {
        this.nlac3 = nlac3;
    }

    public String getNcellid3() {
        return this.ncellid3;
    }

    public void setNcellid3(String ncellid3) {
        this.ncellid3 = ncellid3;
    }

    public String getNrxlev3() {
        return this.nrxlev3;
    }

    public void setNrxlev3(String nrxlev3) {
        this.nrxlev3 = nrxlev3;
    }

    public String getNlac4() {
        return this.nlac4;
    }

    public void setNlac4(String nlac4) {
        this.nlac4 = nlac4;
    }

    public String getNcellid4() {
        return this.ncellid4;
    }

    public void setNcellid4(String ncellid4) {
        this.ncellid4 = ncellid4;
    }

    public String getNrxlev4() {
        return this.nrxlev4;
    }

    public void setNrxlev4(String nrxlev4) {
        this.nrxlev4 = nrxlev4;
    }

    public String getNlac5() {
        return this.nlac5;
    }

    public void setNlac5(String nlac5) {
        this.nlac5 = nlac5;
    }

    public String getNcellid5() {
        return this.ncellid5;
    }

    public void setNcellid5(String ncellid5) {
        this.ncellid5 = ncellid5;
    }

    public String getNrxlev5() {
        return this.nrxlev5;
    }

    public void setNrxlev5(String nrxlev5) {
        this.nrxlev5 = nrxlev5;
    }

    public String getNlac6() {
        return this.nlac6;
    }

    public void setNlac6(String nlac6) {
        this.nlac6 = nlac6;
    }

    public String getNcellid6() {
        return this.ncellid6;
    }

    public void setNcellid6(String ncellid6) {
        this.ncellid6 = ncellid6;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNrxlev6() {
        return this.nrxlev6;
    }

    public void setNrxlev6(String nrxlev6) {
        this.nrxlev6 = nrxlev6;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getLocationsource() {
        return this.locationsource;
    }

    public void setLocationsource(String locationsource) {
        this.locationsource = locationsource;
    }

    public String getAltitude() {
        return this.altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getConntype() {
        return this.conntype;
    }

    public void setConntype(String conntype) {
        this.conntype = conntype;
    }

    public String getConninfo() {
        return this.conninfo;
    }

    public void setConninfo(String conninfo) {
        this.conninfo = conninfo;
    }

    public String getAvgping() {
        return this.avgping;
    }

    public void setAvgping(String avgping) {
        this.avgping = avgping;
    }

    public String getMinping() {
        return this.minping;
    }

    public void setMinping(String minping) {
        this.minping = minping;
    }

    public String getMaxping() {
        return this.maxping;
    }

    public void setMaxping(String maxping) {
        this.maxping = maxping;
    }

    public String getStdevping() {
        return this.stdevping;
    }

    public void setStdevping(String stdevping) {
        this.stdevping = stdevping;
    }

    public String getPingloss() {
        return this.pingloss;
    }

    public void setPingloss(String pingloss) {
        this.pingloss = pingloss;
    }

    public String getTestdlbitrate() {
        return this.testdlbitrate;
    }

    public void setTestdlbitrate(String testdlbitrate) {
        this.testdlbitrate = testdlbitrate;
    }

    public String getTestulbitrate() {
        return this.testulbitrate;
    }

    public void setTestulbitrate(String testulbitrate) {
        this.testulbitrate = testulbitrate;
    }

    public String getMsisdn() {
        return this.msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return this.imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getNetwork_mode() {
        return this.network_mode;
    }

    public void setNetwork_mode(String network_mode) {
        this.network_mode = network_mode;
    }

    public Integer getDataconn_type() {
        return this.dataconn_type;
    }

    public void setDataconn_type(Integer dataconn_type) {
        this.dataconn_type = dataconn_type;
    }

    public String getDataconn_info() {
        return this.dataconn_info;
    }

    public void setDataconn_info(String dataconn_info) {
        this.dataconn_info = dataconn_info;
    }

    public String getAndroidCellType() {
        return this.androidCellType;
    }

    public void setAndroidCellType(String androidCellType) {
        this.androidCellType = androidCellType;
    }

    public int getCellTechType() {
        return this.cellTechType;
    }

    public void setCellTechType(int cellTechType) {
        this.cellTechType = cellTechType;
    }

    public String getTestDbm() {
        return this.testDbm;
    }

    public void setTestDbm(String testDbm) {
        this.testDbm = testDbm;
    }

    public void setBmk(int bmk) {
        this.bmk = bmk;
    }

    public int getBmk() {
        return this.bmk;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallDuration() {
        return this.callDuration;
    }

    public Measurement clone() throws CloneNotSupportedException {
        return (Measurement) super.clone();
    }
}
