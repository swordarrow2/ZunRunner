package com.InsProcess.helper;

public class DataTypeChangeHelper {

    public static int toUByte(byte[] buf, int pos) {
        int luminance = buf[pos] & 0xFF;
        return luminance;
    }

    public static int toUByte(byte b) {
        int luminance = b & 0xFF;
        return luminance;
    }

    public static int toUShort(byte[] buf, int pos) {
        return ((0xFF & (buf[pos] << 8)) | (0xFF & buf[pos + 1])) & 0xFFFF;
    }

    public static long toUInt(byte[] buf, int pos) {
        return ((long) ((0x000000FF & ((int) buf[pos])) << 24 | (0x000000FF & ((int) buf[pos + 1])) << 16 | (0x000000FF & ((int) buf[pos + 2])) << 8 | (0x000000FF & ((int) buf[pos + 3])))) & 0xFFFFFFFFL;
    }

}
