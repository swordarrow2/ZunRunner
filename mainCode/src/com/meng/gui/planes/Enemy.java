package com.meng.gui.planes;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.meng.gui.*;
import com.meng.gui.helpers.*;
import com.meng.gui.task.*;
import com.meng.gui.ui.*;

public class Enemy extends BaseGameObject implements Cloneable{

    public static HashSet<Enemy> instances = new HashSet<Enemy>();
    private static LinkedBlockingQueue<Enemy> toDelete = new LinkedBlockingQueue<Enemy>();
    private static LinkedBlockingQueue<Enemy> toAdd = new LinkedBlockingQueue<Enemy>();

    private final int[][] junkoAnim = new int[][]{
            {10, 14},
            {5, 8}
    };

    public int time = 0;
    public float enemyLastX;
    public float hp = 10;
    public boolean isKilled = false;

    public int animFrom = 0;
    public int animTo = 7;
    public int everyAnimFrameTime = 0;
    public int curFrameNumber = 0;
    public MoveStatus status = MoveStatus.stay;
    public String objectName = "";
    public boolean flip = false;
    public int[][] animNum;

    public Vector2 targetPosition = new Vector2();

    public TaskManagerEnemyPlane taskManager;
    
    @Override
    public Enemy clone() throws CloneNotSupportedException {
    	Enemy enemy=(Enemy) super.clone();
    	enemy.targetPosition=new Vector2(targetPosition);
    	return enemy;
    }

    public void init(Vector2 center, int everyAnimFrameTime, int hp, Task[] task) {
        super.init();
        if (FightScreen.instence.onBoss) {
            FightScreen.instence.boss = this;
        }
        toAdd.add(this);
        taskManager = new TaskManagerEnemyPlane(this, TaskRepeatMode.noRepeat);
        for (Task t : task) {
            taskManager.addTask(t);
        }
        this.everyAnimFrameTime = everyAnimFrameTime;
        isKilled = false;
        objectCenter = center;
        this.hp = hp;
        size = getSize();
        image.setRotation(0);
        image.setSize(size.x, size.y);
        judgeCircle = new Circle(objectCenter, image.getWidth() / 4);
        FightScreen.instence.groupNormal.addActor(image);
        image.setZIndex(Data.zIndexEnemyPlane);
        objectName = "chunhu";
        targetPosition = center.cpy();
        this.everyAnimFrameTime = everyAnimFrameTime;
        animNum = junkoAnim;
    }

    public void set0Size() {
        image.setSize(0, 0);
    }

    public Vector2 getSize() {
        return new Vector2(96, 128);
    }

    public void hit(float bulletDamage) {
		hp -= bulletDamage / 7;
        if (hp < 1) {
            kill();
        } 
    }

    public void moveTo(float x, float y) {
        targetPosition.x = x;
        targetPosition.y = y;
    }

    private void setStatus(MoveStatus mov) {
        if (mov == status) {
            return;
        }
        status = mov;
        switch (status) {
            case stay:
                flip = false;
                animFrom = animNum[0][0];
                animTo = animNum[0][1];
                curFrameNumber = animFrom;
                break;
            case moveLeft:
                flip = true;
                animFrom = animNum[1][0];
                animTo = animNum[1][1];
                curFrameNumber = animFrom;
                break;
            case moveRight:
                flip = false;
                animFrom = animNum[1][0];
                animTo = animNum[1][1];
                curFrameNumber = animFrom;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    @Override
    public void kill() {
        super.kill();
      //  image.remove();
     //   toDelete.add(this);
      //  isKilled = true;
        FightScreen.instence.reflexAndThroughs.clear();
      //  if (FightScreen.instence.onBoss) {
     //       FightScreen.instence.boss = null;
      //  }
    }

    @Override
    public void update() {
        super.update();
        ++time;
        ++animFlag;
        if (taskManager != null) {
            taskManager.update();
        }
        anim();
        image.setPosition(objectCenter.x, objectCenter.y, Align.center);
        judgeCircle.setPosition(objectCenter.x, objectCenter.y);
        if (judgeCircle.x < -5 || judgeCircle.x > GameMain.width + 5 || judgeCircle.y < -5 || judgeCircle.y > GameMain.height + 5) {
            kill();
        } else {
            judge();
        }
        if (objectCenter.cpy().sub(targetPosition).len2() > 10) {
            objectCenter.add(targetPosition.cpy().sub(objectCenter).nor().scl(3f));
        }
        //    normalDanmaku.updateAll();
        // spellCard.updateAll();
    }

    private void anim() {
        if (objectCenter.x > enemyLastX) {
            enemyLastX = objectCenter.x;
            setStatus(MoveStatus.moveRight);
        } else if (objectCenter.x < enemyLastX) {
            enemyLastX = objectCenter.x;
            setStatus(MoveStatus.moveLeft);
        } else {
            setStatus(MoveStatus.stay);
        }
        if (time >= everyAnimFrameTime) {
            ++curFrameNumber;
            time = 0;
        }
        if (curFrameNumber > animTo) {
            curFrameNumber = animFrom + 2;
        }
        if (flip) {
            image.setDrawable(ResourcesManager.flipedTextures.get(objectName + curFrameNumber));
        } else {
            image.setDrawable(ResourcesManager.textures.get(objectName + curFrameNumber));
        }
    }

    public Shape2D getJudgeCircle() {
        return judgeCircle;
    }

    private void judge() {
        if (getJudgeCircle().contains(MyPlaneReimu.instance.objectCenter)) {
            MyPlaneReimu.instance.kill();
        }
    }

    public static void updateAll() {
        while (!toDelete.isEmpty()) {
            instances.remove(toDelete.poll());
        }
        while (!toAdd.isEmpty()) {
            instances.add(toAdd.poll());
        }
        for (Enemy e : instances) {
            e.update();
        }
    }
}
