package com.meng.TaiHunDanmaku.baseObjects.bullets;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.meng.TaiHunDanmaku.baseObjects.bullets.BaseMyBullet;
import com.meng.TaiHunDanmaku.baseObjects.planes.MyPlaneReimu;
import com.meng.TaiHunDanmaku.helpers.ObjectPools;
import com.meng.TaiHunDanmaku.helpers.ResourcesManager;
import com.meng.TaiHunDanmaku.helpers.TextureNameManager;


import com.meng.TaiHunDanmaku.ui.FightScreen;

public class SubPlanePersuationNeedle extends BaseMyBullet {

    @Override
    public void init(Vector2 center, Vector2 velocity) {
        super.init(center, velocity);
        damage=10.5f;
        image.setDrawable(ResourcesManager.textures.get(TextureNameManager.ReimuSubPlaneBulletStraight));
    }

    @Override
    public Vector2 getSize() {
        return new Vector2(64, 16);
    }


    @Override
    public void kill() {
        super.kill();
        ObjectPools.reimuSubPlaneBulletStraightPool.free(this);
    }

    @Override
    public float getRotationDegree() {
        return 90;
    }
}
