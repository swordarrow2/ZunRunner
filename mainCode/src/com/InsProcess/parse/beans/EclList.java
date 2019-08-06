package com.InsProcess.parse.beans;

/**
 * @author Administrator th10_list_t
 */
public class EclList {
    public byte[] magic = new byte[4];
    public int count;
    public byte[] data;

    @Override
    public String toString() {
        return String.format("%s,count:%d,data:%s", new String(magic), count, new String(data));
    }
}
