package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

public interface IMeasurePointChange {
    void onAddMeasurePoint(Measurement measurement);

    void onMeasurePointsChange();
}
