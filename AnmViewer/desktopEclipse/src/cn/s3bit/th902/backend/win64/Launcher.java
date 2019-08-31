package cn.s3bit.th902.backend.win64;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.meng.gui.helpers.ResourcesManager;
import com.meng.gui.ui.GameMain;
import com.meng.gui.ui.PicMain;

public class Launcher {
	public static void main(String[] args) {
		boolean showPic = true; 
		ResourcesManager.pathBase="F:\\project\\ZunRunner\\2un\\";
		if (showPic) {  
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.title = "SJF";
			config.width = PicMain.width;
			config.height = PicMain.height;
			config.vSyncEnabled = false;
			config.resizable = true;
			config.foregroundFPS = 60;
		//	config.addIcon("textures/beammid2.png", Files.FileType.Internal);
			new LwjglApplication(new PicMain(), config);
		} else { 
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.title = "SJF";
			config.width = GameMain.width;
			config.height = GameMain.height;
			config.vSyncEnabled = false;
			config.resizable = true;
			config.foregroundFPS = 60;
			//config.addIcon("textures/beammid2.png", Files.FileType.Internal);
			new LwjglApplication(new GameMain(), config);
		}
	}
}
