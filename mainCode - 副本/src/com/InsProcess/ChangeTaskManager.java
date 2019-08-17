package com.InsProcess;

import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.ui.FightScreen;

public class ChangeTaskManager {

    private EclBullet bullet;
    private ChangeTask[] taskList = new ChangeTask[16];
    public int nowTask = 0;
    private int holdingTime = 0;
    private EclBulletShooter eclBulletShooter;

    public ChangeTaskManager(EclBullet enemyBullet) {
        bullet = enemyBullet;
    }

    public void addChange(int num, int way, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn) {
        taskList[num] = new ChangeTask(way == 0, mode, inta, intb, intc, intd, floatr, floats, floatm, floatn);
    }

    public void addChange(int way, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn) {
        taskList[nowTask++] = new ChangeTask(way == 0, mode, inta, intb, intc, intd, floatr, floats, floatm, floatn);
    }

    public void addChange(ChangeTask task) {
        taskList[nowTask++] = task;
    }

    public void addChange(int pos, ChangeTask task) {
        taskList[pos] = task;
    }

    public void end() {
        nowTask = 0;
    }

    public void update() {
        if (holdingTime-- > 0) {
            return;
        }
        ChangeTask task = taskList[nowTask];
        if (task != null) {
            doTask(task);
            //	throw new NullPointerException(task.mode+"");
        }
    }

    private void doTask(ChangeTask task) {
    	switch (task.mode) {
            case 1: // 1<<0
                break;
            case 2: // 1<<1
                break;
            case 4: // 1<<2
                holdingTime = task.a;
                if (task.r != -999999.0f) {
                    bullet.setAcceleration(task.r);
                }
                if (task.s != -999999.0f) {
                    bullet.setAccelerationAngle(task.s);
                }
                break;
            case 8: // 1<<3
                break;
            case 16: // 1<<4
                break;
            case 32: // 1<<5
                break;
            case 64: // 1<<6
                break;
            case 128: // 1<<7
                break;
            case 256: // 1<<8
                break;
            case 512: // 1<<9
                break;
            case 1024: // 1<<10
                break;
            case 2048: // 1<<11
                break;
            case 4096: // 1<<12
                break;
            case 8192: // 1<<13
                eclBulletShooter = new EclBulletShooter().init(bullet.enemy);
                eclBulletShooter.setFormAndColor(task.b, task.c);
                if (task.r != -999999.0f) {
                    eclBulletShooter.setDirection(task.r);
                }
                if (task.s != -999999.0f) {
                    eclBulletShooter.setDirectionSub(task.s);
                }
                break;
            case 16384: // 1<<14
                if(eclBulletShooter.isLaser){
                	eclBulletShooter.setFormAndColor(task.a, task.b);
                	eclBulletShooter.setNowTask(task.c);
                	eclBulletShooter.shoot();
               // 	System.out.println("laser shoot");
                } else {
                	eclBulletShooter.setFormAndColor(task.a, task.b);
                    if (task.c == 1) {
                        bullet.kill();
                    }
                    if (task.r != -999999.0f) {
                        eclBulletShooter.setDirection(task.r);
                    }
                    if (task.s != -999999.0f) {
                        eclBulletShooter.setDirectionSub(task.s);
                    }
                    eclBulletShooter.shoot();
				}
                break;
            case 32768: // 1<<15
                break;
            case 65536: // 1<<16
                break;
            case 131072: // 1<<17
                break;
            case 262144: // 1<<18
                break;
            case 524288: // 1<<19
                break;
            case 1048576: // 1<<20
                if (task.a == 1) {
                    FightScreen.instence.groupHighLight.addActor(bullet.image);
                    FightScreen.instence.groupNormal.removeActor(bullet.image);
                } else {
                    FightScreen.instence.groupNormal.addActor(bullet.image);
                    FightScreen.instence.groupHighLight.removeActor(bullet.image);
                }
                break;
            case 2097152: // 1<<21
                holdingTime = task.a;
                if (task.r != -999999.0f) {
                    bullet.setTargetSpeed(task.r, task.a);
                }
                if (task.s != -999999.0f) {
                    bullet.setTargetDir(task.s);
                }
                break;
            case 4194304: // 1<<22
                break;
            case 8388608: // 1<<23
                break;
            case 16777216: // 1<<24
                break;
            case 33554432: // 1<<25
                break;
            case 67108864: // 1<<26
                break;
            case 134217728: // 1<<27
                eclBulletShooter = new EclBulletShooter().init(bullet.enemy);
                eclBulletShooter.setCenter(bullet.objectCenter.x, bullet.objectCenter.y);
                eclBulletShooter.setFormAndColor(task.b, task.c);
                if (task.r !=-999999.0f) {
                eclBulletShooter.setDirectionAndSub(task.r, 0);
                } else {
					eclBulletShooter.setDirectionAndSub(bullet.directionAngle, 0);
				}
                eclBulletShooter.setSpeed(task.s+bullet.speed, 0);
                eclBulletShooter.setLaserCreateLenght(task.m);
                eclBulletShooter.setLaserFinalLength(task.n);
                if (task.d == 1) {
                    bullet.waitKill();
                }
                if (task.r != -999999.0f) {
                    eclBulletShooter.setDirection(task.r);
                }
                if (task.s != -999999.0f) {
                    eclBulletShooter.setDirectionSub(task.s);
                }
                eclBulletShooter.isLaser=true;
                break;
            case 268435456: // 1<<28
                break;
            case 536870912: // 1<<29
                break;
            case 1073741824: // 1<<30
                break;
            case -2147483648: // 1<<31
                holdingTime = task.a;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + task.mode);
        }
        ++nowTask;
    }
}
