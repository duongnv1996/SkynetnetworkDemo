package com.skynetsoftware.skynetdemo.network.networkmonitor.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skynetsoftware.skynetdemo.R;

public class ServingCellFragment extends Fragment {
    ServingCellFragmentAsyncTask as;
    private TextView ground;
    private TextView txtMcc;
    private TextView txtMnc;
    private TextView txtNetworkOperator;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_serving_cell, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TelephonyManager tlmg = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String operatorName = tlmg.getNetworkOperatorName();
        String nwOperator = tlmg.getNetworkOperator();
        this.txtNetworkOperator = (TextView) view.findViewById(R.id.tvwTitle);
        this.txtNetworkOperator.setText("Operator: "+operatorName);
        this.txtMcc = (TextView) view.findViewById(R.id.txtMCC);
        this.txtMnc = (TextView) view.findViewById(R.id.txtMNC);
        this.txtMcc.setText("MCC: " + "452");
        this.txtMnc.setText("MNC: " + "02");
        if (this.as == null) {
            this.as = new ServingCellFragmentAsyncTask(view, getActivity());
            this.as.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.as.stop();
    }
}
