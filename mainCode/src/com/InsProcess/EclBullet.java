package com.InsProcess;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.baseObjects.bullets.BaseBullet;
import com.meng.TaiHunDanmaku.baseObjects.planes.Enemy;
import com.meng.TaiHunDanmaku.baseObjects.planes.MyPlane;
import com.meng.TaiHunDanmaku.helpers.Data;
import com.meng.TaiHunDanmaku.helpers.ObjectPools;
import com.meng.TaiHunDanmaku.helpers.ResourcesManager;
import com.meng.TaiHunDanmaku.ui.FightScreen;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class EclBullet extends BaseBullet {
	public Enemy enemy;
	private float directionAngle;
	public float speed;
	private float acceleration;
	private float accelerationAngle;
	private int voiceOnShoot;
	private int voiceOnChangeDirection;
	public int refelxCount = 0;
	public boolean reflexTop = false;
	public boolean reflexBottom = false;
	public boolean reflexRight = false;
	public boolean reflexLeft = false;
	public int throughCount = 0;
	public boolean throughTop = false;
	public boolean throughBottom = false;
	public boolean throughRight = false;
	public boolean throughLeft = false;
	public int canNotRemoveFrames = 0;

	public Vector2 velocity;
	public Vector2 accelerationWithAngle = new Vector2();

	public static HashSet<EclBullet> instances = new HashSet<>();
	private static LinkedBlockingQueue<EclBullet> toDelete = new LinkedBlockingQueue<>();
	private static LinkedBlockingQueue<EclBullet> toAdd = new LinkedBlockingQueue<>();

	private ChangeTaskManager changeTaskManager;

	private float speedSub;
	private float targetSpeed;

	private float dirSub;
	private float targetDir;

	public static void create(Enemy enemy,float centerX, float centerY, float offsetX, float offsetY, int form, int color,
			float directionAngle, float speed, int voiceOnShoot, int voiceOnChangeDirection, ChangeTask[] tasks) {
		ObjectPools.eclBulletPool.obtain().init(enemy,centerX, centerY, offsetX, offsetY, form, color,
				directionAngle - 1.5707963267948966f, speed, voiceOnShoot, voiceOnChangeDirection, tasks);
	}

	public void init(Enemy enemy,float centerX, float centerY, float offsetX, float offsetY, int form, int color,
			float directionAngle, float speed, int voiceOnShoot, int voiceOnChangeDirection, ChangeTask[] tasks) {
		super.init();
		changeTaskManager = new ChangeTaskManager(this);
		this.enemy=enemy;
		for (int i = 0, tasksLength = tasks.length; i < tasksLength; i++) {
			changeTaskManager.addChange(i, tasks[i]);
		}
		changeTaskManager.end();
		this.directionAngle = directionAngle;
		this.speed = speed;
		this.voiceOnShoot = voiceOnShoot;
		this.voiceOnChangeDirection = voiceOnChangeDirection;
		objectCenter.set(centerX + offsetX, centerY + offsetY);
		judgeCircle = new Circle(objectCenter, Math.min(image.getWidth(), image.getHeight()) / 3);
		image.setDrawable(ResourcesManager.textures.get("bullet" + ((form << 4) + color)));
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
		velocity = new Vector2(0, speed).rotateRad(directionAngle);
		image.setRotation(getRotationDegree());
		toAdd.add(this);
		speedSub = 0;
		targetSpeed = 0;
		dirSub = 0;
		targetDir = 0;
	}

	public void setTargetSpeed(float targetSpeed, int frame) {
		this.targetSpeed = targetSpeed;
		speedSub = (targetSpeed - speed) / frame;
	}

	public void setTargetDir(float targetDir) {
		this.targetDir = targetDir;
		directionAngle+=targetDir;
		velocity.setAngleRad(directionAngle);
	}
	
	 public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
		accelerationWithAngle=new Vector2(0, acceleration);
	}
	 public void setAccelerationAngle(float accelerationAngle) {
		this.accelerationAngle = accelerationAngle;
		accelerationWithAngle.setAngleRad(accelerationAngle);
	}
	@Override
	public void judge() {
		if (getCollisionArea().contains(MyPlane.instance.objectCenter)) {
			MyPlane.instance.kill();
			kill();
		}
	}

	@Override
	public void kill() {
		ObjectPools.eclBulletPool.free(this);
		toDelete.add(this);
		image.remove();
	}

	@Override
	public void update() {
		velocity.add(accelerationWithAngle);
		objectCenter.add(velocity);
		changeTaskManager.update();
		if (Math.abs(speed - targetSpeed) > 0.0001f) {
			speed += speedSub;
			velocity.nor().scl(speed);
		}
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
