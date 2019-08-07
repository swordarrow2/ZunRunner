package com.InsProcess.parse.beans;

/**
 * @author Administrator th10_sub_t
 */
public class EclSub {
    public byte[] magic = new byte[4];
    public int data_offset;/* sizeof(th10_sub_t) */
    public int[] zero = new int[2];
    public byte[] data;
}
