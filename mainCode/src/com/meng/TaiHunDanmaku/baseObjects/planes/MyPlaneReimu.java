package com.meng.TaiHunDanmaku.baseObjects.planes;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.meng.TaiHunDanmaku.BaseGameObject;
import com.meng.TaiHunDanmaku.helpers.Data;
import com.meng.TaiHunDanmaku.helpers.ObjectPools;
import com.meng.TaiHunDanmaku.ui.FightScreen;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class MyPlaneReimu extends BaseGameObject {

    public static MyPlaneReimu instance;

    private JudgeCircleAnimation animation = null;
    private JudgeCircleAnimation2 animation2 = null;
    private Vector2 mainShooterVelocity = new Vector2(0, 47);
    private float playerLastX = 270;
    public boolean slow = false;
    private AnimationManager animationManager;
    private SubPlaneReimu subPlane1, subPlane2, subPlane3, subPlane4;

    private GameMain gameMain;

    public void init(GameMain gameMain) {
        super.init();
        instance = this;
        this.gameMain = gameMain;
        animation = new JudgeCircleAnimation();
        animation.init();
        animation2 = new JudgeCircleAnimation2();
        animation2.init();
        existTime = 0;
        objectCenter.set(GameMain.width / 2, 80);
        image.setSize(30, 46);
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        FightScreen.instence.groupNormal.addActor(image);
        image.setZIndex(Data.zIndexMyPlane);
        animationManager = new AnimationManager(this, 5);
        subPlane4 = new SubPlaneReimu().init(this, 4);
        subPlane3 = new SubPlaneReimu().init(this, 3);
        subPlane2 = new SubPlaneReimu().init(this, 2);
        subPlane1 = new SubPlaneReimu().init(this, 1);
    }

    @Override
    public void kill() {
        super.kill();
        ++gameMain.miss;
    }

    @Override
    public void update() {
        super.update();
        animFlag++;
        objectCenter = new Vector2(MathUtils.clamp(objectCenter.x, 10, GameMain.width - 10), MathUtils.clamp(objectCenter.y, 10, GameMain.height - 10));
        if (image.getRotation() != 0) {
            image.setRotation(0);
        }
        image.setPosition(objectCenter.x, objectCenter.y, Align.center);
        shoot();
        if (objectCenter.x > playerLastX) {
            playerLastX = objectCenter.x;
            animationManager.setStatus(MoveStatus.moveRight);
        } else if (objectCenter.x < playerLastX) {
            playerLastX = objectCenter.x;
            animationManager.setStatus(MoveStatus.moveLeft);
        } else {
            animationManager.setStatus(MoveStatus.stay);
        }
        animationManager.update();
        image.toBack();
        animation2.update();
        animation.update();
        subPlane4.update();
        subPlane3.update();
        subPlane2.update();
        subPlane1.update();
    }

    private void shoot() {
        if (existTime % 3 == 1) {
            ObjectPools.reimuShootPool.obtain().init(new Vector2(objectCenter.x + 8, objectCenter.y + 32), mainShooterVelocity);
            ObjectPools.reimuShootPool.obtain().init(new Vector2(objectCenter.x - 8, objectCenter.y + 32), mainShooterVelocity);
        }
    }

}
