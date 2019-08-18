package com.InsProcess;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.ShootLaser;
import com.meng.TaiHunDanmaku.baseObjects.planes.Enemy;
import com.meng.TaiHunDanmaku.baseObjects.planes.MyPlane;
import com.meng.TaiHunDanmaku.helpers.ResourcesManager;
import com.meng.TaiHunDanmaku.ui.FightScreen;

import java.util.Random;
import java.util.*;

public class EclBulletShooter implements Cloneable {

	public boolean isLaser=false;
    private Enemy enemy;

    public Vector2 center;
    private float offsetX;
    private float offsetY;
    public int color;
    public int form;
    private float directionAngle;
    private float directionAngleSub;
    private float speed;
    private float slowestSpeed;
    private int way;
    private int overlap;
    private int style;
    private int voiceOnShoot;
    private int voiceOnChange;

    private Vector2 disVect;
    private Vector2 tmpv1;

    private float allAngleDivide2;// 弹幕总角度/2
    private float overlapSpeedSub;

    private ChangeTask[] taskList = new ChangeTask[16];
    private int taskFlag = 0;
    
    private float laserCreateLenght;
    private float laserFinalLength;
    
    private int taskToJump=0;

    public EclBulletShooter init(Enemy enemy) {
        this.enemy = enemy;
        center = enemy.objectCenter;
        disVect = new Vector2(0, 0);
        return this;
    }

    public EclBulletShooter setLaserCreateLenght(float laserCreateLenght) {
		this.laserCreateLenght = laserCreateLenght;
		return this;
	}
    
    public EclBulletShooter setLaserFinalLength(float laserFinalLength) {
		this.laserFinalLength = laserFinalLength;
		return this;
	}
    
    public EclBulletShooter addChange(ChangeTask ct) {
        taskList[taskFlag++] = ct;
        return this;
    }

    public EclBulletShooter addChange(int pos, ChangeTask ct) {
        taskList[pos] = ct;
        return this;
    }

    public EclBulletShooter setCenter(float x, float y) {
        center = new Vector2(x, y);
        return this;
    }

    public EclBulletShooter setFormAndColor(int form, int color) {
        this.form = form;
        this.color = color;
        return this;
    }

    public EclBulletShooter setOffsetCartesian(float x, float y) {
        offsetX = x;
        offsetY = y;
        return this;
    }

    public EclBulletShooter setDirection(float direction) {
        directionAngle = direction;
        return this;
    }

    public EclBulletShooter setDirectionSub(float sub) {
        if (sub != 0) {
            directionAngleSub = sub;
        } else if (way > 1) {
            directionAngleSub = (float) (Math.PI * 2 / way);
        }
        allAngleDivide2 = ((way - 1) * directionAngleSub) / 2f;
        return this;
    }

    public EclBulletShooter setDirectionAndSub(float direction, float sub) {
        directionAngle = direction;
        if (sub != 0) {
            directionAngleSub = sub;
        } else if (way > 1) {
            directionAngleSub = (float) (Math.PI * 2 / way);
        }
        allAngleDivide2 = ((way - 1) * directionAngleSub) / 2f;
        return this;
    }

    public EclBulletShooter setSpeed(float speed, float slowestSpeed) {
        this.speed = speed;
        this.slowestSpeed = slowestSpeed;
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        return this;
    }

    public EclBulletShooter setWaysAndOverlap(int ways, int overlaps) {
        way = ways;
        overlap = overlaps;
        if (way > 1) {
            directionAngleSub = (float) (Math.PI * 2 / way);
        }
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        allAngleDivide2 = ((way - 1) * directionAngleSub) / 2f;
        return this;
    }

    public EclBulletShooter setStyle(int style) {
        this.style = style;
        return this;
    }

    public EclBulletShooter setVoice(int onShoot, int onChange) {
        voiceOnShoot = onShoot;
        voiceOnChange = onChange;
        return this;
    }
    
    public EclBulletShooter setNowTask(int now){
    	taskToJump=now;
    	return this;
    }

    public EclBulletShooter setDis(int dis) {// 以boss为半径为dis的圆形边上发弹
        disVect = new Vector2(0, dis);
        return this;
    }

    public void shoot() {     
    	if(isLaser){ 
    	//	System.out.println("laser:enemy"+enemy+" center:"+center+" clength:"+laserCreateLenght+" flength:"+laserFinalLength+" dir:"+directionAngle+" speed:"+speed+" jmp:"+taskToJump);
        	ShootLaser.create(enemy,ResourcesManager.textures.get("bullet" + ((5 << 4) + color)), center.x,center.y, laserFinalLength,laserFinalLength, 5, directionAngle, speed, 0, 0,new ChangeTask[0],taskToJump);
        	return;
    	}
        if (way == 1 && overlap == 1) {
            EclBullet.create(enemy, center.x, center.y, offsetX, offsetY, form, color, directionAngle, speed, voiceOnShoot, voiceOnChange, taskList);
        } else {
        //	System.out.println("way:"+way);
            switch (style) {
                case 0:
                    createBullets01();
                    break;
                case 1:
                    directionAngle = MyPlane.instance.objectCenter.cpy().sub(center).angleRad();
                    createBullets01();
                    break;
                case 2:
                    directionAngle = MyPlane.instance.objectCenter.cpy().sub(center).angleRad();
                    createBullets23();
                    break;
                case 3:
                    createBullets23();
                    break;
                case 4:
                    directionAngle = MyPlane.instance.objectCenter.cpy().sub(center).angleRad();
                    createBullets45();
                    break;
                case 5:
                    createBullets45();
                    break;
                case 6:
                    createBullets6();
                    break;
                case 7:
                    createBullets7();
                    break;
                case 8:
                    createBullets8();
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + style);
            }
        }
    }

    private void createBullets01() {
        float overlapSpeedSub;// 两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            float tmpangle = directionAngle - allAngleDivide2;// 调整循环初始角度位置
            for (int i = 0; i < way; ++i) {
                disVect.setAngleRad(tmpangle);
                EclBullet.create(enemy, center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle,
                        baseSpeed, voiceOnShoot, voiceOnChange, taskList);
                tmpangle += directionAngleSub;
            }
            baseSpeed -= overlapSpeedSub;
        }
    }

    private void createBullets23() {
        float overlapSpeedSub;// 两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            float tmpangle = directionAngle - allAngleDivide2 - ceng * directionAngleSub;// 调整循环初始角度位置
            for (int i = 0; i < way; ++i) {
                if (disVect.len2() != 0) {
                    disVect.setAngleRad(tmpangle);
                }
                EclBullet.create(enemy, center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle,
                        baseSpeed, voiceOnShoot, voiceOnChange, taskList);
                tmpangle += directionAngleSub;
            }
            baseSpeed -= overlapSpeedSub;
        }
    }

    private void createBullets45() {
        float overlapSpeedSub;// 两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            float tmpangle = directionAngle - allAngleDivide2 + ceng * directionAngleSub;// 调整循环初始角度位置
            for (int i = 0; i < way; ++i) {
                disVect.setAngleRad(tmpangle);
                EclBullet.create(enemy, center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle,
                        baseSpeed, voiceOnShoot, voiceOnChange, taskList);
                tmpangle += directionAngleSub;
            }
            baseSpeed -= overlapSpeedSub;
        }
    }

    private void createBullets6() {
        float overlapSpeedSub;// 两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            for (int i = 0; i < way; ++i) {
                Random random = new Random();
                float tmpangle = directionAngle
                        + (random.nextFloat() * directionAngleSub * (random.nextBoolean() ? 1 : -1));
                disVect.setAngleRad(tmpangle);
                EclBullet.create(enemy, center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle,
                        baseSpeed, voiceOnShoot, voiceOnChange, taskList);
            }
            baseSpeed -= overlapSpeedSub;
        }
    }

    private void createBullets7() {
        for (int ceng = 0; ceng < overlap; ++ceng) {
            for (int i = 0; i < way; ++i) {
                Random random = new Random();
                float baseSpeed = random.nextFloat() * 2 * speed + speed;
                float tmpangle = random.nextFloat() * 6.283185307179586f;
                disVect.setAngleRad(tmpangle);
                EclBullet.create(enemy, center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange, taskList);
            }
        }
    }

    private void createBullets8() {
        for (int ceng = 0; ceng < overlap; ++ceng) {
            for (int i = 0; i < way; ++i) {
                Random random = new Random();
                float baseSpeed = random.nextFloat() * 2 * speed + speed;
                float tmpangle = directionAngle + (random.nextFloat() * directionAngleSub * (random.nextBoolean() ? 1 : -1));
                disVect.setAngleRad(tmpangle);
                EclBullet.create(enemy, center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange, taskList);
            }
        }
    }

    private void createBullets9() {
        float overlapSpeedSub;// 两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            float tmpangle = directionAngle - allAngleDivide2 - ceng * directionAngleSub;// 调整循环初始角度位置
            for (int i = 0; i < way; ++i) {
                disVect.setAngleRad(tmpangle);
                EclBullet.create(enemy, center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange, taskList);
                tmpangle += directionAngleSub;
            }
            baseSpeed -= overlapSpeedSub;
        }
    }

    @Override
    public EclBulletShooter clone() {
        EclBulletShooter eclBulletShooter = null;
        try {
            eclBulletShooter = (EclBulletShooter) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new NullPointerException("clone failed");
        }
        eclBulletShooter.center = center.cpy();
        eclBulletShooter.disVect = disVect.cpy();
        eclBulletShooter.taskList = new ChangeTask[16];
        for (int i = 0, taskListLength = taskList.length; i < taskListLength; i++) {
            ChangeTask changeTask = taskList[i];
            if (changeTask != null) {
                eclBulletShooter.taskList[i] = changeTask.clone();
            } else {
                break;
            }
        }
        if (tmpv1 != null) {
            eclBulletShooter.tmpv1 = tmpv1.cpy();
        }
        return eclBulletShooter;
    }
}
