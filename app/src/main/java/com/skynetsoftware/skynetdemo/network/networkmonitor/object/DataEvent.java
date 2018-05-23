package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

public enum DataEvent {
    CONNCECT_SUCESS("CONNCECT_SUCESS"),
    CONNECT_FAIL("CONNCECT_FAIL"),
    PING("PING"),
    DOWNLOADING("DOWNLOADING"),
    UPLOADING("UPLOADING");
    
    private final String text;

    private DataEvent(String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
