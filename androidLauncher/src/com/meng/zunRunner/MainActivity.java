package com.meng.zunRunner;

import android.os.*;
import android.widget.*;
import com.InsProcess.pack.*;
import com.InsProcess.parse.*;
import com.badlogic.gdx.backends.android.*;
import com.meng.TaiHunDanmaku.ui.*;
import java.io.*;
import android.app.*;

public class MainActivity extends Activity{//AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		GameMain.baseEclPath="/storage/emulated/0/AppProjects/ZunRunner/ecl/";
      //  setContentView(initializeForView(new GameMain(), new AndroidApplicationConfiguration()));
	//  StringBuilder sb=new StringBuilder();
		Ecl ecl=new Ecl("st06.ecl");
		ScrollView s=new ScrollView(this);
		HorizontalScrollView hs=new HorizontalScrollView(this);
		TextView t=new TextView(this);
		t.setText(ecl.toString());
		s.addView(t);
		hs.addView(s);
		setContentView(hs);
	  }
  }
