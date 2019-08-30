package com.meng.gui.planes;

import com.meng.gui.BaseGameObject;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.gui.ui.*;
import com.badlogic.gdx.utils.*;
import com.meng.gui.helpers.*;

public class Sheild extends BaseGameObject {

	private int stat = 0;
	private float rotatePerFrame;

	public Sheild() {
	}

	public void init(String textureName, float rotatePerFrame, boolean highLight) {
		super.init();
		this.rotatePerFrame = rotatePerFrame;
		image.setDrawable(ResourcesManager.textures.get(textureName));
		if (highLight) {
			FightScreen.instence.groupHighLight.addActor(image);
		} else {
			FightScreen.instence.groupNormal.addActor(image);
		}
		image.setSize(image.getDrawable().getMinWidth(), image.getDrawable().getMinWidth());
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
		stat += rotatePerFrame;
		image.setPosition(objectCenter.x, objectCenter.y, Align.center);
	}
}
