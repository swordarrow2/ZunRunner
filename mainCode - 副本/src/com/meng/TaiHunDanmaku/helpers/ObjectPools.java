package com.meng.TaiHunDanmaku.helpers;

import com.badlogic.gdx.utils.Pool;
import com.InsProcess.EclBullet;
import com.badlogic.gdx.math.*;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.EnemyBullet;
import com.meng.TaiHunDanmaku.baseObjects.bullets.ReimuShoot;
import com.meng.TaiHunDanmaku.baseObjects.bullets.SubPlanePersuationNeedle;

public final class ObjectPools {

	public static RandomXS128 randomPool = new RandomXS128(9961);

	public static Pool<EnemyBullet> enemyBulletPool = new Pool<EnemyBullet>(8192) {
		@Override
		protected EnemyBullet newObject() {
			return new EnemyBullet();
		}
	};
	public static Pool<EclBullet> eclBulletPool = new Pool<EclBullet>(8192) {
		@Override
		protected EclBullet newObject() {
			return new EclBullet();
		}
	};

	public static Pool<ReimuShoot> reimuShootPool = new Pool<ReimuShoot>(64) {
		@Override
		protected ReimuShoot newObject() {
			return new ReimuShoot();
		}
	};

	public static Pool<SubPlanePersuationNeedle> reimuSubPlaneBulletStraightPool = new Pool<SubPlanePersuationNeedle>(
			64) {
		@Override
		protected SubPlanePersuationNeedle newObject() {
			return new SubPlanePersuationNeedle();
		}
	};

}
