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
