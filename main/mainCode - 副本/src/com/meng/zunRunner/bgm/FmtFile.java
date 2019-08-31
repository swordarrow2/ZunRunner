package com.meng.zunRunner.bgm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.badlogic.gdx.Gdx;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.zunRunner.ByteReader;

public class FmtFile {
	public MusicInfo[] musicInfos;
	public String[] names;

	public FmtFile(String fileName) {
		ByteReader byteReader=new ByteReader(Gdx.files.absolute(ResourcesManager.pathBase + "fmt/" + fileName).readBytes());
		musicInfos = new MusicInfo[(int) (byteReader.fileByte.length / 52)];
		names = new String[musicInfos.length];

		for (int i = 0; i < musicInfos.length; ++i) {
			MusicInfo musicInfo = new MusicInfo();
			byteReader.readArray(musicInfo.name);
			musicInfo.start = byteReader.readInt();
			musicInfo.unknown1 = byteReader.readInt();
			musicInfo.repeatStart = byteReader.readInt();
			musicInfo.length = byteReader.readInt();
			musicInfo.format = byteReader.readShort();
			musicInfo.channels = byteReader.readShort();
			musicInfo.rate = byteReader.readInt();
			musicInfo.avgBytesPerSec = byteReader.readInt();
			musicInfo.blockAlign = byteReader.readShort();
			musicInfo.bitsPerSample = byteReader.readShort();
			musicInfo.cbSize = byteReader.readShort();
			musicInfo.pad = byteReader.readShort();
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
	}

	public class MusicInfo {
		public byte[] name = new byte[16];
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
}
