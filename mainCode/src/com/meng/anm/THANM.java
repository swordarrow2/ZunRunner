package com.meng.anm;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class THANM {

	private byte[] fileByte;
	private int position = 0;

	public ArrayList<AnmPart> anmParts = new ArrayList<>();

	public THANM(String fileName) {
		File anm = new File(GameMain.baseAnmPath + fileName);
		fileByte = new byte[(int) anm.length()];
		try {
			FileInputStream fin = new FileInputStream(anm);
			fin.read(fileByte);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		do {
		//	System.out.println("pos:" + position);
			AnmPart anmPart = new AnmPart();
			if (anmParts.size() > 0) {
				AnmPart tmPart = anmParts.get(anmParts.size() - 1);
				anmPart.start = tmPart.header.nextoffset + tmPart.start;
			}
		//	System.out.println("start:" + anmPart.start);
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
		//	System.out.println("anmPart.header.sprites:" + anmPart.header.sprites);
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
				//		System.out.println("id:" + anmIns.type);
				//		System.out.println("anmIns.length - 8:" + (anmIns.length - 8));
						anmIns.data = new byte[anmIns.length - 8];
						readByteArray(anmIns.data);
						if (anmIns.type == 200 || anmIns.type == 1 || anmIns.type == 7) {
							boolean b1 = readInt() == 0x0000ffff;
							boolean b2 = readInt() == 0x00000000;
							if (!(b1 && b2)) {
								position -= 8;
							}
							// readInt();// FF FF 00 00
							// readInt();// 00 00 00 00
							// unknown
						}
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
						if (anmIns.type == 200 || anmIns.type == 1 || anmIns.type == 7) {
							boolean b1 = readInt() != 0x0000ffff;
							boolean b2 = readInt() != 0x00000000;
							if (b1 && b2) {
								position -= 8;
							}
							// readInt();// FF FF 00 00
							// readInt();// 00 00 00 00
							// unknown
						}
						inses.add(anmIns);
					}
				}
				anmPart.scripts[i] = inses;
			}
			anmPart.thtx = new THTX();
			readByteArray(anmPart.thtx.magic);
		//	System.out.println(new String(anmPart.thtx.magic));
			anmPart.thtx.zero = readShort();
			anmPart.thtx.format = readShort();
			anmPart.thtx.w = readShort();
			anmPart.thtx.h = readShort();
			anmPart.thtx.size = readInt();
			anmPart.thtx.data = new byte[anmPart.thtx.size];
			readByteArray(anmPart.thtx.data);
			anmParts.add(anmPart);

			int[] argbArray = null;
			switch (anmPart.thtx.format) {
			case 1:
				argbArray = argb8888ToRgba8888(anmPart.thtx.data);
				break;
			case 3:
				argbArray = rgb565ToRgba8888(anmPart.thtx.data);
				break;
			case 5:
				argbArray = argb4444ToRgba8888(anmPart.thtx.data);
				break;
			case 7:
				argbArray = gray8ToRgba8888(anmPart.thtx.data);
				break;
			default:
				throw new RuntimeException("pic:" + anmPart.picName + " unexpect value:" + anmPart.thtx.format);
			}

			// Pixmap pixmap = new Pixmap(256, 256, Format.RGBA8888);
			// for (int i = 0; i < rgbaArray.length; i++) {
			// pixmap.drawPixel(i % 256, i / 256, rgbaArray[i]);
			// }
			// FightScreen.p = pixmap;

		//	argb8888ToFile(argbArray, anmPart.thtx.w, anmPart.thtx.h, anmPart.picName);

		} while (anmParts.get(anmParts.size() - 1).header.nextoffset != 0);
	}

	private int[] argb4444ToRgba8888(byte[] data) {
		int[] argbArray = new int[data.length / 2];
		for (int i = 0; i < argbArray.length; ++i) {
			int px = readShort(data, i * 2);
			int a = (px >> 12 & 0b1111) * 16;
			int r = (px >> 8 & 0b1111) * 16;
			int g = (px >> 4 & 0b1111) * 16;
			int b = (px >> 0 & 0b1111) * 16;
			argbArray[i] = (a << 24) | (r << 16) | (g << 8) | (b << 0);
		}
		return argbArray;
	}

	private int[] rgb565ToRgba8888(byte[] data) {
		int[] argbArray = new int[data.length / 2];
		for (int i = 0; i < argbArray.length; ++i) {
			int px = readShort(data, i * 2);
			int r = (px >> 11 & 0b11111) * 8;
			int g = (px >> 5 & 0b111111) * 4;
			int b = (px >> 0 & 0b11111) * 8;
			int a = 0xff;
			argbArray[i] = (a << 24) | (r << 16) | (g << 8) | (b << 0);
		}
		return argbArray;
	}

	private int[] argb8888ToRgba8888(byte[] data) {
		int[] argbArray = new int[data.length / 4];
		for (int i = 0; i < argbArray.length; ++i) {
			argbArray[i] = readInt(data, i * 4);
		}
		return argbArray;
	}

	private int[] gray8ToRgba8888(byte[] data) {
		int[] argbArray = new int[data.length];
		for (int i = 0; i < argbArray.length; ++i) {
			argbArray[i] = data[i] << 24 | 0x00FFFFFF;
		}
		return argbArray;
	}


	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (AnmPart anmPart : anmParts) {
			// stringBuilder.append(anmPart.toString()).append("\n\n\n\n");
		}
		return stringBuilder.toString();
	}
	
	private int readShort(byte[] data, int pos) {
		return (data[pos] & 0xff) | (data[pos + 1] & 0xff) << 8;
	}

	private int readInt(byte[] data, int pos) {
		return (data[pos] & 0xff) | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16
				| (data[pos + 3] & 0xff) << 24;
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
