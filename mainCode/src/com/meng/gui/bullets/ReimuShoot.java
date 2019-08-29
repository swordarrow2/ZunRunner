package com.meng.gui.bullets;

import com.badlogic.gdx.math.Vector2;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.gui.helpers.TextureNameManager;

public class ReimuShoot extends BaseMyBullet{

    public void init(Vector2 center,float damage, Vector2 velocity,int drawable) {
        super.init(center, velocity);
        this.damage=damage;
        image.setDrawable(ResourcesManager.textures.get(TextureNameManager.ReimuBullet));
     //   image.setDrawable(ResourcesManager.textures.get(drawable));
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
