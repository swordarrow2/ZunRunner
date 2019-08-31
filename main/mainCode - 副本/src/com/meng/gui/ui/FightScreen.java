package com.meng.gui.ui;

import java.util.HashSet;

import com.meng.zunRunner.ecl.helper.EclBullet;
import com.meng.gui.planes.MyPlaneReimu;
import com.meng.zunRunner.ecl.EclManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.meng.gui.bullets.BaseMyBullet;
import com.meng.gui.bullets.ReflexAndThrough;
import com.meng.gui.bullets.laser.ShootLaser;
import com.meng.gui.planes.Enemy;
import com.meng.gui.planes.MapleEnemy;
import com.meng.gui.control.PlayerInputProcessor;

public class FightScreen extends ScreenAdapter {
	public static FightScreen instence;
	public GameMain gameMain;
	public int gameTimeFlag = 0;
	public Stage stage;
	public boolean onBoss = false;
	public Enemy boss;
	public MapleEnemy mapleEnemy;
	public Group groupNormal;
	public Group groupHighLight;
	public HashSet<ReflexAndThrough> reflexAndThroughs;
	private FitViewport fitViewport;
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
		// boss = new BossTaiZhang1();
		// new Enemy().init(new Vector2(275, 450), 10, 7000, new Task[]{new
		// TaskMoveTo(193, 250)});
		mapleEnemy = new MapleEnemy();
		new MyPlaneReimu().init(gameMain);
		InputMultiplexer inputManager = new InputMultiplexer();
		inputManager.addProcessor(new PlayerInputProcessor());
		Gdx.input.setInputProcessor(inputManager);
		EclManager eclManager = new EclManager("st06.helper");
		eclManager.start();
		// System.out.println(eclManager.toString());
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
		gameMain.bitmapFont.draw(gameMain.spriteBatch, "FPS:" + Gdx.graphics.getFramesPerSecond() + "\nBullets:"
				+ EclBullet.instances.size() + (boss == null ? "" : "\nHP:" + boss.hp + "\n" + "time:" + gameTimeFlag),
				20, 100);
		EclManager.updateAll();
		Enemy.updateAll();
		BaseMyBullet.updateAll();
		// EclBullet.create(96, 144, 0, 0, 5, 2, 0, 1, 0, 0);
		EclBullet.updateAll();
		ShootLaser.updateAll();
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
}
