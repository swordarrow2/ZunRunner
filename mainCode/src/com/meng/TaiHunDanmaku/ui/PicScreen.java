package com.meng.TaiHunDanmaku.ui;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.meng.anm.AnmPart;
import com.meng.anm.THANM;

public class PicScreen extends ScreenAdapter {
	public GameMain gameMain;
	public Stage stage;
	public Group groupNormal;
	private FitViewport fitViewport;
	private THANM thanm;

	@Override
	public void show() {
		fitViewport = new FitViewport(512, 512);
		stage = new Stage(fitViewport, gameMain.spriteBatch);
		groupNormal = new Group();
		thanm = new THANM("pl03.anm");
		AnmPart anmPart = thanm.anmParts.get(0);
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
		Pixmap p = new Pixmap(anmPart.thtx.w, anmPart.thtx.h, Format.RGBA8888);
		for (int i = 0; i < rgbaArray.length; i++) {
			p.drawPixel(i % anmPart.thtx.w, i / anmPart.thtx.w, rgbaArray[i]);
		}
		Image bg = new Image(new Texture(p));
		// bg.setBounds(0, 0, GameMain.width, GameMain.height);
		bg.setBounds(0, 0, anmPart.thtx.w, anmPart.thtx.h);
		stage.addActor(bg);
		super.show();
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

	public PicScreen(GameMain game) {
		gameMain = game;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		fitViewport.update(width, height);
	}

	@Override
	public void render(float delta) {
		stage.draw();
		gameMain.spriteBatch.begin();
		com.meng.anm.Sprite[] sprites = thanm.anmParts.get(0).sprite_ts;
		for (com.meng.anm.Sprite sprite : sprites) {
			gameMain.bitmapFont.draw(gameMain.spriteBatch, sprite.id + "", sprite.x,
					thanm.anmParts.get(0).thtx.h - sprite.y);
		}
		gameMain.spriteBatch.end();
		super.render(delta);
	}

	@Override
	public void hide() {
		super.hide();
	}
}
