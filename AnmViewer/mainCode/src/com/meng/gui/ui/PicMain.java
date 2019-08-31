package com.meng.gui.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class PicMain extends Game {
	public SpriteBatch spriteBatch;
	public BitmapFont bitmapFont;
	public static String baseEclPath = null;

	public static int width = 512;
	public static int height = 512;
	
	public static String anmPath=null;
	
	public PicMain() {
	}

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.RED);

		setScreen(new PicScreen(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// 将屏幕清空为黑色，随后绘制当前活跃Screen上的内容。
		// 颜色可以修改，glClearColor前三个参数分别为红绿蓝，为0~1之间的float型
		super.render();
	}
}
