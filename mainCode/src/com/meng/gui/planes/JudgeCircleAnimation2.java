package com.meng.gui.planes;

import com.badlogic.gdx.utils.*;
import com.meng.gui.*;
import com.meng.gui.helpers.*;
import com.meng.gui.ui.*;

public class JudgeCircleAnimation2 extends BaseGameObject{
    private int stat=0;

    public JudgeCircleAnimation2(){
	  }

    @Override
    public void init(){
        super.init();
        image.setDrawable(ResourcesManager.textures.get("effect24"));
		image.setSize(48,48);
		image.setOrigin(image.getWidth()/2,image.getHeight()/2);
        FightScreen.instence.groupNormal.addActor(image);
		image.setZIndex(Data.zIndexJudgePoint);
	  }

    @Override
    public void update(){
		objectCenter= MyPlaneReimu.instance.objectCenter;
        image.setRotation(stat);
		if(MyPlaneReimu.instance.slow){
			image.setSize(48,48);
		  }else{
			image.setSize(0,0);
		  }
        stat-=2;
		image.setPosition(objectCenter.x,objectCenter.y,Align.center);
	  }

    @Override
    public void kill(){
        super.kill();
	  }
  }
