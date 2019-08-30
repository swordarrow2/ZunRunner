package com.meng.zunRunner.fmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.meng.gui.helpers.ResourcesManager;

public class FmtFile {
	private int position = 0;
	private byte[] fileByte;
	public MusicInfo[] musicInfos;
	public String[] names;

	public FmtFile(String fileName) {
		fileByte = Gdx.files.absolute(ResourcesManager.pathBase + "fmt/" + fileName).readBytes();
		musicInfos = new MusicInfo[(int) (fileByte.length / 52)];
		names = new String[musicInfos.length];

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
		try {
			String str;
			InputStream fis = Gdx.files.absolute(ResourcesManager.pathBase + "fmt/" + fileName).read();
			InputStreamReader isr = new InputStreamReader(fis, "shift_jis");
			BufferedReader br = new BufferedReader(isr);
			while ((str = br.readLine()) != null) {
				if (str.startsWith("@bgm/")) {
					String music = str.substring(5);
					if (!music.endsWith(".wav")) {
						music += ".wav";
					}
					for (int i = 0; i < names.length; ++i) {
						if (music.equals(names[i])) {
							str = br.readLine();
							str = str.substring(5);
							names[i] = str.substring(str.indexOf(" ") + 1);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		fileByte = null;
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
		// public int beanSize = 52;
	}

	private short readShort() {
		return (short) (fileByte[position++] & 0xff | (fileByte[position++] & 0xff) << 8);
	}

	private int readInt() {
		return (fileByte[position++] & 0xff) | (fileByte[position++] & 0xff) << 8 | (fileByte[position++] & 0xff) << 16
				| (fileByte[position++] & 0xff) << 24;
	}

	private byte[] readName() {
		byte[] ba = new byte[16];
		for (int i = 0; i < ba.length; ++i) {
			ba[i] = fileByte[position++];
		}
		return ba;
	}
}
