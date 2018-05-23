package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

public enum PhoneState {
    IDLE("IDLE"),
    VOICE("VOICE"),
    DATA("DATA");
    
    private final String text;

    private PhoneState(String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
