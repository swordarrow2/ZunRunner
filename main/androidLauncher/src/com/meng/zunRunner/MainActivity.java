package com.meng.zunRunner;

import android.os.*;
import android.widget.*;
import com.badlogic.gdx.backends.android.*;
import com.meng.gui.ui.*;
import com.meng.zunRunner.ecl.*;
import com.meng.gui.helpers.*;

public class MainActivity extends  AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ResourcesManager.pathBase="/storage/emulated/0/AppProjects/ZunRunner/2un/";
        boolean showPic = true;
        if (showPic) {
        setContentView(initializeForView(new PicMain(), new AndroidApplicationConfiguration()));
    }else{


        setContentView(initializeForView(new GameMain(), new AndroidApplicationConfiguration()));
    //  StringBuilder sb=new StringBuilder();
     //   EclManager ecl=new EclManager("st06.ecl");
        
      //  ScrollView s=new ScrollView(this);
      //  HorizontalScrollView hs=new HorizontalScrollView(this);
      //  TextView t=new TextView(this);
     //   t.setText(ecl.toString());
    //    s.addView(t);
   //     hs.addView(s);
        //setContentView(hs);
        //ecl.start();
      }
      }
  }
