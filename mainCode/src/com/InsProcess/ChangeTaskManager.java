package com.InsProcess;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import java.util.*;

public class ChangeTaskManager {

	private EnemyBullet bullet;
	private ChangeTask[] taskList=new ChangeTask[16];
	private int nowTask=0;
	public ChangeTaskManager(EnemyBullet enemyBullet) {
		bullet = enemyBullet;
	  }

	public void addChange(int num, int way, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn) {
		taskList[num] = new ChangeTask(way, mode, inta, intb, intc, intd, floatm, floatn, floatr, floats);
	  }
	  
	public void addChange(int way, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn) {
		taskList[nowTask++] = new ChangeTask(way, mode, inta, intb, intc, intd, floatm, floatn, floatr, floats);
	  }
	  
	  public void update(){
		
	  }
  }
