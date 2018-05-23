package com.skynetsoftware.skynetdemo.network.networkmonitor.activity;

import android.app.Activity;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.skynetsoftware.skynetdemo.R;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.Cell;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.NetworkMode;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.ServingCell;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.ServingCellAdapter;

import java.text.DecimalFormat;


/* compiled from: ServingCellFragment */
class ServingCellFragmentAsyncTask extends AsyncTask<Void, Void, Void> {
    private static final DecimalFormat df1 = new DecimalFormat("0.#");
    private static final DecimalFormat df5 = new DecimalFormat("0.#####");
    private TextView accuracy;
    private Activity activity;
    private ServingCellAdapter adbSrvCell;
    private TextView altitude;
    private TextView azimuth;
    private TextView ci;
    private View container;
    private TextView data;
    private TextView dlHTTP;
    private TextView high;
    private TextView latitude;
    private ListView listView;
    private TextView location;
    private TextView longitude;
    long mStartRX = TrafficStats.getTotalRxBytes();
    long mStartTX = TrafficStats.getTotalTxBytes();
    long mStartTimeRX = System.currentTimeMillis();
    long mStartTimeTX = System.currentTimeMillis();
    private TextView mode;
    private TextView node;
    private TextView qual;
    private TextView qualTitle;
    private TextView servingTime;
    private TextView snr;
    private TextView speed;
    private TextView state;
    private boolean stop = false;
    private TextView tilt;
    private TextView txtCellName;
    private TextView txtLac;
    private TextView txtLacTitle;
    private TextView txtLevel;
    private TextView txtLevelTitle;
    private TextView txtMcc;
    private TextView txtMnc;
    private TextView txtNodeTitle;
    private TextView type;
    private TextView ulHTTP;

    public ServingCellFragmentAsyncTask(View container, Activity activity) {
        this.container = container;
        this.activity = activity;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.txtMcc = (TextView) this.container.findViewById(R.id.txtMCC);
        this.txtMnc = (TextView) this.container.findViewById(R.id.txtMNC);
        this.txtCellName = (TextView) this.container.findViewById(R.id.txtCellName);
        this.txtLacTitle = (TextView) this.container.findViewById(R.id.txtLacTitle);
        this.txtLac = (TextView) this.container.findViewById(R.id.txtLAC);
        this.txtNodeTitle = (TextView) this.container.findViewById(R.id.txtNodeTitle);
        this.node = (TextView) this.container.findViewById(R.id.txtNode);
        this.ci = (TextView) this.container.findViewById(R.id.txtCellID);
        this.type = (TextView) this.container.findViewById(R.id.txtType);
        this.txtLevelTitle = (TextView) this.container.findViewById(R.id.txtLevelTitle);
        this.txtLevel = (TextView) this.container.findViewById(R.id.txtLevel);
        this.qualTitle = (TextView) this.container.findViewById(R.id.txtQualTitle);
        this.qual = (TextView) this.container.findViewById(R.id.txtQual);
        this.snr = (TextView) this.container.findViewById(R.id.txtSNR);
        this.mode = (TextView) this.container.findViewById(R.id.txtNetworkMode);
        this.azimuth = (TextView) this.container.findViewById(R.id.txtAzimuth);
        this.tilt = (TextView) this.container.findViewById(R.id.txtTilt);
        this.high = (TextView) this.container.findViewById(R.id.txtHeight);
        this.altitude = (TextView) this.container.findViewById(R.id.txtAltitude);
        this.speed = (TextView) this.container.findViewById(R.id.txtSpeed);
        this.location = (TextView) this.container.findViewById(R.id.txtLocation);
        this.accuracy = (TextView) this.container.findViewById(R.id.txtNWAccuracy);
        this.longitude = (TextView) this.container.findViewById(R.id.txtLongitude);
        this.latitude = (TextView) this.container.findViewById(R.id.txtLatitudes);
        this.ulHTTP = (TextView) this.container.findViewById(R.id.txtULHttp);
        this.dlHTTP = (TextView) this.container.findViewById(R.id.txtDLHttp);
        this.data = (TextView) this.container.findViewById(R.id.txtData);
        this.state = (TextView) this.container.findViewById(R.id.txtState);
        this.servingTime = (TextView) this.container.findViewById(R.id.txtServingTime);
        this.adbSrvCell = new ServingCellAdapter(this.activity, 0, MainActivity.listServingCells);
        this.listView = (ListView) this.container.findViewById(R.id.servingCellHistory);
        this.listView.setAdapter(this.adbSrvCell);
    }

    protected Void doInBackground(Void... params) {
        while (!this.stop) {
            SystemClock.sleep(1000);
            getNetworkTrafficStatistic();
            publishProgress(new Void[0]);
        }
        return null;
    }

    private void getNetworkTrafficStatistic() {
        long curRxBytes = TrafficStats.getTotalRxBytes();
        long curTimeRX = System.currentTimeMillis();
        if (this.mStartRX == -1 || curRxBytes == -1) {
            MainActivity.measurement.setDl_bitrate("");
            MainActivity.measurement.setTestdlbitrate("");
        } else {
            long dlBitrate = (long) (((float) (8 * (curRxBytes - this.mStartRX))) / (1024.0f * (((float) (curTimeRX - this.mStartTimeRX)) / 1000.0f)));
            MainActivity.measurement.setDl_bitrate(String.valueOf(dlBitrate));
            MainActivity.measurement.setTestdlbitrate(String.valueOf(dlBitrate));
        }
        this.mStartRX = curRxBytes;
        this.mStartTimeRX = curTimeRX;
        long curTxBytes = TrafficStats.getTotalTxBytes();
        long curTimeTx = System.currentTimeMillis();
        if (this.mStartTX == -1 || curTxBytes == -1) {
            MainActivity.measurement.setUl_bitrate("");
            MainActivity.measurement.setTestulbitrate("");
        } else {
            long ulBitrate = (long) (((float) (8 * (curTxBytes - this.mStartTX))) / (1024.0f * (((float) (curTimeTx - this.mStartTimeTX)) / 1000.0f)));
            MainActivity.measurement.setUl_bitrate(String.valueOf(ulBitrate));
            MainActivity.measurement.setTestulbitrate(String.valueOf(ulBitrate));
        }
        this.mStartTX = curTxBytes;
        this.mStartTimeTX = curTimeTx;
    }

    public void stop() {
        this.stop = true;
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        String oldMode = "";
        String oldLac = "";
        String oldNode = "";
        String oldCi = "";
        String key = "";
        if (MainActivity.cells2G.size() > 0 || MainActivity.cells3G.size() > 0 || MainActivity.cells4G.size() > 0) {
            oldMode = this.mode.getText().toString();
            if (oldMode.equals("2G") || oldMode.equals("3G")) {
                oldLac = this.txtLac.getText().toString();
                key = oldMode + "_" + oldLac + "_" + this.ci.getText().toString();
            } else if (oldMode.equals("4G")) {
                oldNode = this.node.getText().toString();
                key = oldMode + "_" + oldNode + "_" + this.ci.getText().toString();
            }
        }
        this.txtMcc.setText(MainActivity.measurement.getMcc());
        this.txtMnc.setText(MainActivity.measurement.getMnc());
        String lac = MainActivity.measurement.getLac();
        this.txtLac.setText(lac);
        String modeValue = MainActivity.measurement.getNetwork_mode();
        String nodeValue = MainActivity.measurement.getNode();
        if (modeValue.equals(NetworkMode.MODE_2G.toString())) {
            this.node.setText(nodeValue);
        } else if (modeValue.equals(NetworkMode.MODE_3G.toString())) {
            this.node.setText(nodeValue);
        } else if (modeValue.equals(NetworkMode.MODE_4G.toString())) {
            this.node.setText(nodeValue);
        }
        String ciValue = MainActivity.measurement.getCellid();
        this.ci.setText(ciValue);
        if (MainActivity.cells2G.size() > 0 || MainActivity.cells3G.size() > 0 || MainActivity.cells4G.size() > 0) {
            Cell cell = null;
            if (modeValue.equals("2G") || (modeValue.equals("3G") && !key.equals(modeValue + "_" + lac + "_" + ciValue))) {
                cell = (Cell) MainActivity.cells2G.get(modeValue + "_" + lac + "_" + ciValue);
                if (modeValue.trim().equals("3G")) {
                    cell = (Cell) MainActivity.cells3G.get(modeValue + "_" + lac + "_" + ciValue);
                }
            } else if (modeValue.equals("4G") && !key.equals(modeValue + "_" + nodeValue + "_" + ciValue)) {
                cell = (Cell) MainActivity.cells4G.get(modeValue + "_" + nodeValue + "_" + ciValue);
            }
            if (cell != null) {
                this.txtCellName.setText(cell.getCellName());
                if (cell.getAzimuth() == null || cell.getAzimuth().trim().equals("")) {
                    this.azimuth.setText("-");
                } else {
                    this.azimuth.setText(cell.getAzimuth());
                }
                if (cell.getTilt() == null || cell.getTilt().trim().equals("")) {
                    this.tilt.setText("-");
                } else {
                    this.tilt.setText(cell.getTilt());
                }
            }
        }
        if (modeValue.trim().equals("3G")) {
            this.txtLacTitle.setText("LAC: ");
            this.txtNodeTitle.setText("Node: ");
            this.txtLevelTitle.setText("RSCP: ");
            this.qualTitle.setText("EcNo: ");
        } else if (modeValue.trim().equals("2G")) {
            this.txtLacTitle.setText("LAC: ");
            this.txtNodeTitle.setText("Node: ");
            this.txtLevelTitle.setText("Level: ");
            this.qualTitle.setText("Qual: ");
        } else if (modeValue.trim().equals("4G")) {
            this.txtLacTitle.setText("TAC: ");
            this.txtNodeTitle.setText("ENodeBID: ");
            this.txtLevelTitle.setText("RSRP: ");
            this.qualTitle.setText("RSRQ: ");
        } else {
            this.txtLevelTitle.setText("RSCP: ");
            this.qualTitle.setText("EcNo: ");
        }
        this.type.setText(MainActivity.measurement.getNetwork_type());
        this.txtLevel.setText(MainActivity.measurement.getNw_level());
        this.qual.setText(MainActivity.measurement.getQual());
        this.snr.setText(MainActivity.measurement.getSnr());
        this.mode.setText(modeValue);
        String altitudeValue = MainActivity.measurement.getAltitude();
        try {
            this.altitude.setText(df1.format(Double.parseDouble(altitudeValue)));
        } catch (Exception e) {
            this.altitude.setText(altitudeValue);
        }
        String lngValue = MainActivity.measurement.getLongitude();
        try {
            this.longitude.setText(df5.format(Double.parseDouble(lngValue)));
        } catch (Exception e2) {
            this.longitude.setText(lngValue);
        }
        String latValue = MainActivity.measurement.getLatitude();
        try {
            this.latitude.setText(df5.format(Double.parseDouble(latValue)));
        } catch (Exception e3) {
            this.latitude.setText(latValue);
        }
        String speedValue = MainActivity.measurement.getSpeed();
        try {
            this.speed.setText(df1.format(Double.parseDouble(speedValue)) + " km/h");
        } catch (Exception e4) {
            this.speed.setText(speedValue);
        }
        String accuracyValue = MainActivity.measurement.getAccuracy();
        try {
            this.accuracy.setText(df1.format(Double.parseDouble(accuracyValue)) + " m");
        } catch (Exception e5) {
            this.accuracy.setText(accuracyValue);
        }
        this.location.setText(MainActivity.measurement.getLocationsource());
        String ulValue = MainActivity.measurement.getTestulbitrate();
        try {
            this.ulHTTP.setText(((int) Float.parseFloat(ulValue)) + " kbps");
        } catch (Exception e6) {
            this.ulHTTP.setText(ulValue);
        }
        String dlValue = MainActivity.measurement.getTestdlbitrate();
        try {
            this.dlHTTP.setText(((int) Float.parseFloat(dlValue)) + " kbps");
        } catch (Exception e7) {
            this.dlHTTP.setText(dlValue);
        }
        try {
            this.state.setText(MainActivity.measurement.getState());
        } catch (Exception e8) {
        }
        this.data.setText(MainActivity.measurement.getDataconn_info());
        if (MainActivity.listServingCells != null && MainActivity.listServingCells.size() >= 1) {
            ServingCell currentServingCell = (ServingCell) MainActivity.listServingCells.get(MainActivity.listServingCells.size() - 1);
            currentServingCell.setServingTime((int) ((System.currentTimeMillis() - currentServingCell.getTime().getTime()) / 1000));
            this.adbSrvCell.notifyDataSetChanged();
        }
    }
}
