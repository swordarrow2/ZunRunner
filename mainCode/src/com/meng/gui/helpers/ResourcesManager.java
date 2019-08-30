package com.meng.gui.helpers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.meng.insLogic.helper.DataHelper;
import com.meng.zunRunner.anm.AnmBean;
import com.meng.zunRunner.anm.AnmFile;

import java.util.HashMap;

public final class ResourcesManager {
	public static HashMap<String, Drawable> textures = new HashMap<String, Drawable>();
	public static HashMap<String, Drawable> flipedTextures = new HashMap<String, Drawable>();
	public static HashMap<Integer, Drawable> playerDrawabls = new HashMap<>();
	public static String pathBase=null;
	
	public static void Load() {
		 loadAnm("th15_reisen.anm", "pl00");
		 loadAnm("st06enm.anm", "chunhu");
		 loadAnm("bullet.anm", "bullet");
		 loadAnm("effect.anm", "effect");
	}

	private static void loadAnm(String anmName, String spriteName) {
		AnmFile thanm = new AnmFile(anmName);
		for (AnmBean anmPart : thanm.anmBeans) {
			int[] rgbaArray;
			switch (anmPart.thtx.format) {
			case 1:
				rgbaArray = DataHelper.argb8888ToRgba8888(anmPart.thtx.data);
				break;
			case 3:
				rgbaArray = DataHelper.rgb565ToRgba8888(anmPart.thtx.data);
				break;
			case 5:
				rgbaArray = DataHelper.argb4444ToRgba8888(anmPart.thtx.data);
				break;
			case 7:
				rgbaArray = DataHelper.gray8ToRgba8888(anmPart.thtx.data);
				break;
			default:
				throw new RuntimeException("pic:" + anmPart.picName + " unexpect value:" + anmPart.thtx.format);
			}
			Pixmap pixmap = new Pixmap(anmPart.thtx.w, anmPart.thtx.h, Format.RGBA8888);
			for (int i = 0; i < rgbaArray.length; i++) {
				pixmap.drawPixel(i % anmPart.thtx.w, i / anmPart.thtx.w, rgbaArray[i]);
			}
			Texture texture = new Texture(pixmap);
			for (com.meng.zunRunner.anm.Sprite sprite : anmPart.sprites) {
				textures.put(spriteName + sprite.id, new TextureRegionDrawable(
						new TextureRegion(texture, (int) sprite.x, (int) sprite.y, (int) sprite.w, (int) sprite.h)));
			}
		}
	}
}
