package com.meng.TaiHunDanmaku.ui;

import com.InsProcess.parse.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.*;
import com.meng.TaiHunDanmaku.baseObjects.bullets.*;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.*;
import com.meng.TaiHunDanmaku.control.*;
import com.meng.TaiHunDanmaku.task.*;

import java.io.*;
import java.util.*;

public class FightScreen extends ScreenAdapter {
    public static FightScreen instence;
    public GameMain gameMain;
    public int gameTimeFlag = 0;
    public Stage stage;
    public Group groupNormal;
    public Group groupHighLight;
    public Junko boss;
    public HashSet<ReflexAndThrough> reflexAndThroughs;
    private FitViewport fitViewport;
    public static String nowins = "111";
    private final Actor changeBlend1 = new Actor() {
        @Override
        public void draw(Batch batch, float parentAlpha) {
            gameMain.spriteBatch.end();
            gameMain.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            gameMain.spriteBatch.begin();
        }
    };

    private Actor changeBlend2 = new Actor() {
        @Override
        public void draw(Batch batch, float parentAlpha) {
            gameMain.spriteBatch.end();
            gameMain.spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            gameMain.spriteBatch.begin();
        }
    };

    @Override
    public void show() {
        instence = this;
        reflexAndThroughs = new HashSet<>();
        fitViewport = new FitViewport(GameMain.width, GameMain.height);
        stage = new Stage(fitViewport, gameMain.spriteBatch);
        groupNormal = new Group();
        groupHighLight = new Group();
        // groupHighLight.addActor(laserManager);
        Pixmap p = new Pixmap(1, 1, Pixmap.Format.RGB565);


        p.setColor(Color.DARK_GRAY);
        p.fill();
        Image bg = new Image(new Texture(p));
        bg.setBounds(0, 0, GameMain.width, GameMain.height);
        stage.addActor(bg);
        stage.addActor(groupNormal);
        stage.addActor(changeBlend1);
        stage.addActor(groupHighLight);
        stage.addActor(changeBlend2);
        //      boss = new BossTaiZhang1();
        new Junko().init(new Vector2(275, 450), 10, 7000, new Task[]{new TaskMoveTo(193, 250)});

        new MyPlaneReimu().init(gameMain);
        InputMultiplexer inputManager = new InputMultiplexer();
        inputManager.addProcessor(new PlayerInputProcessor());
        Gdx.input.setInputProcessor(inputManager);
        EclManager eclManager = new EclManager("st06.ecl");
        eclManager.start();
        System.out.println(eclManager.toString());
		/*EclManager eclManager = new EclManager();
		Sub card7 = eclManager.sub();
        card7.parse(read("BossCard7.txt"));
		Sub sub1 = eclManager.sub();
		sub1.parse(read("BossCard7_at.txt"));
		Sub  sub2 = eclManager.sub();
		sub2.parse(read("BossCard7_at2.txt"));	
		Sub sub2b = eclManager.sub();
		sub2b.parse(read("BossCard7_at2b.txt"));		
		Sub  sub3 = eclManager.sub();
		sub3.parse(read("BossCard7_at3.txt"));
		Sub sub3b = eclManager.sub();
	    sub3b.parse(read("BossCard7_at3b.txt"));		
        Sub sub4 = eclManager.sub();
		sub4.parse(read("BossCard7_at4.txt"));
		card7.start();
		*/
        super.show();
    }

    public FightScreen(GameMain game) {
        gameMain = game;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        fitViewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        ++gameTimeFlag;
        stage.draw();
        gameMain.spriteBatch.begin();
        gameMain.bitmapFont.draw(gameMain.spriteBatch, "FPS:" + Gdx.graphics.getFramesPerSecond() + "\nBullets:" + EnemyBullet.instances.size() + (boss == null ? "" : "\nHP:" + boss.hp), 20, 100);
        gameMain.bitmapFont.draw(gameMain.spriteBatch, nowins, 20, 150);

        if (boss == null) {
        } else {
            boss.update();
        }
        EclManager.update();
        BulletShooter.updateAll();
        BaseMyBullet.updateAll();
        EnemyBullet.updateAll();
        MyPlaneReimu.instance.update();
        gameMain.spriteBatch.end();
        for (ReflexAndThrough reflexAndThrough : reflexAndThroughs) {
            reflexAndThrough.update();
        }
        super.render(delta);
    }

    @Override
    public void hide() {
        super.hide();
    }

    public String read(String path) {
        String s = "";
        File eclFile = new File(GameMain.baseEclPath + path);
        try {
            if (!eclFile.exists()) {
                throw new NullPointerException("file not found:" + eclFile.getAbsolutePath());
            }
            long filelength = eclFile.length();
            byte[] filecontent = new byte[(int) filelength];
            FileInputStream in = new FileInputStream(eclFile);
            in.read(filecontent);
            in.close();
            s = new String(filecontent, "Shift_JIS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

}
