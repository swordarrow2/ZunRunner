package com.meng.zunRunner.anm;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.meng.gui.helpers.ResourcesManager;

public class AnmFile {

    private byte[] fileByte;
    private int position = 0;

    public ArrayList<AnmBean> anmBeans = new ArrayList<>();

    public AnmFile(String fileName) {
        // fileByte = Gdx.files.internal("textures/" + fileName).readBytes();
        fileByte = Gdx.files.absolute(ResourcesManager.pathBase + "textures/" + fileName).readBytes();
        do {
            AnmBean anmBean = new AnmBean();
            if (anmBeans.size() > 0) {
                AnmBean tmpPart = anmBeans.get(anmBeans.size() - 1);
                anmBean.start = tmpPart.header.nextoffset + tmpPart.start;
            }
            anmBean.header = new AnmHeader();
            anmBean.header.version = readInt();
            anmBean.header.sprites = readShort();
            anmBean.header.scripts = readShort();
            anmBean.header.zero1 = readShort();
            anmBean.header.w = readShort();
            anmBean.header.h = readShort();
            anmBean.header.format = readShort();
            anmBean.header.nameoffset = readInt();
            anmBean.header.x = readShort();
            anmBean.header.y = readShort();
            anmBean.header.memorypriority = readInt();
            anmBean.header.thtxoffset = readInt();
            anmBean.header.hasdata = readShort();
            anmBean.header.lowresscale = readShort();
            anmBean.header.nextoffset = readInt();
            readArray(anmBean.header.zero2);
            anmBean.spriteOffset = new int[anmBean.header.sprites];
            for (int i = 0; i < anmBean.spriteOffset.length; ++i) {
                anmBean.spriteOffset[i] = readInt();
            }
            AnmInsOffset[] anmInsOffset = new AnmInsOffset[anmBean.header.scripts];
            for (int i = 0; i < anmInsOffset.length; ++i) {
                anmInsOffset[i] = new AnmInsOffset(readInt(), readInt());
            }
            byte[] bs = new byte[anmBean.spriteOffset[0] - anmBean.header.nameoffset];
            readArray(bs);
            anmBean.picName = new String(bs);
            anmBean.picName = anmBean.picName.substring(0, anmBean.picName.indexOf(0));
            anmBean.sprites = new Sprite[anmBean.header.sprites];
            for (int i = 0; i < anmBean.sprites.length; ++i) {
                anmBean.sprites[i] = new Sprite(readInt(), readFloat(), readFloat(), readFloat(), readFloat());
            }
            anmBean.scripts = new ArrayList[anmBean.header.scripts];
            for (int i = 0; i < anmBean.scripts.length; ++i) {
                ArrayList<AnmIns> inses = new ArrayList<>();
                if (i < anmInsOffset.length - 2) {
                    while (position < anmInsOffset[i + 1].offset + anmBean.start) {
                        AnmIns anmIns = new AnmIns();
                        anmIns.id = readShort();
                        if (anmIns.id == -1) {
                            position += 6;
                        } else {
                            anmIns.length = readShort();
                            anmIns.time = readShort();
                            anmIns.param_mask = readShort();
                            anmIns.data = new byte[anmIns.length - 8];
                            readArray(anmIns.data);
                            inses.add(anmIns);
                        }
                    }
                } else {
                    while (position < anmBean.header.thtxoffset + anmBean.start) {
                        AnmIns anmIns = new AnmIns();
                        anmIns.id = readShort();
                        if (anmIns.id == -1) {
                            position += 6;
                        } else {
                            anmIns.length = readShort();
                            anmIns.time = readShort();
                            anmIns.param_mask = readShort();
                            anmIns.data = new byte[anmIns.length - 8];
                            readArray(anmIns.data);
                            inses.add(anmIns);
                        }
                    }
                }
                anmBean.scripts[i] = inses;
            }
            position = anmBean.header.thtxoffset + anmBean.start;
            anmBean.thtx = new THTX();
            readArray(anmBean.thtx.magic);
            anmBean.thtx.zero = readShort();
            anmBean.thtx.format = readShort();
            anmBean.thtx.w = readShort();
            anmBean.thtx.h = readShort();
            anmBean.thtx.size = readInt();
            anmBean.thtx.data = new byte[anmBean.thtx.size];
            readArray(anmBean.thtx.data);
            anmBeans.add(anmBean);
        } while (anmBeans.get(anmBeans.size() - 1).header.nextoffset != 0);
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

    private void readArray(int[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = readInt();
        }
    }

    private void readArray(byte[] arr) {
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = readByte();
        }
    }

}
