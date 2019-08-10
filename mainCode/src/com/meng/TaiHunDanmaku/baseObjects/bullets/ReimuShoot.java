package com.meng.TaiHunDanmaku.baseObjects.bullets;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pool;
import com.meng.TaiHunDanmaku.helpers.ResourcesManager;
import com.meng.TaiHunDanmaku.baseObjects.bullets.BaseMyBullet;
import com.meng.TaiHunDanmaku.helpers.TextureNameManager;
import com.meng.TaiHunDanmaku.helpers.*;

public class ReimuShoot extends BaseMyBullet{

    @Override
    public void init(Vector2 center, Vector2 velocity) {
        super.init(center, velocity);
        image.setDrawable(ResourcesManager.textures.get(TextureNameManager.ReimuBullet));
    }

    @Override
    public void update(){
        super.update();
        image.toBack();
	  }

    @Override
    public Vector2 getSize(){
        return new Vector2(64,16);
	  }
	
    @Override
    public float getRotationDegree(){
        return 90;
	  }
  }
