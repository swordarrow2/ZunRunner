package com.meng.zunRunner.ecl.beans;

/**
 * @author Administrator th10_instr_t
 */
public class EclIns implements Cloneable {

    public int time;
    public short id;
    public short size;
    public short param_mask;
    /*
     * The rank bitmask. 1111LHNE Bits mean: easy, normal, hard, lunatic. The
     * rest are always set to 1.
     */
    public byte rank_mask;
    /*
     * There doesn't seem to be a way of telling how many parameters there are
     * from the additional data.
     */
    public byte param_count;
    /*
     * From TH13 on, this field stores the number of current stack references in
     * the parameter list.
     */
    public int zero;
    public byte data[];

    private int dataPosition = 0;
    private EclSub eclSub;

    public EclIns(EclSub sub) {
        eclSub = sub;
    }

    @Override
    public EclIns clone() throws CloneNotSupportedException {
        return (EclIns) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("time:").append(time).append(" id:").append(id).append(" size:").append(size)
                .append(" paraPos:").append(param_mask).append(" rank:").append(toUByte(rank_mask))
                .append(" paraCount:").append(param_count).append("\n");
        stringBuilder.append(printArray(data));
        return stringBuilder.toString();
    }

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

    public String readString() {
        byte[] strByte = new byte[data[dataPosition]];
        dataPosition += 4;
        if (strByte.length >= 0) {
            System.arraycopy(data, dataPosition, strByte, 0, strByte.length);
        }
        dataPosition += strByte.length;
        if (dataPosition == data.length) {
            dataPosition = 0;
        }
        String s = new String(strByte);
        int index = s.indexOf(0);
        if (index == -1) {
            return s;
        }
        return s.substring(0, index);
    }

    private void moveToNextInt() {
        if ((dataPosition & 0b11) == 0) {
            return;
        }
        dataPosition |= 0b11;
        ++dataPosition;
    }

    private String printArray(byte[] array) {
        if (array.length == 0) {
            return "no param";
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (id == 11 || id == 15 || id == 22) {
            stringBuilder.append("str:").append(readString());
        } else if (id == 44 || id == 45) {
            for (int i = 0; i < data.length; i += 4) {
                stringBuilder.append(((param_mask >> (i / 4)) & 0b1) == 1 ? "var:" : "num:").append(readFloat())
                        .append(" | ");
            }
        } else if (id == 300) {
            stringBuilder.append("str:").append(readString()).append(" | ");
            for (int i = dataPosition; i < data.length; i += 4) {
                stringBuilder.append(((param_mask >> (i / 4)) & 0b1) == 1 ? "var:" : "num:").append(readInt()).append(" | ");
            }
        } else {
            for (int i = 0; i < data.length; i += 4) {
                stringBuilder.append(((param_mask >> (i / 4)) & 0b1) == 1 ? "var:" : "num:").append(readInt())
                        .append(" | ");
            }
        }
        dataPosition = 0;
        // stringBuilder.append(((param_mask >> (i / 4)) & 0b1) == 1 ? "var:" :
        // "num:").append(Integer.toHexString(data[i] & 0xFF)).append((i + 1) %
        // 4 == 0 ? " | " : " ");
        return stringBuilder.toString();
    }

}
