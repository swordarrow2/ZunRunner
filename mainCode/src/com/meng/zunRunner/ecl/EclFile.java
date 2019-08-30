package com.meng.zunRunner.ecl;

import java.util.Collections;

import com.meng.zunRunner.ecl.beans.EclHeader;
import com.meng.zunRunner.ecl.beans.EclIncludeList;
import com.badlogic.gdx.Gdx;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.zunRunner.ecl.beans.*;

public class EclFile {
    public EclManager eclManager;
    private byte[] fileByte;
    private int position = 0;
    private EclHeader eclHeader;
    private EclIncludeList anm;
    private EclIncludeList ecli;

    private String name;

    public EclFile(EclManager eclManager, String fileName) {
        name = fileName;
        this.eclManager = eclManager;
        fileByte = Gdx.files.absolute(ResourcesManager.pathBase + "ecl/" + fileName).readBytes();
        eclHeader = new EclHeader();
        eclHeader.magic = readMagic();
        eclHeader.unknown1 = readShort();
        eclHeader.include_length = readShort();
        eclHeader.include_offset = readInt();
        eclHeader.zero1 = readInt();
        eclHeader.sub_count = readInt();
        eclHeader.zero2[0] = readInt();
        eclHeader.zero2[1] = readInt();
        eclHeader.zero2[2] = readInt();
        eclHeader.zero2[3] = readInt();
        anm = onLoadEclList();
        ecli = onLoadEclList();
        EclSubPack[] subPacks = new EclSubPack[eclHeader.sub_count];
        for (int i = 0; i < eclHeader.sub_count; ++i) {
            subPacks[i] = new EclSubPack();
        }
        for (int i = 0, subPacksLength = subPacks.length; i < subPacksLength; i++) {
            subPacks[i].position = readInt();
        }
        moveToNextInt();
        int subNameDataLength = 0;
        for (int subNameZeroCount = 0; subNameZeroCount < subPacks.length; ++subNameDataLength) {
            if (readByte(subNameDataLength + position) == 0) {
                ++subNameZeroCount;
            }
        }
        --subNameDataLength;
        byte[] subNameData = new byte[subNameDataLength];
        for (int nameLoopFlag = 0; nameLoopFlag < subNameDataLength; ++nameLoopFlag) {
            subNameData[nameLoopFlag] = readByte(position++);
        }
        String[] names = new String(subNameData).split(String.valueOf((char) 0));
        for (int i = 0; i < subPacks.length; ++i) {
            subPacks[i].subName = names[i];
            //      System.out.println(names[i]+" :"+subPacks[i].position);
        }
        moveToNextInt();
        int subPackLenSub1 = subPacks.length - 1;
        for (int i = 0; i < eclHeader.sub_count; ++i) {
            EclSub sub = new EclSub(this, subPacks[i]);
            sub.magic = readMagic();
            sub.data_offset = readInt();
            sub.zero[0] = readInt();
            sub.zero[1] = readInt();
            if (i == subPackLenSub1) {
                sub.data = new byte[fileByte.length - subPacks[i].position - sub.data_offset];
            } else {
                sub.data = new byte[subPacks[i + 1].position - subPacks[i].position - sub.data_offset];
            }
            for (int readDataLoopFlag = 0; readDataLoopFlag < sub.data.length; ++readDataLoopFlag) {
                sub.data[readDataLoopFlag] = readByte(position++);
            }
            sub.readIns();
            subPacks[i].sub = sub;
            moveToNextInt();
        }
        String[] includeName = ecli.getFileName();
        if (includeName != null) {
            for (String s : includeName) {
                new EclFile(eclManager, s);
            }
        }
        Collections.addAll(EclManager.subPacks, subPacks);
        fileByte = null;
    }

    private EclIncludeList onLoadEclList() {
        EclIncludeList eclIncludeList = new EclIncludeList();
        eclIncludeList.magic = readMagic();
        eclIncludeList.count = readInt();
        int length = 0;
        for (int zeroCount = 0; zeroCount < eclIncludeList.count; ++length) {
            if (readByte(length + position) == 0) {
                ++zeroCount;
            }
        }
        if (length != 0) {
            --length;
        }
        eclIncludeList.data = new byte[length];
        for (int i = 0; i < length; ++i) {
            eclIncludeList.data[i] = readByte(position++);
        }
        moveToNextInt();
        return eclIncludeList;
    }

    private short readShort() {
        return (short) (fileByte[position++] & 0xff | (fileByte[position++] & 0xff) << 8);
    }

    private int readInt() {
        return (fileByte[position++] & 0xff) | (fileByte[position++] & 0xff) << 8 | (fileByte[position++] & 0xff) << 16 | (fileByte[position++] & 0xff) << 24;
    }

    private byte readByte(int pos) {
        return fileByte[pos];
    }


    private void moveToNextInt() {
        if ((position & 0b11) == 0) {
            return;
        }
        position |= 0b11;
        ++position;
    }

    private byte[] readMagic() {
        byte[] ba = new byte[4];
        ba[0] = fileByte[position++];
        ba[1] = fileByte[position++];
        ba[2] = fileByte[position++];
        ba[3] = fileByte[position++];
        return ba;
    }

    @Override
    public String toString() {
        return name;
    }
}
