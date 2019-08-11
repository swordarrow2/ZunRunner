package com.meng.TaiHunDanmaku.baseObjects.bullets.enemy;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.baseObjects.bullets.BaseBullet;
import com.meng.TaiHunDanmaku.baseObjects.planes.MyPlane;
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

    public static HashSet<EnemyBullet> instances = new HashSet<>();
    private static LinkedBlockingQueue<EnemyBullet> toDelete = new LinkedBlockingQueue<>();
    private static LinkedBlockingQueue<EnemyBullet> toAdd = new LinkedBlockingQueue<>();

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

    public static void create(Vector2 center, Vector2 velocity, Vector2 acceleration, int bulletForm, int bulletColor,
                              boolean highLight, int reflex, int reflexT, int reflexB, int reflexL, int reflexR, int through,
                              int throughT, int throughB, int throughL, int throughR, ArrayList<Task> tasks) {
        ObjectPools.enemyBulletPool.obtain().init(center, velocity, acceleration, bulletForm, bulletColor, highLight,
                reflex, reflexT, reflexB, reflexL, reflexR, through, throughT, throughB, throughL, throughR, tasks);
    }

    public void init(Vector2 center, Vector2 velocity, Vector2 acceleration, int bulletForm, int bulletColor,
                     boolean highLight, int reflex, int reflexT, int reflexB, int reflexL, int reflexR, int through,
                     int throughT, int throughB, int throughL, int throughR, ArrayList<Task> tasks) {
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
        //	image.setPosition(center.x, center.y, Align.center);
        judgeCircle = new Circle(objectCenter, Math.min(image.getWidth(), image.getHeight()) / 3);
        colorNum = bulletColor;
        formNum = bulletForm;
        image.setDrawable(ResourcesManager.textures.get("bullet" + (formNum * 16 + colorNum)));
        if (highLight) {
            FightScreen.instence.groupHighLight.addActor(image);
        } else {
            FightScreen.instence.groupNormal.addActor(image);
        }
        if (formNum == 13 || formNum == 14) {
            image.setSize(14, 16);
        }
        if (formNum == 0) {
            image.setSize(8, 8);
        }
        if (formNum == 8) {
            image.setSize(8, 10);
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
    public void judge() {
        if (getCollisionArea().contains(MyPlane.instance.objectCenter)) {
            MyPlane.instance.kill();
            kill();
        }
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
