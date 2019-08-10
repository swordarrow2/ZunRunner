package com.meng.TaiHunDanmaku.task;


import com.meng.TaiHunDanmaku.baseObjects.planes.*;
import com.meng.TaiHunDanmaku.helpers.*;
import com.meng.TaiHunDanmaku.ui.*;
import java.util.*;

public class TaskManagerEnemyPlane {
    private TaskRepeatMode repeatMode;
    private Enemy junko;
    private int holdingTime = 0;
    private int addTaskFlag = 0;
    private int getTaskFlag = 0;
    private HashMap<Integer, Task> tasks;

    public TaskManagerEnemyPlane(Enemy junko, TaskRepeatMode repeatMode) {
        this.repeatMode = repeatMode;
        this.junko = junko;
        tasks = new HashMap<>();
	  }

    public void addTask(Task t) {
        tasks.put(addTaskFlag, t);
        addTaskFlag++;
	  }

    public void update() {
        switch (repeatMode) {
            case repeatAll:
			  if (getTaskFlag < addTaskFlag) {
				  if (tasks.get(getTaskFlag).holdingTime - holdingTime > 0) {
					  doTask(tasks.get(getTaskFlag));
                    } else {
					  holdingTime = 0;
					  getTaskFlag++;
                    }
                } else {
				  holdingTime = 0;
				  getTaskFlag = 0;
                }
			  break;
            case repeatLast:
			  if (getTaskFlag < addTaskFlag) {
				  if (tasks.get(getTaskFlag).holdingTime - holdingTime > 0) {
					  doTask(tasks.get(getTaskFlag));
                    } else {
					  holdingTime = 0;
					  getTaskFlag++;
                    }
                } else {
				  holdingTime = 0;
				  getTaskFlag = addTaskFlag - 1;
                }
			  break;
            case noRepeat:
			  if (getTaskFlag < addTaskFlag) {
				  if (tasks.get(getTaskFlag).holdingTime - holdingTime > 0) {
					  doTask(tasks.get(getTaskFlag));
                    } else {
					  holdingTime = 0;
					  getTaskFlag++;
                    }
                }
			  break;
		  }
	  }

    public void doTask(Task task) {

        if (task instanceof TaskMoveTo) {
            if (task.tmpVector2.x == 10000 && task.tmpVector2.y == 10000) {
                junko.moveTo(ObjectPools.randomPool.nextInt(GameMain.width /2) + 200, ObjectPools.randomPool.nextInt(GameMain.height /2) + 200);
			  } else {
                junko.moveTo(task.tmpVector2.x, task.tmpVector2.y);
			  }
		  } else if (task instanceof TaskShoot) {
            for (int i = 0; i < task.bulletShooter.length; ++i) {
                task.bulletShooter[i].shoot();
			  }
		  } else if (task instanceof TaskRunnable) {
            task.runnable.run();
		  }
        holdingTime++;
	  }
  }

