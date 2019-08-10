package com.meng.TaiHunDanmaku.bossDanmaku;

import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.baseObjects.planes.Enemy;
import java.util.*;

public abstract class BaseNormalDanmaku{

    public Enemy boss;
    public int spellTime=1000;
    public HashMap<Integer,BulletShooter> shooters;
    public int frame=0;

	public int waitFrameNormal=60;

    public abstract void init(Enemy boss);

    public void update(){
		if(waitFrameNormal -->0){
			return;
		  }	
	  }
  }
