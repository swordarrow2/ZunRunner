package com.InsProcess;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.meng.TaiHunDanmaku.baseObjects.bullets.BaseBullet;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.EnemyBullet;
import com.meng.TaiHunDanmaku.baseObjects.planes.MyPlaneReimu;
import com.meng.TaiHunDanmaku.helpers.Data;
import com.meng.TaiHunDanmaku.helpers.ObjectPools;
import com.meng.TaiHunDanmaku.helpers.ResourcesManager;
import com.meng.TaiHunDanmaku.ui.FightScreen;

public class EclBullet extends BaseBullet {
	public float centerX;
	public float centerY;
	public float offsetX;
	public float offsetY;
	// public int color;
	// public int form;
	public float directionRadians;
	public float directionRandiansSub;
	public float speed;
	public float slowestSpeed;
	public float acceleration;
	public float accelerationRadians;
	public int way;
	public int overlap;
	public int voiceOnShoot;
	public int voiceOnChangeDirection;
	public int dis;// 以boss为半径为dis的圆形边上发弹
	public int refelxCount;
	public boolean reflexTop = false;
	public boolean reflexBottom = false;
	public boolean reflexRight = false;
	public boolean reflexLeft = false;
	public int throughCount;
	public boolean throughTop = false;
	public boolean throughBottom = false;
	public boolean throughRight = false;
	public boolean throughLeft = false;
	public int canNotRemoveFrames;

	public Vector2 velocity;
	public Vector2 accelerationWithRadians;

	public static HashSet<EclBullet> instances = new HashSet<>();
	private static LinkedBlockingQueue<EclBullet> toDelete = new LinkedBlockingQueue<>();
	private static LinkedBlockingQueue<EclBullet> toAdd = new LinkedBlockingQueue<>();

	public static void create(float centerX, float centerY, float offsetX, float offsetY, int color, int form,
			float directionRadians, float directionRandiansSub, float speed, float slowestSpeed, float acceleration,
			float accelerationRadians, int way, int overlap, int voiceOnShoot, int voiceOnChangeDirection, int dis,
			int refelxCount, boolean reflexTop, boolean reflexBottom, boolean reflexRight, boolean reflexLeft,
			int throughCount, boolean throughTop, boolean throughBottom, boolean throughRight, boolean throughLeft,
			int canNotRemoveFrames) {
		ObjectPools.eclBulletPool.obtain().init(centerX, centerY, offsetX, offsetY, color, form, directionRadians,
				directionRandiansSub, speed, slowestSpeed, acceleration, accelerationRadians, way, overlap,
				voiceOnShoot, voiceOnChangeDirection, dis, refelxCount, reflexTop, reflexBottom, reflexRight,
				reflexLeft, throughCount, throughTop, throughBottom, throughRight, throughLeft, canNotRemoveFrames);
	}

	public void init(float centerX, float centerY, float offsetX, float offsetY, int color, int form,
			float directionRadians, float directionRandiansSub, float speed, float slowestSpeed, float acceleration,
			float accelerationRadians, int way, int overlap, int voiceOnShoot, int voiceOnChangeDirection, int dis,
			int refelxCount, boolean reflexTop, boolean reflexBottom, boolean reflexRight, boolean reflexLeft,
			int throughCount, boolean throughTop, boolean throughBottom, boolean throughRight, boolean throughLeft,
			int canNotRemoveFrames) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.directionRadians = directionRadians;
		this.directionRandiansSub = directionRandiansSub;
		this.way = way;
		this.overlap = overlap;
		this.voiceOnShoot = voiceOnShoot;
		this.voiceOnChangeDirection = voiceOnChangeDirection;
		this.dis = dis;
		this.refelxCount = refelxCount;
		this.reflexTop = reflexTop;
		this.reflexBottom = reflexBottom;
		this.reflexRight = reflexRight;
		this.reflexLeft = reflexLeft;
		this.throughCount = throughCount;
		this.throughTop = throughTop;
		this.throughBottom = throughBottom;
		this.throughRight = throughRight;
		this.throughLeft = throughLeft;
		this.canNotRemoveFrames = canNotRemoveFrames;

		judgeCircle = new Circle(objectCenter, Math.min(image.getWidth(), image.getHeight()) / 3);
		image.setDrawable(ResourcesManager.textures.get("bullet" + (form * 16 + color)));
		FightScreen.instence.groupNormal.addActor(image);
		image.setSize(16, 16);
		if (form == 13 || form == 14) {
			image.setSize(14, 16);
		}
		if (form == 0) {
			image.setSize(8, 8);
		}
		if (form == 8) {
			image.setSize(8, 10);
		}
		image.setZIndex(Data.zIndexEnemyBullet);

		velocity = new Vector2(0, speed).rotateRad(directionRadians);
		accelerationWithRadians = new Vector2(0, acceleration).rotateRad(accelerationRadians);
	}

	@Override
	public void judge() {
		if (getCollisionArea().contains(MyPlaneReimu.instance.objectCenter)) {
			MyPlaneReimu.instance.kill();
			kill();
		}

	}

	@Override
	public void update() {
		velocity.add(accelerationWithRadians);
		objectCenter.add(velocity);
		super.update();
	}

	public static void updateAll() {
		while (!toDelete.isEmpty()) {
			instances.remove(toDelete.poll());
		}
		while (!toAdd.isEmpty()) {
			instances.add(toAdd.poll());
		}
		for (EclBullet eclBullet : instances) {
			eclBullet.update();
		}
	}

	@Override
	public float getRotationDegree() {
		return velocity.angle() + 270;
	}

	@Override
	public Vector2 getSize() {
		return new Vector2(16, 16);
	}

}
