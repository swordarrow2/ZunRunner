package com.meng.gui.planes;

import com.badlogic.gdx.math.Vector2;

public class MapleEnemy extends Enemy {

    @Override
    public void init() {
        super.init();
        objectCenter = new Vector2(-1000,-1000);
        hp = 1;
        image.setSize(0,0);
    }

    @Override
    public void update() {

    }
}
