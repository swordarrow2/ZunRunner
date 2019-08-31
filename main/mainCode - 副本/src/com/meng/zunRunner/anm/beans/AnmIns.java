package com.meng.zunRunner.anm.beans;

public class AnmIns {
    public short id;
    public short length;
    public short time;
    public short param_mask;
    public byte data[];

    private int dataPosition = 0;

    private int toUByte(byte buf) {
        return buf & 0xFF;
    }

    public int readInt() {
        int i = (data[dataPosition++] & 0xff) | (data[dataPosition++] & 0xff) << 8 | (data[dataPosition++] & 0xff) << 16 | (data[dataPosition++] & 0xff) << 24;
        if (dataPosition == data.length) {
            dataPosition = 0;
        }
        return i;
    }

    public float readFloat() {
        float f = Float.intBitsToFloat((data[dataPosition++] & 0xff) | (data[dataPosition++] & 0xff) << 8 | (data[dataPosition++] & 0xff) << 16 | (data[dataPosition++] & 0xff) << 24);
        if (dataPosition == data.length) {
            dataPosition = 0;
        }
        return f;
    }
}
