package com.meng.zunRunner.sht;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.meng.gui.helpers.ResourcesManager;

public class ShtFile {
    public PlayerArg playerArg = new PlayerArg();
    public ArrayList<Vector2> subPlanePositions = new ArrayList<>();
    public ArrayList<Integer> dataOffsets = new ArrayList<>();
    public ArrayList<Shooter[]> shootersList = new ArrayList<>();

    private byte[] fileByte;
    private int position = 0;
    private int shotStart;

    public ShtFile(String fileName) {
        fileByte = Gdx.files.absolute(ResourcesManager.pathBase + "sht/" + fileName).readBytes();
        playerArg.maxPower = readShort();
        playerArg.totalOffset = readShort();
        playerArg.hitBox = readFloat();
        playerArg.powerAttractSpeed = readFloat();
        playerArg.powerAttractHitBox = readFloat();
        playerArg.normalSpeed = readFloat();
        playerArg.forcedSpeed = readFloat();
        playerArg.normalDiagonalSpeed = readFloat();
        playerArg.forcedDiagonalSpeed = readFloat();
        playerArg.maxPowerLevel = readShort();
        playerArg.unknown1 = readShort();
        playerArg.powerVar = readInt();
        playerArg.maxDamage = readInt();
        readIntArray(playerArg.unknown);
        for (int status = 0; status < 2; ++status) { //normal and slow
            for (int powerFlag = 0; powerFlag < playerArg.maxPower; powerFlag++) { //power 1 to max
                for (int shooterFlag = -1; shooterFlag < powerFlag; ++shooterFlag) { //power is shooter count
                    subPlanePositions.add(new Vector2(readFloat(), readFloat()));
                }
            }
        }
        for (int i = 0; i < playerArg.totalOffset; ++i) {
            dataOffsets.add(readInt());
        }
        shotStart = position;
        for (int i = 0; i < dataOffsets.size(); ++i) {
            Shooter[] shooters;
            int sizeSub1 = dataOffsets.size() - 1;
            if (i < sizeSub1) {
                shooters = new Shooter[(dataOffsets.get(i + 1) - dataOffsets.get(i)) / Shooter.size];
            } else {
                shooters = new Shooter[(fileByte.length - shotStart - dataOffsets.get(i)) / Shooter.size];
            }
            for (int shooterFlag = 0; shooterFlag < shooters.length; ++shooterFlag) {
                Shooter shooter = new Shooter();
                shooter.rate = readByte();
                shooter.delay = readByte();
                shooter.power = readShort();
                shooter.pos = new Vector2(readFloat(), readFloat());
                shooter.hitBox = new Vector2(readFloat(), readFloat());
                shooter.angle = readFloat();
                shooter.speed = readFloat();
                shooter.unknown1 = readInt();
                shooter.option = readByte();
                shooter.unknown2 = readByte();
                shooter.ANM = readShort();
                shooter.SE = readShort();
                shooter.rate2 = readByte();
                shooter.delay2 = readByte();
                readIntArray(shooter.flags);
                shooters[shooterFlag] = shooter;
            }
            if (i < sizeSub1) {
                readInt(); //split bytes
            }
            shootersList.add(shooters);
        }
        fileByte = null;
    }

    private byte readByte() {
        return fileByte[position++];
    }

    private short readShort() {
        return (short) (fileByte[position++] & 0xff | (fileByte[position++] & 0xff) << 8);
    }

    private int readInt() {
        return (fileByte[position++] & 0xff) | (fileByte[position++] & 0xff) << 8 | (fileByte[position++] & 0xff) << 16 | (fileByte[position++] & 0xff) << 24;
    }

    private float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    private void readIntArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = readInt();
        }
    }

}
