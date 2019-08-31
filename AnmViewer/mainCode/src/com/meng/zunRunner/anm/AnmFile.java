package com.meng.zunRunner.anm;

import com.badlogic.gdx.*;
import com.meng.gui.ui.*;
import com.meng.zunRunner.*;
import com.meng.zunRunner.anm.beans.*;
import java.util.*;

public class AnmFile {

    public ArrayList<AnmBean> anmBeans = new ArrayList<>();

    public AnmFile() {
        // fileByte = Gdx.files.internal("textures/" + fileName).readBytes();
    	ByteReader byteReader=new ByteReader(Gdx.files.absolute(PicMain.anmPath).readBytes()); 
        do {
            AnmBean anmBean = new AnmBean();
            if (anmBeans.size() > 0) {
                AnmBean tmpPart = anmBeans.get(anmBeans.size() - 1);
                anmBean.start = tmpPart.header.nextoffset + tmpPart.start;
            }
            anmBean.header = new AnmHeader();
            anmBean.header.version = byteReader.readInt();
            anmBean.header.sprites = byteReader.readShort();
            anmBean.header.scripts = byteReader.readShort();
            anmBean.header.zero1 = byteReader.readShort();
            anmBean.header.w = byteReader.readShort();
            anmBean.header.h = byteReader.readShort();
            anmBean.header.format = byteReader.readShort();
            anmBean.header.nameoffset = byteReader.readInt();
            anmBean.header.x = byteReader.readShort();
            anmBean.header.y = byteReader.readShort();
            anmBean.header.memorypriority = byteReader.readInt();
            anmBean.header.thtxoffset = byteReader.readInt();
            anmBean.header.hasdata = byteReader.readShort();
            anmBean.header.lowresscale = byteReader.readShort();
            anmBean.header.nextoffset = byteReader.readInt();
            byteReader.readArray(anmBean.header.zero2);
            anmBean.spriteOffset = new int[anmBean.header.sprites];
            for (int i = 0; i < anmBean.spriteOffset.length; ++i) {
                anmBean.spriteOffset[i] = byteReader.readInt();
            }
            AnmInsOffset[] anmInsOffset = new AnmInsOffset[anmBean.header.scripts];
            for (int i = 0; i < anmInsOffset.length; ++i) {
                anmInsOffset[i] = new AnmInsOffset(byteReader.readInt(), byteReader.readInt());
            }
            byte[] bs = new byte[anmBean.spriteOffset[0] - anmBean.header.nameoffset];
            byteReader.readArray(bs);
            anmBean.picName = new String(bs);
            anmBean.picName = anmBean.picName.substring(0, anmBean.picName.indexOf(0));
            anmBean.sprites = new Sprite[anmBean.header.sprites];
            for (int i = 0; i < anmBean.sprites.length; ++i) {
                anmBean.sprites[i] = new Sprite(byteReader.readInt(),byteReader. readFloat(), byteReader.readFloat(), byteReader.readFloat(), byteReader.readFloat());
            }
            anmBean.scripts = new ArrayList[anmBean.header.scripts];
            for (int i = 0; i < anmBean.scripts.length; ++i) {
                ArrayList<AnmIns> inses = new ArrayList<>();
                if (i < anmInsOffset.length - 2) {
                    while (byteReader.position < anmInsOffset[i + 1].offset + anmBean.start) {
                        AnmIns anmIns = new AnmIns();
                        anmIns.id = byteReader.readShort();
                        if (anmIns.id == -1) {
                        	byteReader.position += 6;
                        } else {
                            anmIns.length = byteReader.readShort();
                            anmIns.time = byteReader.readShort();
                            anmIns.param_mask = byteReader.readShort();
                            anmIns.data = new byte[anmIns.length - 8];
                            byteReader.readArray(anmIns.data);
                            inses.add(anmIns);
                        }
                    }
                } else {
                    while (byteReader.position < anmBean.header.thtxoffset + anmBean.start) {
                        AnmIns anmIns = new AnmIns();
                        anmIns.id = byteReader.readShort();
                        if (anmIns.id == -1) {
                        	byteReader.position += 6;
                        } else {
                            anmIns.length = byteReader.readShort();
                            anmIns.time = byteReader.readShort();
                            anmIns.param_mask = byteReader.readShort();
                            anmIns.data = new byte[anmIns.length - 8];
                            byteReader.readArray(anmIns.data);
                            inses.add(anmIns);
                        }
                    }
                }
                anmBean.scripts[i] = inses;
            }
            byteReader.position = anmBean.header.thtxoffset + anmBean.start;
            anmBean.thtx = new THTX();
            byteReader.readArray(anmBean.thtx.magic);
            anmBean.thtx.zero = byteReader.readShort();
            anmBean.thtx.format = byteReader.readShort();
            anmBean.thtx.w = byteReader.readShort();
            anmBean.thtx.h = byteReader.readShort();
            anmBean.thtx.size = byteReader.readInt();
            anmBean.thtx.data = new byte[anmBean.thtx.size];
            byteReader.readArray(anmBean.thtx.data);
            anmBeans.add(anmBean);
        } while (anmBeans.get(anmBeans.size() - 1).header.nextoffset != 0);
    }

}
