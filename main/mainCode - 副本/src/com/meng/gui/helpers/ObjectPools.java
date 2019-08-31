package com.meng.gui.helpers;

import com.badlogic.gdx.utils.Pool;
import com.meng.zunRunner.ecl.helper.EclBullet;
import com.badlogic.gdx.math.*;
import com.meng.gui.bullets.ReimuShoot;
import com.meng.gui.bullets.PersuationNeedle;
import com.meng.gui.bullets.laser.FixLaser;
import com.meng.gui.bullets.laser.ShootLaser;

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

	public static Pool<PersuationNeedle> reimuSubPlaneBulletStraightPool = new Pool<PersuationNeedle>(
			64) {
		@Override
		protected PersuationNeedle newObject() {
			return new PersuationNeedle();
		}
	};

}
