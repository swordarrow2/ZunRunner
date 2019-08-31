package com.meng.zunRunner.ecl.beans;

/**
 * @author Administrator th10_list_t
 */
public class EclIncludeList {
    public byte[] magic = new byte[4];
    public int count;
    public byte[] data;

    public String[] getFileName() {
        if (data.length == 0) {
            return null;
        }
        return new String(data).split(String.valueOf((char) 0));
    }
}
