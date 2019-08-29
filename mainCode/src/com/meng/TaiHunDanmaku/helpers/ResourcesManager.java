package com.meng.TaiHunDanmaku.helpers;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.meng.anm.AnmPart;
import com.meng.anm.THANM;

import java.util.*;

public final class ResourcesManager {
	public static HashMap<String, Drawable> textures = new HashMap<String, Drawable>();
	public static HashMap<String, Drawable> flipedTextures = new HashMap<String, Drawable>();
	public static HashMap<Integer, Drawable> playerDrawabls = new HashMap<>();

	public static void Load() {
	//	loadMyPlane("pl00");
		loadEnemy("enemy");
		loadBullets("bullet1");
		loadBoss("chunhu");
		// p:519
	}

	private static void loadMyPlane(String anmName) {
		THANM thanm = new THANM("pl00.anm");
		//THANM thanm = new THANM(anmName);
		for (AnmPart anmPart : thanm.anmParts) {
			int[] rgbaArray = null;
			switch (anmPart.thtx.format) {
			case 1:
				rgbaArray = argb8888ToRgba8888(anmPart.thtx.data);
				break;
			case 3:
				rgbaArray = rgb565ToRgba8888(anmPart.thtx.data);
				break;
			case 5:
				rgbaArray = argb4444ToRgba8888(anmPart.thtx.data);
				break;
			case 7:
				rgbaArray = gray8ToRgba8888(anmPart.thtx.data);
				break;
			default:
				throw new RuntimeException("pic:" + anmPart.picName + " unexpect value:" + anmPart.thtx.format);
			}
			Pixmap pixmap = new Pixmap(anmPart.thtx.w, anmPart.thtx.h, Format.RGBA8888);
			for (int i = 0; i < rgbaArray.length; i++) {
				pixmap.drawPixel(i % anmPart.thtx.w, i / anmPart.thtx.w, rgbaArray[i]);
			}
			Texture texture = new Texture(pixmap);
			String name=anmPart.picName;
			name=name.substring(name.lastIndexOf("/")+1,name.lastIndexOf("."));
			for (com.meng.anm.Sprite sprite : anmPart.sprite_ts) {
				textures.put(name + sprite.id, new TextureRegionDrawable(
						new TextureRegion(texture, (int) sprite.x, (int) sprite.y, (int) sprite.w, (int) sprite.h)));
			}

		}

	}

	private static void loadBoss(String name) {
		FileHandle plsanae = Gdx.files.internal("textures/enemy/" + name + ".png");
		FileHandle plsanaetxt = Gdx.files.internal(plsanae.pathWithoutExtension() + ".txt");
		String[] plsanaeWalkSheet = plsanaetxt.readString().replace("\n", " ").replace("*", " ").replace("+", " ")
				.split("\\s");
		Texture tplsanae = new Texture(plsanae);
		int textureCount = getTextureCount(plsanaetxt);
		for (int i = 0, n = 0; n < textureCount; ++i) {
			if (plsanaeWalkSheet[i].equals("Sprite:")) {
				textures.put("chunhu" + plsanaeWalkSheet[i + 1],
						new TextureRegionDrawable(new TextureRegion(tplsanae, Integer.parseInt(plsanaeWalkSheet[i + 4]),
								Integer.parseInt(plsanaeWalkSheet[i + 5]), Integer.parseInt(plsanaeWalkSheet[i + 2]),
								Integer.parseInt(plsanaeWalkSheet[i + 3]))));
				TextureRegion tr = new TextureRegion(tplsanae, Integer.parseInt(plsanaeWalkSheet[i + 4]),
						Integer.parseInt(plsanaeWalkSheet[i + 5]), Integer.parseInt(plsanaeWalkSheet[i + 2]),
						Integer.parseInt(plsanaeWalkSheet[i + 3]));
				tr.flip(true, false);
				flipedTextures.put("chunhu" + plsanaeWalkSheet[i + 1], new TextureRegionDrawable(tr));
				++n;
			}
		}
	}

	private static void loadEnemy(String name) {
		FileHandle plsanae = Gdx.files.internal("textures/enemy/" + name + ".png");
		FileHandle plsanaetxt = Gdx.files.internal(plsanae.pathWithoutExtension() + ".txt");
		String[] plsanaeWalkSheet = plsanaetxt.readString().replace("\n", " ").replace("*", " ").replace("+", " ")
				.split("\\s");
		Texture tplsanae = new Texture(plsanae);
		int textureCount = getTextureCount(plsanaetxt);
		for (int i = 0, n = 0; n < textureCount; ++i) {
			if (plsanaeWalkSheet[i].equals("Sprite:")) {
				textures.put("zayu" + plsanaeWalkSheet[i + 1],
						new TextureRegionDrawable(new TextureRegion(tplsanae, Integer.parseInt(plsanaeWalkSheet[i + 4]),
								Integer.parseInt(plsanaeWalkSheet[i + 5]), Integer.parseInt(plsanaeWalkSheet[i + 2]),
								Integer.parseInt(plsanaeWalkSheet[i + 3]))));
				TextureRegion tr = new TextureRegion(tplsanae, Integer.parseInt(plsanaeWalkSheet[i + 4]),
						Integer.parseInt(plsanaeWalkSheet[i + 5]), Integer.parseInt(plsanaeWalkSheet[i + 2]),
						Integer.parseInt(plsanaeWalkSheet[i + 3]));
				tr.flip(true, false);
				flipedTextures.put("zayu" + plsanaeWalkSheet[i + 1], new TextureRegionDrawable(tr));
				++n;
			}
		}
	}

	private static void loadBullets(String name) {
		FileHandle plsanae = Gdx.files.internal("textures/bullet/" + name + ".png");
		FileHandle plsanaetxt = Gdx.files.internal(plsanae.pathWithoutExtension() + ".txt");
		String[] plsanaeWalkSheet = plsanaetxt.readString().replace("\n", " ").replace("*", " ").replace("+", " ")
				.split("\\s");
		Texture tplsanae = new Texture(plsanae);
		int textureCount = getTextureCount(plsanaetxt);
		for (int i = 0, n = 0; n < textureCount; ++i) {
			if (plsanaeWalkSheet[i].equals("Sprite:")) {
				textures.put("bullet" + plsanaeWalkSheet[i + 1],
						new TextureRegionDrawable(new TextureRegion(tplsanae, Integer.parseInt(plsanaeWalkSheet[i + 4]),
								Integer.parseInt(plsanaeWalkSheet[i + 5]), Integer.parseInt(plsanaeWalkSheet[i + 2]),
								Integer.parseInt(plsanaeWalkSheet[i + 3]))));
				++n;
			}
		}
	}

	private static int getTextureCount(FileHandle txt) {
		String str = txt.readString();
		String flagStr = "Sprite:";
		String newStr = str.replace(flagStr, "");
		return (str.length() - newStr.length()) / flagStr.length();
	}

	private static int[] argb4444ToRgba8888(byte[] data) {
		int[] argbArray = new int[data.length / 2];
		for (int i = 0; i < argbArray.length; ++i) {
			int px = readShort(data, i * 2);
			int a = (px >> 12 & 0b1111) * 17;
			int r = (px >> 8 & 0b1111) * 17;
			int g = (px >> 4 & 0b1111) * 17;
			int b = (px >> 0 & 0b1111) * 17;
			argbArray[i] = (r << 24) | (g << 16) | (b << 8) | (a << 0);
		}
		return argbArray;
	}

	private static int[] rgb565ToRgba8888(byte[] data) {
		int[] argbArray = new int[data.length / 2];
		for (int i = 0; i < argbArray.length; ++i) {
			int px = readShort(data, i * 2);
			int r = (px >> 11 & 0b11111) * 8;
			int g = (px >> 5 & 0b111111) * 4;
			int b = (px >> 0 & 0b11111) * 8;
			int a = 0xff;
			argbArray[i] = (r << 24) | (g << 16) | (b << 8) | (a << 0);
		}
		return argbArray;
	}

	private static int[] argb8888ToRgba8888(byte[] data) {
		int[] argbArray = new int[data.length / 4];
		for (int i = 0; i < argbArray.length; ++i) {
			int px = readInt(data, i * 4);
			px = (px << 8) | (px >> 24 & 0xff);
			argbArray[i] = px;
		}
		return argbArray;
	}

	private static int[] gray8ToRgba8888(byte[] data) {
		int[] argbArray = new int[data.length];
		for (int i = 0; i < argbArray.length; ++i) {
			argbArray[i] = data[i] | 0xFFFFFF00;
		}
		return argbArray;
	}

	private static int readShort(byte[] data, int pos) {
		return (data[pos] & 0xff) | (data[pos + 1] & 0xff) << 8;
	}

	private static int readInt(byte[] data, int pos) {
		return (data[pos] & 0xff) | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16
				| (data[pos + 3] & 0xff) << 24;
	}

}
