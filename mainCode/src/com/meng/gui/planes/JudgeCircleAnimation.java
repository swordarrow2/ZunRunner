package com.meng.gui.planes;

import com.meng.gui.BaseGameObject;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.gui.ui.*;
import com.badlogic.gdx.utils.*;
import com.meng.gui.helpers.*;

public class JudgeCircleAnimation extends BaseGameObject {

	private int stat = 0;

	public JudgeCircleAnimation() {
	}

	@Override
	public void init() {
		super.init();
		image.setDrawable(ResourcesManager.textures.get("effect23"));
		FightScreen.instence.groupNormal.addActor(image);
		image.setSize(48, 48);
		image.setZIndex(Data.zIndexJudgePoint);
		image.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
	}

	@Override
	public void kill() {
		super.kill();
	}

	@Override
	public void update() {
		objectCenter = MyPlaneReimu.instance.objectCenter;
		image.setRotation(stat);
		if (MyPlaneReimu.instance.slow) {
			image.setSize(48, 48);
		} else {
			image.setSize(0, 0);
		}
		stat += 2;
		image.setPosition(objectCenter.x, objectCenter.y, Align.center);
		image.toFront();
	}
}
