package com.skynetsoftware.skynetdemo.network.networkmonitor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;

import com.skynetsoftware.skynetdemo.R;
import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;


public class DataSequenceReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 2;
    public static boolean START_DATA_SEQ_FLAG = false;
    static boolean isDLRunning = false;
    private final String LOG_TAG = "DATASEQUENCE_LOG";
    private Context context;
    SpeedTestSocket downloadSocket;
    SpeedTestSocket uploadSocket;

    private class RunDataTest extends AsyncTask<Void, Void, Void> {
        private RunDataTest() {
        }

        protected Void doInBackground(Void... params) {
            DataSequenceReceiver.this.ping(Utils.getFromPrefs(DataSequenceReceiver.this.context, DataSequenceReceiver.this.context.getString(R.string.key_dataSequence_pingURL), "google.com"));
            DataSequenceReceiver.this.startDownload();
            return null;
        }
    }

    class C06411 implements ISpeedTestListener {
        C06411() {
        }

        public void onDownloadFinished(SpeedTestReport report) {
            DataSequenceReceiver.this.startUpload();
        }

        public void onDownloadError(SpeedTestError speedTestError, String errorMessage) {
            DataSequenceReceiver.this.startUpload();
        }

        public void onUploadFinished(SpeedTestReport report) {
        }

        public void onUploadError(SpeedTestError speedTestError, String errorMessage) {
        }

        public void onDownloadProgress(float percent, SpeedTestReport report) {
          //  Utils.addNewMeasurement(DataSequenceReceiver.this.context, DataEvent.DOWNLOADING, Float.valueOf(Float.parseFloat(report.getTransferRateBit().divide(BigDecimal.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)).toString())), null);
        }

        public void onUploadProgress(float percent, SpeedTestReport report) {
        }

        public void onInterruption() {
            DataSequenceReceiver.this.startUpload();
        }

        @Override
        public void onCompletion(SpeedTestReport report) {

        }

        @Override
        public void onProgress(float percent, SpeedTestReport report) {

        }

        @Override
        public void onError(SpeedTestError speedTestError, String errorMessage) {

        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onReceive(Context r9, android.content.Intent r10) {
        /*
        r8 = this;
        r7 = 2131165261; // 0x7f07004d float:1.7944734E38 double:1.052935541E-314;
        r6 = 2;
        r1 = START_DATA_SEQ_FLAG;
        if (r1 == 0) goto L_0x0037;
    L_0x0008:
        r8.context = r9;
        if (r10 == 0) goto L_0x0037;
    L_0x000c:
        r1 = isDLRunning;	 Catch:{ Exception -> 0x0038 }
        if (r1 != 0) goto L_0x0021;
    L_0x0010:
        r1 = 1;
        isDLRunning = r1;	 Catch:{ Exception -> 0x0038 }
        r1 = new vn.vnpt.networkmonitor.receiver.DataSequenceReceiver$RunDataTest;	 Catch:{ Exception -> 0x0038 }
        r4 = 0;
        r1.<init>();	 Catch:{ Exception -> 0x0038 }
        r4 = android.os.AsyncTask.THREAD_POOL_EXECUTOR;	 Catch:{ Exception -> 0x0038 }
        r5 = 0;
        r5 = new java.lang.Void[r5];	 Catch:{ Exception -> 0x0038 }
        r1.executeOnExecutor(r4, r5);	 Catch:{ Exception -> 0x0038 }
    L_0x0021:
        r1 = r9.getString(r7);
        r4 = "10";
        r1 = vn.vnpt.networkmonitor.Utils.getFromPrefs(r9, r1, r4);
        r1 = java.lang.Integer.parseInt(r1);
        r1 = r1 * 1000;
        r2 = (long) r1;
        r1 = vn.vnpt.networkmonitor.receiver.DataSequenceReceiver.class;
        vn.vnpt.networkmonitor.Utils.scheduleAlarm(r9, r1, r6, r2);
    L_0x0037:
        return;
    L_0x0038:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0053 }
        r1 = r9.getString(r7);
        r4 = "10";
        r1 = vn.vnpt.networkmonitor.Utils.getFromPrefs(r9, r1, r4);
        r1 = java.lang.Integer.parseInt(r1);
        r1 = r1 * 1000;
        r2 = (long) r1;
        r1 = vn.vnpt.networkmonitor.receiver.DataSequenceReceiver.class;
        vn.vnpt.networkmonitor.Utils.scheduleAlarm(r9, r1, r6, r2);
        goto L_0x0037;
    L_0x0053:
        r1 = move-exception;
        r4 = r9.getString(r7);
        r5 = "10";
        r4 = vn.vnpt.networkmonitor.Utils.getFromPrefs(r9, r4, r5);
        r4 = java.lang.Integer.parseInt(r4);
        r4 = r4 * 1000;
        r2 = (long) r4;
        r4 = vn.vnpt.networkmonitor.receiver.DataSequenceReceiver.class;
        vn.vnpt.networkmonitor.Utils.scheduleAlarm(r9, r4, r6, r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: vn.vnpt.networkmonitor.receiver.DataSequenceReceiver.onReceive(android.content.Context, android.content.Intent):void");
    }

    private void startDownload() {
        if (this.downloadSocket == null) {
            this.downloadSocket = new SpeedTestSocket();
            this.downloadSocket.addSpeedTestListener(new C06411());
        }
        String downloadFileUrl = Utils.getFromPrefs(this.context, this.context.getString(R.string.key_dataSequence_downloadURL), "http://speedtest.ftp.otenet.gr/files/test100Mb.db");
      //  this.downloadSocket.startFixedDownload("speedtest.ftp.otenet.gr", 80, "/files/test100Mb.db", 60000, 3000);
    }

    private void startUpload() {
//        if (this.uploadSocket == null) {
//            this.uploadSocket = new SpeedTestSocket();
//            final String id = this.downloadSocket.toString();
//            this.uploadSocket.addSpeedTestListener(new ISpeedTestListener() {
//                public void onDownloadFinished(SpeedTestReport report) {
//                }
//
//                public void onDownloadError(SpeedTestError speedTestError, String errorMessage) {
//                }
//
//                public void onUploadFinished(SpeedTestReport report) {
//                    Log.v(id, "[UL Interrupion]");
//                    DataSequenceReceiver.isDLRunning = false;
//                }
//
//                public void onUploadError(SpeedTestError speedTestError, String errorMessage) {
//                    Log.v(id, "[UL Interrupion]");
//                    DataSequenceReceiver.isDLRunning = false;
//                }
//
//                public void onDownloadProgress(float percent, SpeedTestReport report) {
//                }
//
//                public void onUploadProgress(float percent, SpeedTestReport report) {
//                    Float ulthroughput = Float.valueOf(Float.parseFloat(report.getTransferRateBit().divide(BigDecimal.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)).toString()));
//                    Log.d("GGGGGGGGGGGG-UL", String.valueOf(ulthroughput));
//                    Utils.addNewMeasurement(DataSequenceReceiver.this.context, DataEvent.UPLOADING, ulthroughput, null);
//                }
//
//                public void onInterruption() {
//                    Log.v(id, "[UL Interrupion]");
//                    DataSequenceReceiver.isDLRunning = false;
//                }
//            });
//        }
//        this.uploadSocket.startFixedUpload("csm.vnpt.vn", 80, "/testUL.php", 150000000, 60000, 3000);
    }

    public void ping(String host) {
//        if (Utils.checkConnectionToServer(host, 80)) {
//            Utils.addNewMeasurement(this.context, DataEvent.CONNCECT_SUCESS, null, null);
//        } else {
//            Utils.addNewMeasurement(this.context, DataEvent.CONNECT_FAIL, null, null);
//        }
//        StringBuffer echo = new StringBuffer();
//        Process process = null;
//        try {
//            process = Runtime.getRuntime().exec("ping -c 4 " + host);
//            process.waitFor();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (process.exitValue() == 0) {
//            try {
//                InputStreamReader reader = new InputStreamReader(process.getInputStream());
//                BufferedReader buffer = new BufferedReader(reader);
//                String str = "";
//                while (true) {
//                    str = buffer.readLine();
//                    if (str == null) {
//                        break;
//                    }
//                    echo.append(str + "\n");
//                }
//                String[] pingReturn = Utils.getPingStats(echo.toString());
//                if (pingReturn.length != 0) {
//                    MainActivity.measurement.setMinping(pingReturn[4]);
//                    MainActivity.measurement.setMaxping(pingReturn[6]);
//                    MainActivity.measurement.setAvgping(pingReturn[5]);
//                    MainActivity.measurement.setStdevping(pingReturn[7]);
//                    MainActivity.measurement.setPingloss(pingReturn[2].replaceAll("%", ""));
//                    Utils.addNewMeasurement(this.context, DataEvent.PING, null, null);
//                }
//                buffer.close();
//                reader.close();
//            } catch (IOException e2) {
//                e2.printStackTrace();
//            }
//        }
    }
}
