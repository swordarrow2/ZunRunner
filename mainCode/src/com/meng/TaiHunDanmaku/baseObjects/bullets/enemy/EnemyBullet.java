package com.meng.TaiHunDanmaku.baseObjects.bullets.enemy;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.meng.TaiHunDanmaku.baseObjects.bullets.BaseBullet;
import com.meng.TaiHunDanmaku.helpers.Data;
import com.meng.TaiHunDanmaku.helpers.ObjectPools;
import com.meng.TaiHunDanmaku.helpers.ResourcesManager;
import com.meng.TaiHunDanmaku.task.Task;
import com.meng.TaiHunDanmaku.task.TaskManagerBullet;
import com.meng.TaiHunDanmaku.task.TaskRepeatMode;
import com.meng.TaiHunDanmaku.ui.FightScreen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

public class EnemyBullet extends BaseBullet {

    public static HashSet<EnemyBullet> instances = new HashSet<EnemyBullet>();
    public static LinkedBlockingQueue<EnemyBullet> toDelete = new LinkedBlockingQueue<EnemyBullet>();
    public static LinkedBlockingQueue<EnemyBullet> toAdd = new LinkedBlockingQueue<EnemyBullet>();

    public int reflexCount = 0;
    public int reflexTopCount = 0;
    public int reflexBottomCount = 0;
    public int reflexLeftCount = 0;
    public int reflexRightCount = 0;
    public int throughCount = 0;
    public int throughTopCount = 0;
    public int throughBottomCount = 0;
    public int throughLeftCount = 0;
    public int throughRightCount = 0;
    public int colorNum = 0;
    public int formNum = 0;

    public Vector2 acceleration = new Vector2();

    public TaskManagerBullet taskManager;

    public static void create(Vector2 center, Vector2 velocity, Vector2 acceleration, BulletForm bulletForm, BulletColor bulletColor, boolean highLight, int reflex, int reflexT, int reflexB, int reflexL, int reflexR, int through, int throughT, int throughB, int throughL, int throughR, ArrayList<Task> tasks) {
        ObjectPools.enemyBulletPool.obtain().init(center, velocity, acceleration, bulletForm, bulletColor, highLight, reflex, reflexT, reflexB, reflexL, reflexR, through, throughT, throughB, throughL, throughR, tasks);
    }

    @Override
    public Drawable getDrawable() {
        if (formNum == 13 || formNum == 14) {
            image.setSize(14, 16);
        }
        if (formNum == 0) {
            image.setSize(8, 8);
        }
        if (formNum == 8) {
            image.setSize(8, 10);
        }
        if (drawable == null) {
            drawable = ResourcesManager.textures.get("bullet" + (formNum * 16 + colorNum));
        }
        return drawable;
    }

    public void init(Vector2 center, Vector2 velocity, Vector2 acceleration, BulletForm bulletForm, BulletColor bulletColor, boolean highLight, int reflex, int reflexT, int reflexB, int reflexL, int reflexR, int through, int throughT, int throughB, int throughL, int throughR, ArrayList<Task> tasks) {
        super.init();
        toAdd.add(this);
        taskManager = new TaskManagerBullet(this, TaskRepeatMode.noRepeat);
        for (Task t : tasks) {
            taskManager.addTask(t);
        }
        reflexCount = reflex;
        reflexTopCount = reflexT;
        reflexBottomCount = reflexB;
        reflexLeftCount = reflexL;
        reflexRightCount = reflexR;
        throughCount = through;
        throughTopCount = throughT;
        throughBottomCount = throughB;
        throughLeftCount = throughL;
        throughRightCount = throughR;
        objectCenter.set(center);
        this.velocity.set(velocity);
        this.acceleration.set(acceleration);
        image.setPosition(center.x, center.y, Align.center);
        judgeCircle = new Circle(objectCenter, Math.min(image.getWidth(), image.getHeight()) / 3);
        switch (bulletColor) {
            case gray:
                colorNum = 0;
                break;
            case grayAndRed:
                colorNum = 1;
                break;
            case red:
                colorNum = 2;
                break;
            case grayAndPurple:
                colorNum = 3;
                break;
            case purple:
                colorNum = 4;
                break;
            case grayAndBlue:
                colorNum = 5;
                break;
            case blue:
                colorNum = 6;
                break;
            case grayAndLightBlue:
                colorNum = 7;
                break;
            case lightBlue:
                colorNum = 8;
                break;
            case grayAndGreen:
                colorNum = 9;
                break;
            case green:
                colorNum = 10;
                break;
            case grayAndYellow:
                colorNum = 11;
                break;
            case yellow_dark:
                colorNum = 12;
                break;
            case yellow_light:
                colorNum = 13;
                break;
            case orange:
                colorNum = 14;
                break;
            case white:
                colorNum = 15;
                break;
            default:
                break;
        }
        switch (bulletForm) {
            case jiguangkuai:
                formNum = 22;
                break;
            case lindan:
                formNum = 14;
                break;
            case huanyu:
                formNum = 6;
                break;
            case xiaoyu:
                formNum = 4;
                break;
            case midan:
                formNum = 8;
                break;
            case liandan:
                formNum = 10;
                break;
            case zhendan:
                formNum = 11;
                break;
            case zadan:
                formNum = 13;
                break;
            case chongdan:
                formNum = 15;
                break;
            case ganjundan:
                formNum = 17;
                break;
            case xingdan:
                formNum = 18;
                break;
            case xiaodan:
                formNum = 16;
                break;
            case jundan:
                formNum = 3;
                break;
            case lidan:
                formNum = 4;
                break;
            case diandan:
                formNum = 0;
                break;
            default:
                break;
        }
        image.setDrawable(getDrawable());
        if (highLight) {
            FightScreen.instence.groupHighLight.addActor(image);
        } else {
            FightScreen.instence.groupNormal.addActor(image);
        }
        image.setZIndex(Data.zIndexEnemyBullet);
    }

    public static void updateAll() {
        while (!toDelete.isEmpty()) {
            instances.remove(toDelete.poll());
        }
        while (!toAdd.isEmpty()) {
            instances.add(toAdd.poll());
        }
        for (EnemyBullet baseBullet : instances) {
            baseBullet.update();
        }
    }

    @Override
    public void kill() {
        super.kill();
        toDelete.add(this);
        image.remove();
        drawable = null;
        ObjectPools.enemyBulletPool.free(this);
    }

    @Override
    public void update() {
        super.update();
        if (taskManager != null) {
            taskManager.update();
        }
        velocity.add(acceleration);
        objectCenter.add(velocity);
    }

    @Override
    public Shape2D getCollisionArea() {
        return judgeCircle;
    }

    @Override
    public void judge() {

    }

    @Override
    public Vector2 getSize() {
        return new Vector2(16, 16);
    }

    @Override
    public float getRotationDegree() {
        return velocity.angle() + 270;
    }
}
