package com.meng.thbgm.fileRead;

import java.io.File;
import java.io.FileInputStream;

public class TH10fmt {
    private int position = 0;
    private byte[] fileByte;
    public MusicInfo[] musicInfos;

    public TH10fmt(File file) {
        if (!file.exists()) {
            throw new RuntimeException("file not found:" + file.getAbsolutePath());
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileByte = new byte[(int) file.length()];
            fileInputStream.read(fileByte);
        } catch (Exception e) {
            throw new RuntimeException("read file failed:" + file.getAbsolutePath());
        }
        musicInfos = new MusicInfo[(int) (file.length() / 52)];
    }

    public void load() {
        for (int i = 0; i < musicInfos.length; ++i) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.name = readName();
            musicInfo.start = readInt();
            musicInfo.unknown1 = readInt();
            musicInfo.repeatStart = readInt();
            musicInfo.end = readInt();
            musicInfo.format = readShort();
            musicInfo.channels = readShort();
            musicInfo.rate = readInt();
            musicInfo.unknown2 = readInt();
            musicInfo.unknown3 = readInt();
            musicInfo.zero = readInt();
            musicInfos[i] = musicInfo;
        }
    }

    public class MusicInfo {
        public byte[] name;
        public int start;
        public int unknown1;
        public int repeatStart;
        public int end;
        public short format;
        public short channels;
        public int rate;
        public int unknown2;
        public int unknown3;
        public int zero;
        public int beanSize = 52;
    }

    private short readShort() {
        return (short) (fileByte[position++] & 0xff | (fileByte[position++] & 0xff) << 8);
    }

    private int readInt() {
        return (fileByte[position++] & 0xff) | (fileByte[position++] & 0xff) << 8 | (fileByte[position++] & 0xff) << 16 | (fileByte[position++] & 0xff) << 24;
    }

    private byte[] readName() {
        byte[] ba = new byte[16];
        for (int i = 0; i < ba.length; ++i) {
            ba[i] = fileByte[position++];
        }
        return ba;
    }
}
