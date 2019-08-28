package com.meng.anm;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.InsProcess.parse.EclManager;
import com.InsProcess.parse.beans.EclHeader;
import com.InsProcess.parse.beans.EclIncludeList;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class THANM {

	private byte[] fileByte;
	private int position = 0;

	private String name;

	public ArrayList<AnmPart> anmParts = new ArrayList<>();

	// repeat{
	// header
	// sprites' data offsets
	// anmIns' data offsets
	// pic name
	// sprite data
	// script data
	// THTX
	// }

	public THANM(String fileName) {
		name = fileName;
		File ecl = new File(GameMain.baseAnmPath + fileName);
		fileByte = new byte[(int) ecl.length()];
		try {
			FileInputStream fin = new FileInputStream(ecl);
			fin.read(fileByte);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		// do {
		// System.out.println("pos:" + position);
		AnmPart anmPart = new AnmPart();
		if (anmParts.size() > 0) {
			AnmPart tmPart = anmParts.get(anmParts.size() - 1);
			anmPart.start = tmPart.header.nextoffset + tmPart.start;
		}
		System.out.println("start:" + anmPart.start);
		anmPart.header = new AnmHeader();
		anmPart.header.version = readInt();
		anmPart.header.sprites = readShort();
		anmPart.header.scripts = readShort();
		anmPart.header.zero1 = readShort();
		anmPart.header.w = readShort();
		anmPart.header.h = readShort();
		anmPart.header.format = readShort();
		anmPart.header.nameoffset = readInt();
		anmPart.header.x = readShort();
		anmPart.header.y = readShort();
		anmPart.header.memorypriority = readInt();
		anmPart.header.thtxoffset = readInt();
		anmPart.header.hasdata = readShort();
		anmPart.header.lowresscale = readShort();
		anmPart.header.nextoffset = readInt();
		readIntArray(anmPart.header.zero2);
		// System.out.println("anmPart.header.sprites:" +
		// anmPart.header.sprites);
		anmPart.spriteOffset = new int[anmPart.header.sprites];
		for (int i = 0; i < anmPart.spriteOffset.length; ++i) {
			anmPart.spriteOffset[i] = readInt();
		}
		AnmInsOffset[] anmInsOffset = new AnmInsOffset[anmPart.header.scripts];
		for (int i = 0; i < anmInsOffset.length; ++i) {
			AnmInsOffset ais = new AnmInsOffset();
			ais.id = readInt();
			ais.offset = readInt();
			anmInsOffset[i] = ais;
		}
		byte[] bs = new byte[anmPart.spriteOffset[0] - anmPart.header.nameoffset];
		readByteArray(bs);
		anmPart.picName = new String(bs);
		anmPart.picName = anmPart.picName.substring(0, anmPart.picName.indexOf(0));
		anmPart.sprite_ts = new Sprite[anmPart.header.sprites];
		for (int i = 0; i < anmPart.sprite_ts.length; ++i) {
			Sprite sprite = new Sprite();
			sprite.id = readInt();
			sprite.x = readFloat();
			sprite.y = readFloat();
			sprite.w = readFloat();
			sprite.h = readFloat();
			anmPart.sprite_ts[i] = sprite;
		}
		anmPart.scripts = new ArrayList[anmPart.header.scripts];
		for (int i = 0; i < anmPart.scripts.length; ++i) {
			ArrayList<AnmIns> inses = new ArrayList<>();
			if (i < anmInsOffset.length - 2) {
				while (position < anmInsOffset[i + 1].offset + anmPart.start) {
					AnmIns anmIns = new AnmIns();
					anmIns.type = readShort();
					anmIns.length = readShort();
					anmIns.time = readShort();
					anmIns.param_mask = readShort();
					// System.out.println("anmIns.length - 8:" + (anmIns.length
					// - 8));
					anmIns.data = new byte[anmIns.length - 8];
					readByteArray(anmIns.data);
					if (anmIns.type == 200 || anmIns.type == 1) {
						boolean b1 = readInt() != 0x0000ffff;
						boolean b2 = readInt() != 0x00000000;
						if (b1 && b2) {
							position -= 8;
						}
						// readInt();// FF FF 00 00
						// readInt();// 00 00 00 00
						// unknown
					}
					// System.out.println("anmIns:" + anmIns.toString());
					inses.add(anmIns);
				}
			} else {
				while (position < anmPart.header.thtxoffset + anmPart.start) {
					AnmIns anmIns = new AnmIns();
					anmIns.type = readShort();
					anmIns.length = readShort();
					anmIns.time = readShort();
					anmIns.param_mask = readShort();
					anmIns.data = new byte[anmIns.length - 8];
					readByteArray(anmIns.data);
					if (anmIns.type == 200 || anmIns.type == 1) {
						boolean b1 = readInt() != 0x0000ffff;
						boolean b2 = readInt() != 0x00000000;
						if (b1 && b2) {
							position -= 8;
						}
						// readInt();// FF FF 00 00
						// readInt();// 00 00 00 00
						// unknown
					}
					// System.out.println("anmIns:" + anmIns.toString());
					inses.add(anmIns);
				}
			}
			anmPart.scripts[i] = inses;
		}
		anmPart.thtx = new THTX();
		readByteArray(anmPart.thtx.magic);
		anmPart.thtx.zero = readShort();
		anmPart.thtx.format = readShort();
		anmPart.thtx.w = readShort();
		anmPart.thtx.h = readShort();
		anmPart.thtx.size = readInt();
		anmPart.thtx.data = new byte[anmPart.thtx.size];
		readByteArray(anmPart.thtx.data);
		anmParts.add(anmPart);

		for (int i = 0; i < anmPart.thtx.data.length; i+=4) {
			byte r = (byte) ((((anmPart.thtx.data[i] & 0xff000000) >> 24) + 8) / 17);
			byte g = (byte) ((((anmPart.thtx.data[i] & 0xff0000) >> 16) + 8) / 17);
			byte b = (byte) ((((anmPart.thtx.data[i] & 0xff00) >> 8) + 8) / 17);
			byte a = (byte) ((((anmPart.thtx.data[i] & 0xff)) + 8) / 17);

			anmPart.thtx.data[i] = b;
			anmPart.thtx.data[i+1] = g;
			anmPart.thtx.data[i+2] = r;
			anmPart.thtx.data[i+3] = a;
		}
		

		// } while (anmParts.get(anmParts.size() - 1).header.nextoffset != 0);

	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (AnmPart anmPart : anmParts) {
			stringBuilder.append(anmPart.toString()).append("\n\n\n\n");
		}
		return stringBuilder.toString();
	}

	private byte readByte() {
		return fileByte[position++];
	}

	private short readShort() {
		return (short) (fileByte[position++] & 0xff | (fileByte[position++] & 0xff) << 8);
	}

	private int readInt() {
		return (fileByte[position++] & 0xff) | (fileByte[position++] & 0xff) << 8 | (fileByte[position++] & 0xff) << 16
				| (fileByte[position++] & 0xff) << 24;
	}

	private float readFloat() {
		return Float.intBitsToFloat(readInt());
	}

	private void readIntArray(int[] arr) {
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = readInt();
		}
	}

	private void readByteArray(byte[] arr) {
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = readByte();
		}
	}

}
