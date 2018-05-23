package com.skynetsoftware.skynetdemo.network.networkmonitor.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.skynetsoftware.skynetdemo.R;
import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;
import com.skynetsoftware.skynetdemo.network.networkmonitor.activity.MainActivity;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.PhoneState;


public class StartVoiceCallReceiver extends BroadcastReceiver {
    public String TAG = "StartVoiceCallReceiver";
    private AlarmManager am;
    private Context context;
    private PendingIntent endIntent;
    private Intent voiceCallIntent;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.voiceSequence_sharedPreference), 0);
        int callDuration = Integer.parseInt(Utils.getFromPrefs(context, context.getResources().getString(R.string.key_voiceSequence_callDuration), "90"));
        int numOfCall = sharedPref.getInt(context.getString(R.string.voiceSequence_numOfCall), 0);
        if (numOfCall < Integer.parseInt(Utils.getFromPrefs(context, context.getResources().getString(R.string.key_voiceSequence_numOfCall), "10"))) {
            String startCallOn = Utils.getFromPrefs(context, context.getResources().getString(R.string.key_voiceSequence_startCall), "ALL");
            String curMode = MainActivity.measurement.getNetwork_mode();
            if (startCallOn.equals("ALL") || startCallOn.equals(curMode)) {
                makeACall();
                MainActivity.measurement.setState(PhoneState.VOICE.toString());
                Editor editor = sharedPref.edit();
                editor.putInt(context.getString(R.string.voiceSequence_numOfCall), numOfCall + 1);
                editor.commit();
                this.endIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, EndVoiceCallReceiver.class), 0);
                this.am = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
                if (VERSION.SDK_INT >= 23) {
                    this.am.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + ((long) (callDuration * 1000)), this.endIntent);
                    return;
                } else {
                    this.am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + ((long) (callDuration * 1000)), this.endIntent);
                    return;
                }
            }
            return;
        }
        Editor editor = sharedPref.edit();
        editor.putInt(context.getString(R.string.voiceSequence_numOfCall), 0);
        editor.commit();
    }

    private void makeACall() {
        if (this.voiceCallIntent == null) {
            this.voiceCallIntent = new Intent("android.intent.action.CALL");
        }
        this.voiceCallIntent.setData(Uri.parse("tel:" + Utils.getFromPrefs(this.context, this.context.getResources().getString(R.string.key_voiceSequence_callednumber), "18001091")));
//        this.voiceCallIntent.setFlags(1111);
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.CALL_PHONE") == 0) {
            this.context.startActivity(this.voiceCallIntent);
            Log.d("VOICE_CALL", "Start a call");
            MainActivity.startCallFlag();
        }
    }
}
