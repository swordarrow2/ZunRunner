package com.meng.gui.bullets;

import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.meng.gui.BaseGameObject;
import com.meng.gui.ui.GameMain;

public abstract class BaseBullet extends BaseGameObject {

    @Override
    public void init() {
        super.init();
        existTime = 0;
    }

    @Override
    public void update() {
        super.update();
        image.setRotation(getRotationDegree());
        image.setPosition(objectCenter.x, objectCenter.y, Align.center);
        judgeCircle.setPosition(objectCenter);
        if (judgeCircle.x < -5 || judgeCircle.x > GameMain.width + 5 || judgeCircle.y < -5 || judgeCircle.y > GameMain.height + 5) {
            kill();
        } else {
            judge();
        }
    }

    public Shape2D getCollisionArea() {
        return judgeCircle;
    }
    
    public abstract void kill();

    public abstract void judge();

    public abstract float getRotationDegree();

}
