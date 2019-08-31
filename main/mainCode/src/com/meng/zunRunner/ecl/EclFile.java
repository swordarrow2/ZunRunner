package com.meng.zunRunner.ecl;

import java.util.Collections;

import com.meng.zunRunner.ecl.beans.EclHeader;
import com.meng.zunRunner.ecl.beans.EclIncludeList;
import com.badlogic.gdx.Gdx;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.zunRunner.ByteReader;
import com.meng.zunRunner.ecl.beans.*;

public class EclFile {
    public EclManager eclManager;
    private EclHeader eclHeader;
    private EclIncludeList anm;
    private EclIncludeList ecli;

    public EclFile(EclManager eclManager, String fileName) {
        this.eclManager = eclManager;
        ByteReader byteReader=new ByteReader(Gdx.files.absolute(ResourcesManager.pathBase + "ecl/" + fileName).readBytes());
        eclHeader = new EclHeader();
        byteReader.readArray(eclHeader.magic);
        eclHeader.unknown1 = byteReader.readShort();
        eclHeader.include_length = byteReader.readShort();
        eclHeader.include_offset = byteReader.readInt();
        eclHeader.zero1 = byteReader.readInt();
        eclHeader.sub_count = byteReader.readInt();
        eclHeader.zero2[0] = byteReader.readInt();
        eclHeader.zero2[1] = byteReader.readInt();
        eclHeader.zero2[2] = byteReader.readInt();
        eclHeader.zero2[3] = byteReader.readInt();
        anm = new EclIncludeList();
        byteReader.readArray(anm.magic);
        anm.count = byteReader.readInt();
        int length = 0;
        for (int zeroCount = 0; zeroCount < anm.count; ++length) {
            if (byteReader.readByte(length + byteReader.position) == 0) {
                ++zeroCount;
            }
        }
        if (length != 0) {
            --length;
        }
        anm.data = new byte[length];
        for (int i = 0; i < length; ++i) {
        	anm.data[i] = byteReader.readByte();
        }
        byteReader.moveToNextInt();
        
        ecli = new EclIncludeList();
        byteReader.readArray(ecli.magic);
        ecli.count = byteReader.readInt();
        int length2 = 0;
        for (int zeroCount = 0; zeroCount < ecli.count; ++length2) {
            if (byteReader.readByte(length2 + byteReader.position) == 0) {
                ++zeroCount;
            }
        }
        if (length2 != 0) {
            --length2;
        }
        ecli.data = new byte[length2];
        for (int i = 0; i < length2; ++i) {
        	ecli.data[i] = byteReader.readByte();
        }
        byteReader.moveToNextInt();
        EclSubPack[] subPacks = new EclSubPack[eclHeader.sub_count];
        for (int i = 0; i < eclHeader.sub_count; ++i) {
            subPacks[i] = new EclSubPack();
        }
        for (int i = 0, subPacksLength = subPacks.length; i < subPacksLength; i++) {
            subPacks[i].position = byteReader.readInt();
        }
        byteReader.moveToNextInt();
        int subNameDataLength = 0;
        for (int subNameZeroCount = 0; subNameZeroCount < subPacks.length; ++subNameDataLength) {
            if (byteReader.readByte(subNameDataLength + byteReader.position) == 0) {
                ++subNameZeroCount;
            }
        }
        --subNameDataLength;
        byte[] subNameData = new byte[subNameDataLength];
        for (int nameLoopFlag = 0; nameLoopFlag < subNameDataLength; ++nameLoopFlag) {
            subNameData[nameLoopFlag] = byteReader.readByte(byteReader.position++);
        }
        String[] names = new String(subNameData).split(String.valueOf((char) 0));
        for (int i = 0; i < subPacks.length; ++i) {
            subPacks[i].subName = names[i];
        }
        byteReader.moveToNextInt();
        int subPackLenSub1 = subPacks.length - 1;
        for (int i = 0; i < eclHeader.sub_count; ++i) {
            EclSub sub = new EclSub(this, subPacks[i]);
            byteReader.readArray(sub.magic);
            sub.data_offset = byteReader.readInt();
            sub.zero[0] = byteReader.readInt();
            sub.zero[1] = byteReader.readInt();
            if (i == subPackLenSub1) {
                sub.data = new byte[byteReader.fileByte.length - subPacks[i].position - sub.data_offset];
            } else {
                sub.data = new byte[subPacks[i + 1].position - subPacks[i].position - sub.data_offset];
            }
            for (int readDataLoopFlag = 0; readDataLoopFlag < sub.data.length; ++readDataLoopFlag) {
                sub.data[readDataLoopFlag] = byteReader.readByte(byteReader.position++);
            }
            sub.readIns();
            subPacks[i].sub = sub;
            byteReader.moveToNextInt();
        }
        String[] includeName = ecli.getFileName();
        if (includeName != null) {
            for (String s : includeName) {
                new EclFile(eclManager, s);
            }
        }
        Collections.addAll(EclManager.subPacks, subPacks);
    }
}
