package com.InsProcess.parse;
import java.io.*;
import com.meng.TaiHunDanmaku.ui.*;

public class EclFile {
	public byte[] fileByte;
	public int position=0;
	public EclFile(String fileName) {
		File ecl=new File(FightScreen.baseEclPath + fileName);
		fileByte = new byte[(int)ecl.length()];
		try {
			FileInputStream fin=new FileInputStream(ecl);
			fin.read(fileByte);
		  } catch (Exception e) {
			throw new RuntimeException("boom on create ecl entity");
		  } 
	  }

	public byte readByte(int pos) {
		return fileByte[pos];
	  }

	public int readInteger(int pos) {
		return ((int)fileByte[pos]) | ((int)fileByte[pos + 1]) << 8 | ((int)fileByte[pos + 2]) << 16 | ((int)fileByte[pos + 3]) << 24;
	  }
	
	public void moveToNextInt() {
		position |= 0b11;			
	  }
  }
