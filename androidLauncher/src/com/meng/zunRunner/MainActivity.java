package com.meng.zunRunner;

import android.os.*;
import android.widget.*;
import com.InsProcess.parse.*;
import com.badlogic.gdx.backends.android.*;
import com.meng.TaiHunDanmaku.ui.*;

public class MainActivity extends  AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean showPic = true;
        String game = "th15";
        if (showPic) {
          GameMain.baseEclPath = "/storage/emulated/0/AppProjects/ZunRunner/2un/ecl/";
          GameMain.baseShtPath = "/storage/emulated/0/AppProjects/ZunRunner/2un/sht/";
          GameMain.baseAnmPath = "/storage/emulated/0/AppProjects/ZunRunner/2un/anm/";
            AnmFile thanm = new AnmFile("st06enm.anm");
            AnmBean anmBean = thanm.anmBeans.get(0);
            int[] rgbaArray = null;
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
        setContentView(initializeForView(new GameMain(), new AndroidApplicationConfiguration()));
    }else{


          GameMain.baseEclPath = "/storage/emulated/0/AppProjects/ZunRunner/2un/ecl/";
          GameMain.baseShtPath = "/storage/emulated/0/AppProjects/ZunRunner/2un/sht/";
          GameMain.baseAnmPath = "/storage/emulated/0/AppProjects/ZunRunner/2un/anm/";
        setContentView(initializeForView(new GameMain(), new AndroidApplicationConfiguration()));
    //  StringBuilder sb=new StringBuilder();
        EclManager ecl=new EclManager("st06.ecl");
        
        ScrollView s=new ScrollView(this);
        HorizontalScrollView hs=new HorizontalScrollView(this);
        TextView t=new TextView(this);
        t.setText(ecl.toString());
        s.addView(t);
        hs.addView(s);
        //setContentView(hs);
        //ecl.start();
      }
      }
  }
