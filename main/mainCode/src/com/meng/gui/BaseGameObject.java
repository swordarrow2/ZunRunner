package com.meng.gui;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public abstract class BaseGameObject {

    public Vector2 objectCenter = new Vector2();
    public Circle judgeCircle;
    public Image image = null;
    public int animFlag = 0;
    public Vector2 velocity = new Vector2();
    public int existTime = 0;

    public void update() {
        ++existTime;
    }

    public void init() {
        image = new Image();//ObjectPools.imagePool.obtain();
    }

    public void kill() {
        //ObjectPools.imagePool.free(image);
    }
}
