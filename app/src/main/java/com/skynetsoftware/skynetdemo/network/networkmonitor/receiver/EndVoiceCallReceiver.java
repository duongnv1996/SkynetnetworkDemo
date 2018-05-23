package com.skynetsoftware.skynetdemo.network.networkmonitor.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.skynetsoftware.skynetdemo.R;
import com.skynetsoftware.skynetdemo.network.networkmonitor.Utils;
import com.skynetsoftware.skynetdemo.network.networkmonitor.activity.MainActivity;
import com.skynetsoftware.skynetdemo.network.networkmonitor.object.PhoneState;

import java.lang.reflect.Method;


public class EndVoiceCallReceiver extends BroadcastReceiver {
    private AlarmManager am;
    private Context context;
    private PendingIntent startIntent;

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        endACall();
        String state = MainActivity.measurement.getState();
        if (state != null && state.trim().equals(PhoneState.VOICE.toString())) {
            MainActivity.measurement.setState(PhoneState.IDLE.toString());
        }
        int delayTime = Integer.parseInt(Utils.getFromPrefs(context, context.getResources().getString(R.string.key_voiceSequence_pauseCall), "10"));
        this.am = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        this.startIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, StartVoiceCallReceiver.class), 0);
        this.am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + ((long) (delayTime * 1000)), this.startIntent);
    }

    private void endACall() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
            Method methodGetITelephony = Class.forName(telephonyManager.getClass().getName()).getDeclaredMethod("getITelephony", new Class[0]);
            methodGetITelephony.setAccessible(true);
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager, new Object[0]);
            Method methodEndCall = Class.forName(telephonyInterface.getClass().getName()).getDeclaredMethod("endCall", new Class[0]);
            Log.d("VOICE_CALL", "End a call");
            MainActivity.endCallFlag();
            methodEndCall.invoke(telephonyInterface, new Object[0]);
        } catch (Exception e) {
        }
    }
}
