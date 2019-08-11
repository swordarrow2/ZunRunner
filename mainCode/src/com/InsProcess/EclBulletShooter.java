package com.InsProcess;

import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.baseObjects.planes.MyPlaneReimu;

public class EclBulletShooter {
	public Vector2 center;
	public float offsetX;
	public float offsetY;
	public int color;
	public int form;
	public float directionRadians;
	public float directionRadiansSub;
	public float speed;
	public float slowestSpeed;
	public float acceleration;
	public float accelerationRadians;
	public int way;
	public int overlap;
	public int style;
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

	public void shoot(){
		
		switch(style){
		case 0:
			break;
		case 1:
			directionRadians=MyPlaneReimu.instance.objectCenter.cpy().sub(center).angleRad();
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			break;
		case 11:
			break;
		case 12:
			break;
		
		}
		
		
		
		
		
		
		
		
		
		
		
		
		EclBullet.create(center.x, center.y, offsetX, offsetY, color, form, directionRadians, directionRadiansSub,
				speed, slowestSpeed, acceleration, accelerationRadians, way, overlap, voiceOnShoot,
				voiceOnChangeDirection, dis, refelxCount, reflexTop, reflexBottom, reflexRight, reflexLeft,
				throughCount, throughTop, throughBottom, throughRight, throughLeft, canNotRemoveFrames);
	}
	
	 

}
