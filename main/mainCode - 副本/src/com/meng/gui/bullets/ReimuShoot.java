package com.meng.gui.bullets;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.gui.helpers.TextureNameManager;

public class ReimuShoot extends BaseMyBullet{

    public void init(Vector2 center,float damage, Vector2 velocity,int drawable) {
        super.init(center, velocity);
        this.damage=damage;
        image.setDrawable(ResourcesManager.textures.get(TextureNameManager.ReimuBullet));
     //   image.setDrawable(ResourcesManager.textures.get(drawable)); 
        image.setSize(image.getDrawable().getMinWidth(), image.getDrawable().getMinHeight());
        image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        image.setPosition(objectCenter.x, objectCenter.y, Align.center);
        judgeCircle = new Circle(objectCenter, image.getWidth() / 2 * 3); //中心、半径
    }

    @Override
    public void update(){
        super.update();
        image.toBack();
	  }
	
    @Override
    public float getRotationDegree(){
        return 90;
	  }
  }
