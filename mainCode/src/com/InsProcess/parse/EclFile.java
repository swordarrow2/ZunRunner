package com.InsProcess.parse;

import java.io.*;

import com.InsProcess.parse.beans.EclHeader;
import com.InsProcess.parse.beans.EclList;
import com.meng.TaiHunDanmaku.ui.*;
import com.InsProcess.parse.beans.*;

public class EclFile {
    private byte[] fileByte;
    private int position = 0;
	EclHeader eclHeader;
	EclList anm;
	EclList ecli;
	EclSubPack[] subPacks;

    public EclFile(String fileName) {
        File ecl = new File(GameMain.baseEclPath + fileName);
        fileByte = new byte[(int) ecl.length()];
        try {
            FileInputStream fin = new FileInputStream(ecl);
            fin.read(fileByte);
        } catch (Exception e) {
            throw new RuntimeException("boom on create ecl entity");
        }
		eclHeader = new EclHeader();
		eclHeader.magic[0] = readByte();
        eclHeader.magic[1] = readByte();
        eclHeader.magic[2] = readByte();
        eclHeader.magic[3] = readByte();
        eclHeader.unknown1 = readShort();
        eclHeader.include_length = readShort();
        eclHeader.include_offset = readInt();
        eclHeader.zero1 = readInt();
        eclHeader.sub_count = readInt();
        eclHeader.zero2[0] = readInt();
        eclHeader.zero2[1] = readInt();
        eclHeader.zero2[2] = readInt();
        eclHeader.zero2[3] = readInt();
		anm=onLoadEclList();
		ecli=onLoadEclList();
		subPacks=new EclSubPack[eclHeader.sub_count];
		for(int i=0;i<eclHeader.sub_count;++i){
		  subPacks[i]=new EclSubPack();
		}
		readSubs();
    }

	public String getSubNames(){
	  StringBuilder sb=new StringBuilder();
	  for(EclSubPack esp:subPacks){
		sb.append(esp.subName).append("\n");
	  }
	  return sb.toString();
	}
	
    private EclList onLoadEclList() {
        EclList eclList = new EclList();
        eclList.magic[0] = readByte();
        eclList.magic[1] = readByte();
        eclList.magic[2] = readByte();
        eclList.magic[3] = readByte();
        eclList.count = readInt();
        int length = 0;
        for (int zeroCount = 0; zeroCount < eclList.count; ++length) {
            if (readByte(length + position) == 0) {
                ++zeroCount;
            }
        }
        --length;
        eclList.data = new byte[length];
        for (int i = 0; i < length; ++i) {
            eclList.data[i] = readByte();
			if(eclList.data[i]==0){
			  eclList.data[i]=32;
			}
        }
        moveToNextInt();
        return eclList;
    }
	
	private void readSubs(){  
	  for(int i=0;i<subPacks.length;++i){
		subPacks[i].position=readInt();
	  }  
	  int length = 0;
      for (int zeroCount = 0; zeroCount < subPacks.length; ++length) {
          if (readByte(length + position) == 0) {
              ++zeroCount;
		  }
	  }
      --length;
      byte[] data = new byte[length];
      for (int i = 0; i < length; ++i) {
        data[i] = readByte();
		if(data[i]==0){
			data[i]=32;
		  }
	  }
	  String[] names=new String(data).split(" ");
	  for(int i=0;i<subPacks.length;++i){
		  subPacks[i].subName=names[i];
	  }
	  moveToNextInt();
	  for(int i=0;i<eclHeader.sub_count;++i){
	      EclSub sub=new EclSub();
	      sub.magic[0] = readByte();
          sub.magic[1] = readByte();
          sub.magic[2] = readByte();
          sub.magic[3] = readByte();
	      sub.data_offset=readInt();
	      sub.zero[0]=readInt();
	      sub.zero[1]=readInt();
		  if(i<subPacks.length-2){
			try{
		      sub.data=new byte[subPacks[i+1].position-subPacks[i].position-sub.data_offset-1];
		       }catch(Exception e){
				   throw new RuntimeException(" "+subPacks[i+1].position+" "+subPacks[i].position+" "+sub.data_offset);
			   }
		  }else{
			  sub.data=new byte[fileByte.length-subPacks[i].position-sub.data_offset];
		  }
		  for(int ii=0;ii<data.length;++ii){
			sub.data[ii]=readByte();
		  }
		subPacks[i].sub=sub;
		}  
	}

    public byte readByte() {
        return readByte(position++);
    }

    public short readShort() {
        short s = readShort(position);
        position += 2;
        return s;
    }

    public int readInt() {
        int i = readInt(position);
        position += 4;
        return i;
    }

    public long readLong() {
        long l = readLong(position);
        position += 8;
        return l;
    }

    public byte readByte(int pos) {
        return fileByte[pos];
    }

    public short readShort(int pos) {
        return (short) (fileByte[pos] | fileByte[pos + 1] << 8);
    }

    public int readInt(int pos) {
        return fileByte[pos] | fileByte[pos + 1] << 8 | fileByte[pos + 2] << 16 | fileByte[pos + 3] << 24;
    }

    public long readLong(int pos) {
        return fileByte[pos] | fileByte[pos + 1] << 8 | fileByte[pos + 2] << 16 | fileByte[pos + 3] << 24 | (long) fileByte[pos + 4] << 32 | (long) fileByte[pos + 5] << 40 | (long) fileByte[pos + 6] << 48 | (long) fileByte[pos + 7] << 56;
    }

    public void moveToNextInt() {
        position |= 0b11;
        ++position;
    }

    public int getPosition() {
        return position;
    }
}
