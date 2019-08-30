package com.meng.gui.task;

import com.meng.zunRunner.ecl.helper.EclBulletShooter;
import com.badlogic.gdx.math.Vector2;

public class Task {
    public int holdingTime = 1;
    public EclBulletShooter[] bulletShooter;
    public Vector2 tmpVector2 = new Vector2();
    public Runnable runnable;
    public ChangeMode changeMode;
}
