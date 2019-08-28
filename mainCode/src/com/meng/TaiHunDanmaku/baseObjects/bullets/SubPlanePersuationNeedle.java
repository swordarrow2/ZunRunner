package com.meng.TaiHunDanmaku.baseObjects.bullets;

import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.baseObjects.bullets.BaseMyBullet;
import com.meng.TaiHunDanmaku.helpers.ObjectPools;
import com.meng.TaiHunDanmaku.helpers.ResourcesManager;
import com.meng.TaiHunDanmaku.helpers.TextureNameManager;

public class SubPlanePersuationNeedle extends BaseMyBullet {

    public void init(Vector2 center,float damage,Vector2 velocity,int drawable) { 
        super.init(center, velocity);
        this.damage=damage;
       // image.setDrawable(ResourcesManager.textures.get(drawable));
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
        return velocity.angle();
    }
}
