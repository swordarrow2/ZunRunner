package com.InsProcess.parse.beans;

/**
 * @author Administrator th10_header_t
 */
public class EclHeader {
    public byte[] magic = new byte[4];
    public short unknown1; // 1
    public short include_length; // include_offset + ANIM + ECLI length
    public int include_offset; // sizeof(th10_header_t)=0x24
    public int zero1;
    public int sub_count;
    public int[] zero2 = new int[4];

    @Override
    public String toString() {
        return String.format("%s,include_length:%d,include_offset:%d,sub_count:%d", new String(magic), include_length, include_offset, sub_count);
    }
}
