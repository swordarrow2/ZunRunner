package com.InsProcess.parse;

import java.io.*;

import com.InsProcess.parse.beans.EclHeader;
import com.InsProcess.parse.beans.EclList;
import com.badlogic.gdx.utils.StringBuilder;
import com.meng.TaiHunDanmaku.ui.*;
import com.InsProcess.parse.beans.*;

public class EclFile {
    public Ecl eclManager;
    private byte[] fileByte;
    private int position = 0;
    private EclHeader eclHeader;
    private EclList anm;
    private EclList ecli;
    
	private String name;

    public EclFile(Ecl eclManager,String fileName) {
	  name=fileName;
	  this.eclManager=eclManager;
        File ecl = new File(GameMain.baseEclPath + fileName);
        fileByte = new byte[(int) ecl.length()];
        try {
            FileInputStream fin = new FileInputStream(ecl);
            fin.read(fileByte);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
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
            subPacks[i] = new EclSubPack(this);
        }
			for (int positionLoopFlag = 0, subPacksLength = subPacks.length; positionLoopFlag < subPacksLength; positionLoopFlag++) {
				EclSubPack subPack = subPacks[positionLoopFlag];
				subPack.position = readInt();
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
				subNameData[nameLoopFlag] = readByte();
			  }
			String[] names = new String(subNameData).split(String.valueOf((char) 0));
			for (int i = 0; i < subPacks.length; ++i) {
				subPacks[i].subName = names[i];
			  }
			moveToNextInt();
			int subPackLenSub1 = subPacks.length - 1;
			for (int i = 0; i < eclHeader.sub_count; ++i) {
				EclSub sub = new EclSub(this,subPacks[i]);
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
					sub.data[readDataLoopFlag] = readByte();
				  }
				subPacks[i].sub = sub;
				sub.insList();
				moveToNextInt();
			  }	
		String[] includeName=ecli.getFileName();
		if(includeName!=null){
			for(String s:includeName){
				new EclFile(eclManager,s);
			  }
		  }
		  for(EclSubPack esp:subPacks){
			eclManager.subPacks.add(esp);
		  }
    }

    private EclList onLoadEclList() {
        EclList eclList = new EclList();
        eclList.magic = readMagic();
        eclList.count = readInt();
        int length = 0;
        for (int zeroCount = 0; zeroCount < eclList.count; ++length) {
            if (readByte(length + position) == 0) {
                ++zeroCount;
            }
        }
        if (length != 0) {
            --length;
        }
        eclList.data = new byte[length];
        for (int i = 0; i < length; ++i) {
            eclList.data[i] = readByte();
        }
        moveToNextInt();
        return eclList;
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
        return fileByte[pos];
    }

    private short readShort(int pos) {
        return (short) (fileByte[pos] & 0xff | (fileByte[pos + 1] & 0xff) << 8);
    }

    private int readInt(int pos) {
	  return (fileByte[pos] & 0xff) | (fileByte[pos + 1] & 0xff) << 8 | (fileByte[pos + 2] & 0xff) << 16 | (fileByte[pos + 3] & 0xff) << 24;
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
        ba[0] = fileByte[position];
        ++position;
        ba[1] = fileByte[position];
        ++position;
        ba[2] = fileByte[position];
        ++position;
        ba[3] = fileByte[position];
        ++position;
        return ba;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
		return name;
    }
}
