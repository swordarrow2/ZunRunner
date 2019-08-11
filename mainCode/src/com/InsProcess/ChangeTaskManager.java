package com.InsProcess;

import com.InsProcess.parse.beans.EclIns;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.*;
import com.meng.TaiHunDanmaku.ui.GameMain;

public class ChangeTaskManager {

	private EnemyBullet bullet;
	private ChangeTask[] taskList = new ChangeTask[16];
	private int nowTask = 0;
	private int holdingTime = 0;

	public ChangeTaskManager(EnemyBullet enemyBullet) {
		bullet = enemyBullet;
	}

	public void addChange(int num, int way, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn) {
		taskList[num] = new ChangeTask(way, mode, inta, intb, intc, intd, floatm, floatn, floatr, floats);
	}

	public void addChange(int way, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn) {
		taskList[nowTask++] = new ChangeTask(way, mode, inta, intb, intc, intd, floatm, floatn, floatr, floats);
	}

	public void update() {
		ChangeTask task = taskList[nowTask];
		if (task != null) {
			doTask(task);
		} else {
			nowTask = 0;
		}
	}

	public void doTask(ChangeTask task) {
		switch (task.mode) {
		case 1: // 1<<0
			break;
		case 2: // 1<<1
			break;
		case 4: // 1<<2
			break;
		case 8: // 1<<3
			break;
		case 16: // 1<<4
			break;
		case 32: // 1<<5
			break;
		case 64: // 1<<6
			break;
		case 128: // 1<<7
			break;
		case 256: // 1<<8
			break;
		case 512: // 1<<9
			break;
		case 1024: // 1<<10
			break;
		case 2048: // 1<<11
			break;
		case 4096: // 1<<12
			break;
		case 8192: // 1<<13
			break;
		case 16384: // 1<<14
			break;
		case 32768: // 1<<15
			break;
		case 65536: // 1<<16
			break;
		case 131072: // 1<<17
			break;
		case 262144: // 1<<18
			break;
		case 524288: // 1<<19
			break;
		case 1048576: // 1<<20
			break;
		case 2097152: // 1<<21
			break;
		case 4194304: // 1<<22
			break;
		case 8388608: // 1<<23
			break;
		case 16777216: // 1<<24
			break;
		case 33554432: // 1<<25
			break;
		case 67108864: // 1<<26
			break;
		case 134217728: // 1<<27
			break;
		case 268435456: // 1<<28
			break;
		case 536870912: // 1<<29
			break;
		case 1073741824: // 1<<30
			break;
		case -2147483648: // 1<<31
			if (task.a < holdingTime++) {
				holdingTime = 0;
				++nowTask;
				update();
				break;
			}
			break;
		}

	}
}
