package com.meng.gui.ui;

import com.meng.insLogic.helper.DataHelper;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.meng.zunRunner.anm.AnmBean;
import com.meng.zunRunner.anm.AnmFile;

public class PicScreen extends ScreenAdapter {
	public PicMain gameMain;
	public Stage stage;
	public Group groupNormal;
	private FitViewport fitViewport;
	public static AnmBean anmBean;
	public static int[] rgbaArray;
	@Override
	public void show() {
		fitViewport = new FitViewport(PicMain.width, PicMain.height);
		stage = new Stage(fitViewport, gameMain.spriteBatch);
		groupNormal = new Group();
		
		Pixmap p = new Pixmap(anmBean.thtx.w, anmBean.thtx.h, Format.RGBA8888);
		for (int i = 0; i < rgbaArray.length; i++) {
			p.drawPixel(i % anmBean.thtx.w, i / anmBean.thtx.w, rgbaArray[i]);
		}
		Image bg = new Image(new Texture(p));
		// bg.setBounds(0, 0, GameMain.width, GameMain.height);
		bg.setBounds(0, 0, anmBean.thtx.w, anmBean.thtx.h);
		stage.addActor(bg);
		super.show();
	}

	public PicScreen(PicMain game) {
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
		com.meng.zunRunner.anm.Sprite[] sprites = anmBean.sprites;
		for (com.meng.zunRunner.anm.Sprite sprite : sprites) {
			gameMain.bitmapFont.draw(gameMain.spriteBatch, sprite.id + "", sprite.x, anmBean.thtx.h - sprite.y);
		}
		gameMain.spriteBatch.end();
		super.render(delta);
	}

	@Override
	public void hide() {
		super.hide();
	}
}
