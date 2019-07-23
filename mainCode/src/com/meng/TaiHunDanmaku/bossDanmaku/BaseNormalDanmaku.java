package com.meng.TaiHunDanmaku.bossDanmaku;

import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.Junko;

public abstract class BaseNormalDanmaku{

    public Junko boss;
    public int spellTime=1000;
    public BulletShooter[] shooters;
    public int frame=0;

	public int waitFrameNormal=60;

    public abstract void init(Junko boss);

    public void update(){
		if(waitFrameNormal -->0){
			return;
		  }	
	  }
  }
