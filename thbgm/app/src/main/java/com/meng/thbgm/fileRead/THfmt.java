package com.meng.thbgm.fileRead;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class THfmt {
    private int position = 0;
    private byte[] fileByte;
    public MusicInfo[] musicInfos;
    public String[] names;
    private File fmt;

    public THfmt(File file) {
        if (!file.exists()) {
            throw new RuntimeException("file not found:" + file.getAbsolutePath());
        }
        fmt = file;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileByte = new byte[(int) file.length()];
            fileInputStream.read(fileByte);
        } catch (Exception e) {
            throw new RuntimeException("read file failed:" + file.getAbsolutePath());
        }
        musicInfos = new MusicInfo[(int) (file.length() / 52)];
        names = new String[musicInfos.length];
    }

    public void load() {
        for (int i = 0; i < musicInfos.length; ++i) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.name = readName();
            musicInfo.start = readInt();
            musicInfo.unknown1 = readInt();
            musicInfo.repeatStart = readInt();
            musicInfo.length = readInt();
            musicInfo.format = readShort();
            musicInfo.channels = readShort();
            musicInfo.rate = readInt();
            musicInfo.avgBytesPerSec = readInt();
            musicInfo.blockAlign = readShort();
            musicInfo.bitsPerSample = readShort();
            musicInfo.cbSize = readShort();
            musicInfo.pad = readShort();
            String name = new String(musicInfo.name);
            names[i] = name.substring(0, name.indexOf(0));
            musicInfos[i] = musicInfo;
        }
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
        try {
            String str = "";
            String str1 = "";
            fis = new FileInputStream(new File(fmt.getParent() + "/musiccmt.txt"));
            isr = new InputStreamReader(fis,"shift_jis");
            br = new BufferedReader(isr);

            while ((str = br.readLine()) != null) {
                if (str.startsWith("@bgm/")) {
                    String music = str.substring(5);
                    music += ".wav";
                    for (int i = 0; i < names.length; ++i) {
                        if (music.equals(names[i])) {
                            str = br.readLine();
                            names[i] = str.substring(str.indexOf("  ") + 2);
                            break;
                        }
                    }
                }
            }
            // 当读取的一行不为空时,把读到的str的值赋给str1
            System.out.println(str1);// 打印出str1
        } catch (FileNotFoundException e) {
            System.out.println("找不到指定文件");
        } catch (IOException e) {
            System.out.println("读取文件失败");
        } finally {
            try {
                br.close();
                isr.close();
                fis.close();
                // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class MusicInfo {
        public byte[] name;
        public int start;
        public int unknown1;
        public int repeatStart;
        public int length;
        public short format;
        public short channels;
        public int rate;
        public int avgBytesPerSec;
        public short blockAlign;
        public short bitsPerSample;
        public short cbSize;
        public short pad;
        //    public int beanSize = 52;
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
