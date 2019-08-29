package com.meng.gui.bullets.laser;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.meng.insLogic.ChangeTask;
import com.meng.insLogic.ChangeTaskManager;
import com.meng.insLogic.EclBullet;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.meng.gui.planes.*;
import com.meng.gui.helpers.*;
import com.meng.gui.ui.*;

public class ShootLaser extends EclBullet {

	public float finalLength;
	public float width;
	public Color color = new Color(Color.WHITE);
	public Color rayColor = new Color(Color.WHITE);
	public float degrees;
	public Image begin1 = new Image();
	public Image mid1 = new Image();
	public Vector2 p1 = new Vector2();
	public Vector2 p2 = new Vector2();

	private float directionAngle;
	public float speed;
	private float acceleration;
	private float accelerationAngle;
	private int voiceOnShoot;
	private int voiceOnChangeDirection;
	public int canNotRemoveFrames = 0;

	public Vector2 velocity;
	public Vector2 accelerationWithAngle = new Vector2();

	public static HashSet<ShootLaser> instances = new HashSet<>();
	private static LinkedBlockingQueue<ShootLaser> toDelete = new LinkedBlockingQueue<>();
	private static LinkedBlockingQueue<ShootLaser> toAdd = new LinkedBlockingQueue<>();

	private ChangeTaskManager changeTaskManager;

	private float speedSub;
	private float targetSpeed;

	private float dirSub;
	private float targetDir;

	public ShootLaser() {

	}

	public static void create(Enemy enemy, Drawable image, float centerX, float centerY, float createLength,
			float finalLength, float width, float directionAngle, float speed, int voiceOnShoot,
			int voiceOnChangeDirection, ChangeTask[] tasks, int nowTask) {
		ObjectPools.shootLaserPool.obtain().init(enemy, image, centerX, centerY, createLength, finalLength, width,
				directionAngle, speed, voiceOnShoot, voiceOnChangeDirection, tasks, nowTask);
	}

	public void init(Enemy enemy, Drawable image, float centerX, float centerY, float createLength, float finalLength,
			float width, float directionAngle, float speed, int voiceOnShoot, int voiceOnChangeDirection,
			ChangeTask[] tasks, int nowTask) {
		existTime = 0;
		changeTaskManager = new ChangeTaskManager(this);
		this.enemy = enemy;
		for (int i = 0, tasksLength = tasks.length; i < tasksLength; i++) {
			changeTaskManager.addChange(i, tasks[i]);
		}
		changeTaskManager.end();
		changeTaskManager.nowTask = nowTask;
		this.directionAngle = directionAngle;
		this.speed = speed;
		this.voiceOnShoot = voiceOnShoot;
		this.voiceOnChangeDirection = voiceOnChangeDirection;
		objectCenter.set(centerX, centerY);
		velocity = new Vector2(0, speed).rotateRad(directionAngle);
		toAdd.add(this);
		begin1 = new Image(image);
		mid1 = new Image(image);
		// begin1.setDrawable(ResourcesManager.textures.get("bullet" + (5 * 16 +
		// 8)));
		// mid1.setDrawable(ResourcesManager.textures.get("bullet" + (5 * 16 +
		// 8)));
		begin1.setZIndex(Data.zIndexEnemyBullet);
		mid1.setZIndex(Data.zIndexEnemyBullet);
		begin1.setSize(width, width);
		mid1.setSize(width, createLength);
		objectCenter = new Vector2(centerX, centerY);
		this.finalLength = finalLength;
		this.width = width;
		degrees = (float) Math.toDegrees(directionAngle);
		this.speed = speed;
		//velocity = new Vector2(0, speed).rotate(degrees);

		begin1.setColor(color);
		mid1.setColor(color);
		begin1.setOrigin(begin1.getWidth() / 2, begin1.getWidth() / 2);
		mid1.setOrigin(mid1.getWidth() / 2, mid1.getWidth() / 2);
		begin1.setPosition(objectCenter.x, objectCenter.y);
		mid1.setPosition((float) (objectCenter.x - mid1.getHeight() * Math.cos(Math.toRadians(degrees + 90))),
				(float) (objectCenter.y - mid1.getHeight() * Math.sin(Math.toRadians(degrees + 90))));
		begin1.setRotation(degrees);
		mid1.setRotation(degrees);


		FightScreen.instence.groupHighLight.addActor(begin1);
		FightScreen.instence.groupHighLight.addActor(mid1);


		p1 = new Vector2(begin1.getX() + begin1.getOriginX(), begin1.getY() + begin1.getOriginY());
		p2 = new Vector2(
				(float) (begin1.getX() + begin1.getOriginX() - finalLength * Math.cos(Math.toRadians(degrees + 90))),
				(float) (begin1.getY() + begin1.getOriginY() - finalLength * Math.sin(Math.toRadians(degrees + 90))));
		Vector2 v3 = new Vector2(p2.x - p1.x, p2.y - p1.y).nor();
		v3.scl(finalLength + begin1.getHeight());
		v3.add(p1);
		p2.set(v3);
	}

	@Override
	public void update() {
		++existTime;
		velocity.add(accelerationWithAngle);
		objectCenter.add(velocity);
		if (objectCenter.x < -(5 + finalLength) || objectCenter.x > GameMain.width + 5 + finalLength
				|| objectCenter.y < -(5 + finalLength) || objectCenter.y > GameMain.height + 5 + finalLength) {
			kill();
			return;
		} else {
			judge();
		}
		if (mid1.getHeight() < finalLength) {
			mid1.setHeight(mid1.getHeight() + speed);
		}
		changeTaskManager.update();
		if (Math.abs(speed - targetSpeed) > 0.0001f) {
			speed += speedSub;
			velocity.nor().scl(speed);
		}

		begin1.setColor(color);
		mid1.setColor(color);
		begin1.setOrigin(begin1.getWidth() / 2, begin1.getWidth() / 2);
		mid1.setOrigin(mid1.getWidth() / 2, mid1.getWidth() / 2);
		begin1.setPosition(objectCenter.x, objectCenter.y);
		mid1.setPosition((float) (objectCenter.x - mid1.getHeight() * Math.cos(Math.toRadians(degrees + 90))),
				(float) (objectCenter.y - mid1.getHeight() * Math.sin(Math.toRadians(degrees + 90))));
		begin1.setRotation(degrees);
		mid1.setRotation(degrees);

		p1 = new Vector2(begin1.getX() + begin1.getOriginX(), begin1.getY() + begin1.getOriginY());
		p2 = new Vector2(
				(float) (begin1.getX() + begin1.getOriginX() - finalLength * Math.cos(Math.toRadians(degrees + 90))),
				(float) (begin1.getY() + begin1.getOriginY() - finalLength * Math.sin(Math.toRadians(degrees + 90))));
		Vector2 v3 = new Vector2(p2.x - p1.x, p2.y - p1.y).nor();
		v3.scl(finalLength + begin1.getHeight());
		v3.add(p1);
		p2.set(v3);
	}

	@Override
	public void judge() {
		if (pointToLine(p1.x, p1.y, p2.x, p2.y, MyPlaneReimu.instance.objectCenter.x,
				MyPlaneReimu.instance.objectCenter.y) < width) {
			MyPlaneReimu.instance.kill();
			kill();
			System.out.println("kill");
		}
	}

	@Override
	public void kill() {
		toDelete.add(this);
		ObjectPools.shootLaserPool.free(this);
		begin1.remove();
		mid1.remove();
	}

	public void setTargetSpeed(float targetSpeed, int frame) {
		this.targetSpeed = targetSpeed;
		speedSub = (targetSpeed - speed) / frame;
	}

	public void setTargetDir(float targetDir) {
		this.targetDir = targetDir;
		directionAngle += targetDir;
		velocity.setAngleRad(directionAngle);
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
		accelerationWithAngle = new Vector2(0, acceleration);
	}

	public void setAccelerationAngle(float accelerationAngle) {
		this.accelerationAngle = accelerationAngle;
		accelerationWithAngle.setAngleRad(accelerationAngle);
	}

	public void setSpeed(float speed) {
		this.speed = speed;
		velocity.nor().scl(speed);
	}

	public void setDirectionAngle(float directionAngle) {
		this.directionAngle = directionAngle;
		velocity.setAngleRad(directionAngle);
	}

	public static void updateAll() {
		while (!toDelete.isEmpty()) {
			instances.remove(toDelete.poll());
		}
		while (!toAdd.isEmpty()) {
			instances.add(toAdd.poll());
		}
		for (ShootLaser laser : instances) {
			laser.update();
		}
	}

	// 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
	private double pointToLine(double x1, double y1, double x2, double y2, double x0, double y0) {
		double space = 0;
		double a, b, c;
		a = lineSpace(x1, y1, x2, y2);// 线段的长度
		b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离
		c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离
		if (c <= 0.000001 || b <= 0.000001) {
			space = 0;
			return space;
		}
		if (a <= 0.000001) {
			space = b;
			return space;
		}
		if (c * c >= a * a + b * b) {
			space = b;
			return space;
		}
		if (b * b >= a * a + c * c) {
			space = c;
			return space;
		}
		double p = (a + b + c) / 2;// 半周长
		double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
		space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
		return space;
	}

	// 计算两点之间的距离
	private double lineSpace(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}
}
