package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

public enum NetworkMode {
    MODE_2G("2G"),
    MODE_3G("3G"),
    MODE_4G("4G");
    
    private final String text;

    private NetworkMode(String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
