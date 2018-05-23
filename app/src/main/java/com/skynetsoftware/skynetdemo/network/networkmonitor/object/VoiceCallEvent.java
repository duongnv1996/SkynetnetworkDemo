package com.skynetsoftware.skynetdemo.network.networkmonitor.object;

public enum VoiceCallEvent {
    DROP("DROP_CALL"),
    BLOCK("BLOCK_CALL"),
    CSSR("CSSR");
    
    private final String text;

    private VoiceCallEvent(String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
