package com.InsProcess.parse.beans;

import com.InsProcess.helper.DataTypeChangeHelper;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * @author Administrator th10_sub_t
 */
public class EclSub {
    public byte[] magic = new byte[4];
    public int data_offset;/* sizeof(th10_sub_t) */
    public int[] zero = new int[2];
    public byte[] data;

    private EclSubPack eclSubPack;

    public EclSub(EclSubPack eclSubPack) {
        this.eclSubPack = eclSubPack;
    }

    private int position = 0;

    private ArrayList<EclIns> inses = new ArrayList<>();

    public void insList() {
        while (position < data.length) {
            EclIns eclIns = new EclIns();
            eclIns.time = readInt();
            eclIns.id = readShort();
            eclIns.size = readShort();
            eclIns.param_mask = readShort();
            eclIns.rank_mask = readByte();
            eclIns.param_count = readByte();
            eclIns.zero = readInt();
            //    position -= 16;
            int bound = eclIns.size - 16;
            eclIns.data = new byte[bound];
            for (int i = 0; i < bound; ++i) {
                eclIns.data[i] = data[position];
                ++position;
            }
            inses.add(eclIns);
            //        System.out.println("sub:" + eclSubPack.subName + " ins_" + eclIns.id + " end,now position:" + position);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        for (EclIns eclIns : inses) {
            stringBuilder.append("\n").append(eclIns.toString());
        }
        return stringBuilder.toString();
    }

    private byte readByte() {
        return readByte(position++);
    }

    private short readShort() {
        short s = readShort(position);
        position += 2;
        return s;
    }

    private int readInt() {
        int i = readInt(position);
        position += 4;
        return i;
    }

    private byte readByte(int pos) {
        return data[pos];
    }

    private short readShort(int pos) {
        return (short) (data[pos] & 0xff | (data[pos + 1] & 0xff) << 8);
    }

    private int readInt(int pos) {
        return (data[pos] & 0xff) | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
    }

    private void moveToNextInt() {
        if ((position & 0b11) == 0) {
            return;
        }
        position |= 0b11;
        ++position;
    }


}
