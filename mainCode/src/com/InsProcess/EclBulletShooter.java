package com.InsProcess;

import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.baseObjects.planes.MyPlane;

import java.util.Random;

public class EclBulletShooter {
    public Vector2 center;
    private float offsetX;
    private float offsetY;
    private int color;
    private int form;
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

    private float allAngleDivide2;//弹幕总角度/2

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

    public EclBulletShooter setDirectionAndSub(float direction, float sub) {
        directionAngle = direction;
        directionAngleSub = sub;
        if (way > 1) {
            allAngleDivide2 = ((way - 1) * directionAngleSub) / 2;
        }
        return this;
    }

    public EclBulletShooter setSpeed(float speed, float slowestSpeed) {
        this.speed = speed;
        this.slowestSpeed = slowestSpeed;
        return this;
    }

    public EclBulletShooter setWaysAndOverlap(int ways, int overlaps) {
        way = ways;
        overlap = overlaps;
        if (way > 1) {
            allAngleDivide2 = ((way - 1) * directionAngleSub) / 2;
        }
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

    public EclBulletShooter setDis(int dis) {// 以boss为半径为dis的圆形边上发弹
        disVect = new Vector2(0, dis);
        return this;
    }

    public void shoot() {
        if (way == 1 && overlap == 1) {
            EclBullet.create(center.x, center.y, offsetX, offsetY, form, color, directionAngle, speed, voiceOnShoot, voiceOnChange);
        } else {
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
        float overlapSpeedSub;//两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            float tmpangle = directionAngle - allAngleDivide2 - 1.5707963267948966f;//调整循环初始角度位置
            for (int i = 0; i < way; ++i) {
                disVect.setAngleRad(tmpangle - 1.5707963267948966f);
                EclBullet.create(center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange);
                tmpangle += directionAngleSub;
            }
            baseSpeed -= overlapSpeedSub;
        }
    }

    private void createBullets23() {
        float overlapSpeedSub;//两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            float tmpangle = directionAngle - allAngleDivide2 - ceng * directionAngleSub - 1.5707963267948966f;//调整循环初始角度位置
            for (int i = 0; i < way; ++i) {
                disVect.setAngleRad(tmpangle - 1.5707963267948966f);
                EclBullet.create(center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange);
                tmpangle += directionAngleSub;
            }
            baseSpeed -= overlapSpeedSub;
        }
    }

    private void createBullets45() {
        float overlapSpeedSub;//两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            float tmpangle = directionAngle - allAngleDivide2 + ceng * directionAngleSub - 1.5707963267948966f;//调整循环初始角度位置
            for (int i = 0; i < way; ++i) {
                disVect.setAngleRad(tmpangle - 1.5707963267948966f);
                EclBullet.create(center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange);
                tmpangle += directionAngleSub;
            }
            baseSpeed -= overlapSpeedSub;
        }
    }

    private void createBullets6() {
        float overlapSpeedSub;//两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            for (int i = 0; i < way; ++i) {
                Random random = new Random();
                float tmpangle = directionAngle + (random.nextFloat() * directionAngleSub * (random.nextBoolean() ? 1 : -1)) - 1.5707963267948966f;
                disVect.setAngleRad(tmpangle - 1.5707963267948966f);
                EclBullet.create(center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange);
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
                disVect.setAngleRad(tmpangle - 1.5707963267948966f);
                EclBullet.create(center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange);
            }
        }
    }

    private void createBullets8() {
        for (int ceng = 0; ceng < overlap; ++ceng) {
            for (int i = 0; i < way; ++i) {
                Random random = new Random();
                float baseSpeed = random.nextFloat() * 2 * speed + speed;
                float tmpangle = directionAngle + (random.nextFloat() * directionAngleSub * (random.nextBoolean() ? 1 : -1)) - 1.5707963267948966f;
                disVect.setAngleRad(tmpangle - 1.5707963267948966f);
                EclBullet.create(center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange);
            }
        }
    }

    private void createBullets9() {
        float overlapSpeedSub;//两层间的弹速差
        if (overlap == 1) {
            overlapSpeedSub = 1;
        } else {
            overlapSpeedSub = (speed - slowestSpeed) / (overlap - 1);
        }
        float baseSpeed = speed;
        for (int ceng = 0; ceng < overlap; ++ceng) {
            float tmpangle = directionAngle - allAngleDivide2 - ceng * directionAngleSub - 1.5707963267948966f;//调整循环初始角度位置
            for (int i = 0; i < way; ++i) {
                disVect.setAngleRad(tmpangle - 1.5707963267948966f);
                EclBullet.create(center.x + disVect.x, center.y + disVect.y, offsetX, offsetY, form, color, tmpangle, baseSpeed, voiceOnShoot, voiceOnChange);
                tmpangle += directionAngleSub;
            }
            baseSpeed -= overlapSpeedSub;
        }
    }
}
