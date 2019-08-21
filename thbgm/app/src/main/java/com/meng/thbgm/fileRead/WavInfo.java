package com.meng.thbgm.fileRead;

public class WavInfo {

    private int dataSize;
    private int channels;
    private int rate;

    public WavInfo(int channels, int rate, int dataSize) {
        this.channels = channels;
        this.rate = rate;
        this.dataSize = dataSize;
    }

    public int getDataSize() {
        return dataSize;
    }
}
