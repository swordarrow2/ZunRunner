package com.meng.TaiHunDanmaku.baseObjects.bullets;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.meng.TaiHunDanmaku.baseObjects.planes.Enemy;
import com.meng.TaiHunDanmaku.baseObjects.planes.MapleEnemy;
import com.meng.TaiHunDanmaku.ui.FightScreen;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.meng.TaiHunDanmaku.helpers.*;


public abstract class BaseMyBullet extends BaseBullet {

    private static HashSet<BaseMyBullet> instances = new HashSet<BaseMyBullet>();
    private static LinkedBlockingQueue<BaseMyBullet> toDelete = new LinkedBlockingQueue<BaseMyBullet>();
    private static LinkedBlockingQueue<BaseMyBullet> toAdd = new LinkedBlockingQueue<BaseMyBullet>();

    public float damage = 0;

    public void init(Vector2 center, Vector2 velocity) {
        super.init();
        toAdd.add(this);
        objectCenter.set(center);
        this.velocity.set(velocity);
        image.setPosition(objectCenter.x, objectCenter.y, Align.center);
        judgeCircle = new Circle(objectCenter, image.getHeight() / 2 * 3); //中心、半径
        FightScreen.instence.groupNormal.addActor(image);
        image.setZIndex(Data.zIndexMyBullet);
    }

    @Override
    public void kill() {
        super.kill();
        toDelete.add(this);
        image.remove();
    }

    @Override
    public void update() {
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
        for (BaseMyBullet baseBullet : instances) {
            baseBullet.update();
        }
    }

    @Override
    public void judge() {
        if (Enemy.instances.isEmpty()) {
            return;
        }
        for (Enemy e : Enemy.instances) {
            if (!(e instanceof MapleEnemy)) {
                if (((Circle) e.getJudgeCircle()).overlaps(judgeCircle)) {
                    e.hit(damage);
                    kill();
                }
            }
        }
    }
}
