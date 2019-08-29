package com.meng.zunRunner.deskTopLauncher;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.meng.gui.ui.GameMain;
import com.meng.gui.ui.PicMain;
import com.meng.gui.ui.PicScreen;
import com.meng.insLogic.helper.DataHelper;
import com.meng.zunRunner.anm.AnmBean;
import com.meng.zunRunner.anm.AnmFile;

public class Main {

    public static void main(String[] args) {

        boolean showPic = true;
        String game = "th15";
        if (showPic) {
            GameMain.baseEclPath = "F:\\project\\ZunRunner\\2un\\" + game + "\\ecl\\";
            GameMain.baseShtPath = "F:\\project\\ZunRunner\\2un\\" + game + "\\sht\\";
            GameMain.baseAnmPath = "F:\\project\\ZunRunner\\2un\\" + game + "\\anm\\";
            AnmFile thanm = new AnmFile("pl00sub.anm");
            AnmBean anmBean = thanm.anmBeans.get(0);
            int[] rgbaArray;
            switch (anmBean.thtx.format) {
                case 1:
                    rgbaArray = DataHelper.argb8888ToRgba8888(anmBean.thtx.data);
                    break;
                case 3:
                    rgbaArray = DataHelper.rgb565ToRgba8888(anmBean.thtx.data);
                    break;
                case 5:
                    rgbaArray = DataHelper.argb4444ToRgba8888(anmBean.thtx.data);
                    break;
                case 7:
                    rgbaArray = DataHelper.gray8ToRgba8888(anmBean.thtx.data);
                    break;
                default:
                    throw new RuntimeException("pic:" + anmBean.picName + " unexpect value:" + anmBean.thtx.format);
            }
            PicMain.width=anmBean.thtx.w;
            PicMain.height=anmBean.thtx.h;
            PicScreen.anmBean=anmBean;
            PicScreen.rgbaArray=rgbaArray;
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "SJF";
            config.width = PicMain.width;
            config.height = PicMain.height;
            config.vSyncEnabled = false;
            config.resizable = true;
            config.foregroundFPS = 60;
            config.addIcon("textures/beammid2.png", Files.FileType.Internal);
            new LwjglApplication(new PicMain(), config);
        } else {
            GameMain.baseEclPath = "F:\\project\\ZunRunner\\2un\\" + game + "\\ecl\\";
            GameMain.baseShtPath = "F:\\project\\ZunRunner\\2un\\" + game + "\\sht\\";
            GameMain.baseAnmPath = "F:\\project\\ZunRunner\\2un\\" + game + "\\anm\\";
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "SJF";
            config.width = GameMain.width;
            config.height = GameMain.height;
            config.vSyncEnabled = false;
            config.resizable = true;
            config.foregroundFPS = 60;
            config.addIcon("textures/beammid2.png", Files.FileType.Internal);
            new LwjglApplication(new GameMain(), config);
        }
    }
}
