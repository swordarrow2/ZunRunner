package com.meng.zunRunner.sht;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.*;
import com.meng.gui.ui.*;
import com.meng.zunRunner.*;
import java.util.*;

public class ShtFile {
    public PlayerArg playerArg = new PlayerArg();
    public ArrayList<Vector2> subPlanePositions = new ArrayList<>();
    public ArrayList<Integer> dataOffsets = new ArrayList<>();
    public ArrayList<Shooter[]> shootersList = new ArrayList<>();

    private int shotStart;

    public ShtFile(String fileName) {
    	ByteReader byteReader=new ByteReader(Gdx.files.absolute(PicMain.pathBase + "sht/" + fileName).readBytes());
        playerArg.maxPower = byteReader.readShort();
        playerArg.totalOffset = byteReader.readShort();
        playerArg.hitBox = byteReader.readFloat();
        playerArg.powerAttractSpeed = byteReader.readFloat();
        playerArg.powerAttractHitBox = byteReader.readFloat();
        playerArg.normalSpeed = byteReader.readFloat();
        playerArg.forcedSpeed = byteReader.readFloat();
        playerArg.normalDiagonalSpeed = byteReader.readFloat();
        playerArg.forcedDiagonalSpeed = byteReader.readFloat();
        playerArg.maxPowerLevel = byteReader.readShort();
        playerArg.unknown1 = byteReader.readShort();
        playerArg.powerVar = byteReader.readInt();
        playerArg.maxDamage = byteReader.readInt();
        byteReader.readArray(playerArg.unknown);
        for (int status = 0; status < 2; ++status) { //normal and slow
            for (int powerFlag = 0; powerFlag < playerArg.maxPower; powerFlag++) { //power 1 to max
                for (int shooterFlag = -1; shooterFlag < powerFlag; ++shooterFlag) { //power is shooter count
                    subPlanePositions.add(new Vector2(byteReader.readFloat(), byteReader.readFloat()));
				  }
			  }
		  }
        for (int i = 0; i < playerArg.totalOffset; ++i) {
            dataOffsets.add(byteReader.readInt());
		  }
        shotStart = byteReader.position;
        for (int i = 0; i < dataOffsets.size(); ++i) {
            Shooter[] shooters;
            int sizeSub1 = dataOffsets.size() - 1;
            if (i < sizeSub1) {
                shooters = new Shooter[(dataOffsets.get(i + 1) - dataOffsets.get(i)) / Shooter.size];
			  } else {
                shooters = new Shooter[(byteReader.fileByte.length - shotStart - dataOffsets.get(i)) / Shooter.size];
			  }
            for (int shooterFlag = 0; shooterFlag < shooters.length; ++shooterFlag) {
                Shooter shooter = new Shooter();
                shooter.rate = byteReader.readByte();
                shooter.delay = byteReader.readByte();
                shooter.power = byteReader.readShort();
                shooter.pos = new Vector2(byteReader.readFloat(), byteReader.readFloat());
                shooter.hitBox = new Vector2(byteReader.readFloat(), byteReader.readFloat());
                shooter.angle = byteReader.readFloat();
                shooter.speed = byteReader.readFloat();
                shooter.unknown1 = byteReader.readInt();
                shooter.option = byteReader.readByte();
                shooter.unknown2 = byteReader.readByte();
                shooter.ANM = byteReader.readShort();
                shooter.SE = byteReader.readShort();
                shooter.rate2 = byteReader.readByte();
                shooter.delay2 = byteReader.readByte();
                byteReader.readArray(shooter.flags);
                shooters[shooterFlag] = shooter;
			  }
            if (i < sizeSub1) {
            	byteReader.readInt(); //split bytes
			  }
            shootersList.add(shooters);
		  }
	  }
  }
