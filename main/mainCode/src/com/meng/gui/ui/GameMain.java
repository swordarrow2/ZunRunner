package com.meng.gui.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.meng.gui.helpers.*;

public class GameMain extends Game {
    public SpriteBatch spriteBatch;
    public BitmapFont bitmapFont; 
    public static int difficulty = 3; //0-E 1-N 2-H 3-L

    public static final int width = 386;
    public static final int height = 450;

    public int miss;

    public GameMain() {
	  }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        ResourcesManager.Load();
        bitmapFont = new BitmapFont(Gdx.files.absolute(ResourcesManager.pathBase+"font/font.fnt"));
        bitmapFont.setColor(Color.RED);
        setScreen(new FightScreen(this));
	  }

    @Override
    public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //将屏幕清空为黑色，随后绘制当前活跃Screen上的内容。
        //颜色可以修改，glClearColor前三个参数分别为红绿蓝，为0~1之间的float型
		super.render();
	  }
  }
