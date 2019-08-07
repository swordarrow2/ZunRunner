package com.InsProcess.parse.beans;


import com.InsProcess.helper.DataTypeChangeHelper;

/**
 * @author Administrator th10_instr_t
 */
public class EclIns {

    public int time;
    public short id;
    public short size;
    public short param_mask;
    /* The rank bitmask.
     *   1111LHNE
     * Bits mean: easy, normal, hard, lunatic. The rest are always set to 1. */
    public byte rank_mask;
    /* There doesn't seem to be a way of telling how many parameters there are
     * from the additional data. */
    public byte param_count;
    /* From TH13 on, this field stores the number of current stack references
     * in the parameter list. */
    public int zero;
    public byte data[];

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        //rank E:193 N:194 H:196 L:232
        stringBuilder.append("time:").append(time).append(" id:").append(id).append(" size:").append(size).append(" param_mask:").append(param_mask).append(" rank_mask:").append(toUByte(rank_mask)).append(" param_count:").append(param_count).append("\n");
        stringBuilder.append(printArray(data));
        return stringBuilder.toString();
    }

    private int toUByte(byte buf) {
        return buf & 0xFF;
    }
    private int readInt(int pos) {
        return (data[pos] & 0xff) | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
    }
    private String printArray(byte[] array) {
        if (array.length == 0) {
            return "0";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<data.length;i+=4){
            stringBuilder.append(readInt(i)).append(" | ");
        }
      //  for (int i = 0, arrayLength = array.length; i < arrayLength; i++) {
      //      byte b = array[i];
      //      stringBuilder.append(Integer.toHexString(b & 0xFF)).append((i + 1) % 4 == 0 ? " | " : " ");
     //   }
     //   stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
