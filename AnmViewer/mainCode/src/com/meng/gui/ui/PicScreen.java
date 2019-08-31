package com.meng.gui.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.*;
import com.meng.*;
import com.meng.zunRunner.anm.*;
import com.meng.zunRunner.anm.beans.*;

public class PicScreen extends ScreenAdapter {
    private PicMain gameMain;
    private Stage stage;
    private FitViewport fitViewport;
    private AnmBean anmBean;
	private MyRectangle[] rects;
	
    @Override
    public void show() {
        anmBean = new AnmFile().anmBeans.get(0);
        int[] rgbaArray;
        switch (anmBean.thtx.format) {
            case 1:
                rgbaArray = DataHelper.argb8888ToRgba8888(anmBean.thtx.data);
                break;
            case 3:
                rgbaArray = DataHelper.rgb565ToRgba8888(anmBean.thtx.data);
                break;
            case 5:
                rgbaArray = DataHelper.argb4444ToRgba8888(anmBean.thtx.data);
                break;
            case 7:
                rgbaArray = DataHelper.gray8ToRgba8888(anmBean.thtx.data);
                break;
            default:
                throw new RuntimeException("pic:" + anmBean.picName + " unexpect value:" + anmBean.thtx.format);
        }
		PicMain.width=anmBean.thtx.w;
		PicMain.height=anmBean.thtx.h;
		fitViewport = new FitViewport(PicMain.width, PicMain.height);
        stage = new Stage(fitViewport, gameMain.spriteBatch);
		Pixmap p = new Pixmap(anmBean.thtx.w, anmBean.thtx.h, Format.RGBA8888);
        for (int i = 0; i < rgbaArray.length; i++) {
            p.drawPixel(i % anmBean.thtx.w, i / anmBean.thtx.w, rgbaArray[i]);
        }
        Image bg = new Image(new Texture(p));
        bg.setBounds(0, 0, anmBean.thtx.w, anmBean.thtx.h);
        stage.addActor(bg);
//        FmtFile fmtFile=new FmtFile("th10.fmt");
//        WavFile wavFile=new WavFile("", fmtFile);
		rects=new MyRectangle[anmBean.sprites.length];
		Sprite[] sprites = anmBean.sprites;
		int i=0;
		for (Sprite sprite : sprites) {
            MyRectangle mr=new MyRectangle(sprite.x,anmBean.thtx.h - sprite.y,sprite.w,sprite.h);
			stage.addActor(mr);
			rects[i++]=mr;
			if(i>3){
			//  break;
			}
		  }
		
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
        Sprite[] sprites = anmBean.sprites;
		
        for (Sprite sprite : sprites) {
            gameMain.bitmapFont.draw(gameMain.spriteBatch, sprite.id + "", sprite.x, anmBean.thtx.h - sprite.y);
			
			//gameMain.spriteBatch.draw
        }
        gameMain.spriteBatch.end();
        super.render(delta);
    }

    @Override
    public void hide() {
        super.hide();
    }
}
