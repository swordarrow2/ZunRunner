package com.meng.gui.bullets.laser;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.meng.insLogic.EclBullet;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.meng.gui.planes.*;
import com.meng.gui.helpers.*;
import com.meng.gui.ui.*;

public class FixLaser extends EclBullet {

	public float length;
	public float width;
	public Color color = new Color(Color.WHITE);
	public Color rayColor = new Color(Color.WHITE);
	public float degrees;
	public Image begin1 = new Image();
	public Image mid1 = new Image();
	public Vector2 p1 = new Vector2();
	public Vector2 p2 = new Vector2();

	private float directionAngle;
	private int voiceOnShoot;
	private int voiceOnChangeDirection;
	public int canNotRemoveFrames = 0;


	public static HashSet<FixLaser> instances = new HashSet<>();
	private static LinkedBlockingQueue<FixLaser> toDelete = new LinkedBlockingQueue<>();
	private static LinkedBlockingQueue<FixLaser> toAdd = new LinkedBlockingQueue<>();

	public FixLaser() {

	}

	public static void create(Enemy enemy, float centerX, float centerY, float length, float width,
			float directionAngle, float speed, int voiceOnShoot, int voiceOnChangeDirection) {
		ObjectPools.fixLaserPool.obtain().init(enemy, centerX, centerY, length, width, directionAngle, speed, voiceOnShoot,
				voiceOnChangeDirection);
	}

	public void init(Enemy enemy, float centerX, float centerY, float length, float width, float directionAngle,
			float speed, int voiceOnShoot, int voiceOnChangeDirection) {
		existTime = 0; 
		this.enemy = enemy;
		this.directionAngle = directionAngle;
		this.speed = speed;
		this.voiceOnShoot = voiceOnShoot;
		this.voiceOnChangeDirection = voiceOnChangeDirection;
		objectCenter.set(centerX, centerY);
		velocity = new Vector2(0, speed).rotateRad(directionAngle);
		toAdd.add(this);

		begin1.setDrawable(ResourcesManager.textures.get("bullet" + (0 * 16 + 8)));
		mid1.setDrawable(ResourcesManager.textures.get("bullet" + (0 * 16 + 8)));
		begin1.setZIndex(Data.zIndexEnemyBullet);
		mid1.setZIndex(Data.zIndexEnemyBullet);
		begin1.setSize(width, width);
		mid1.setSize(width, length);
		objectCenter = new Vector2(centerX, centerY);
		this.length = length;
		this.width = width;
		degrees = (float) Math.toDegrees(directionAngle);
		this.speed = speed;
		velocity = new Vector2(0, speed).rotate(degrees);

		begin1.setColor(color);
		mid1.setColor(color);
		begin1.setOrigin(begin1.getWidth() / 2, begin1.getHeight() / 2);
		mid1.setOrigin(mid1.getWidth() / 2, begin1.getHeight() / 2 - begin1.getHeight());
		mid1.setSize(mid1.getWidth(), length);
		begin1.setPosition(objectCenter.x - begin1.getHeight() / 2, objectCenter.y - begin1.getHeight() / 2);
		mid1.setPosition(begin1.getX(), begin1.getY() + begin1.getHeight());
		begin1.setRotation(degrees);
		mid1.setRotation(degrees);
		p1 = new Vector2(begin1.getX() + begin1.getOriginX(), begin1.getY() + begin1.getOriginY());
		p2 = new Vector2(
				(float) (begin1.getX() + begin1.getOriginX() + length * Math.cos(Math.toRadians(degrees + 90))),
				(float) (begin1.getY() + begin1.getOriginY() + length * Math.sin(Math.toRadians(degrees + 90))));
		Vector2 v3 = new Vector2(p2.x - p1.x, p2.y - p1.y).nor();
		v3.scl(length + begin1.getHeight());
		v3.add(p1);
		p2.set(v3);

		FightScreen.instence.groupHighLight.addActor(begin1);
		FightScreen.instence.groupHighLight.addActor(mid1);
	}

	@Override
	public void update() {
		++existTime;
		if (objectCenter.x < -5 || objectCenter.x > GameMain.width + 5 || objectCenter.y < -5
				|| objectCenter.y > GameMain.height + 5) {
			kill();
		} else {
			judge();
		}

		begin1.setColor(color);
		mid1.setColor(color);
		begin1.setOrigin(begin1.getWidth() / 2, begin1.getHeight() / 2);
		mid1.setOrigin(mid1.getWidth() / 2, begin1.getHeight() / 2 - begin1.getHeight());
		mid1.setSize(mid1.getWidth(), length);
		begin1.setPosition(objectCenter.x - begin1.getHeight() / 2, objectCenter.y - begin1.getHeight() / 2);
		mid1.setPosition(begin1.getX(), begin1.getY() + begin1.getHeight());
		begin1.setRotation(degrees);
		mid1.setRotation(degrees);
		p1 = new Vector2(begin1.getX() + begin1.getOriginX(), begin1.getY() + begin1.getOriginY());
		p2 = new Vector2(
				(float) (begin1.getX() + begin1.getOriginX() + length * Math.cos(Math.toRadians(degrees + 90))),
				(float) (begin1.getY() + begin1.getOriginY() + length * Math.sin(Math.toRadians(degrees + 90))));
		Vector2 v3 = new Vector2(p2.x - p1.x, p2.y - p1.y).nor();
		v3.scl(length + begin1.getHeight());
		v3.add(p1);
		p2.set(v3);
	}

	@Override
	public void judge() {
		if (pointToLine(p1.x, p1.y, p2.x, p2.y, MyPlaneReimu.instance.objectCenter.x,
				MyPlaneReimu.instance.objectCenter.y) < width) {
			MyPlaneReimu.instance.kill();
			kill();
		}
	}

	@Override
	public void kill() {
		toDelete.add(this);
		ObjectPools.fixLaserPool.free(this);
		begin1.remove();
		mid1.remove();
	}

	public static void updateAll() {
		while (!toDelete.isEmpty()) {
			instances.remove(toDelete.poll());
		}
		while (!toAdd.isEmpty()) {
			instances.add(toAdd.poll());
		}
		for (FixLaser laser : instances) {
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
