package com.meng.TaiHunDanmaku.helpers;

import com.badlogic.gdx.utils.Pool;
import com.InsProcess.EclBullet;
import com.badlogic.gdx.math.*;
import com.meng.TaiHunDanmaku.baseObjects.bullets.ReimuShoot;
import com.meng.TaiHunDanmaku.baseObjects.bullets.SubPlanePersuationNeedle;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.FixLaser;
import com.meng.TaiHunDanmaku.baseObjects.bullets.enemy.ShootLaser;

public final class ObjectPools {

	public static RandomXS128 randomPool = new RandomXS128(9961);

	public static Pool<EclBullet> eclBulletPool = new Pool<EclBullet>(8192) {
		@Override
		protected EclBullet newObject() {
			return new EclBullet();
		}
	};
	
	public static Pool<ShootLaser> shootLaserPool=new Pool<ShootLaser>(1024){
		protected ShootLaser newObject() {
			return new ShootLaser();
		}
	};
	
	public static Pool<FixLaser> fixLaserPool=new Pool<FixLaser>(1024){
		protected FixLaser newObject() {
			return new FixLaser();
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
