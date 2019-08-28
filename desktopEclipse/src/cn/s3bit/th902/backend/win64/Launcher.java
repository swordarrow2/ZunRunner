package cn.s3bit.th902.backend.win64;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.meng.TaiHunDanmaku.ui.GameMain;
import com.meng.sht.ShtFile;
import com.badlogic.gdx.Files;

/**
 * @author Obsidianss
 * <p>
 * The launcher of the game and the wrapper of LwjglApplication.
 * </p>
 */
public class Launcher {  
	public static void main(String[] args) {
		  GameMain.baseEclPath = "F:\\project\\ZunRunner\\ecl\\";
		  GameMain.baseShtPath = "F:\\project\\ZunRunner\\sht\\";
	        /*  GameMain.baseEclPath = "F:\\project\\ZunRunner\\subed\\";*/
	        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	        config.title = "SJF";
	        config.width = GameMain.width;
	        config.height = GameMain.height;
	        config.vSyncEnabled = false;
	        config.resizable = true;
	        config.foregroundFPS = 60;
	        config.addIcon("textures/beammid2.png", Files.FileType.Internal);
	        new LwjglApplication(new GameMain(), config);
	       // System.out.println(new ShtFile("pl03.sht"));
	        // new zunRunner() ;
	        //  EclFile eclFile=new EclFile("st06bs.ecl");
	        //  System.out.println(eclFile.toString());
	}
}
