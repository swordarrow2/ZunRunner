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
	  StringBuilder sb=new StringBuilder();
		EclFile eclFile=new EclFile("st06.ecl");
	
		ScrollView s=new ScrollView(this);
		TextView t=new TextView(this);
		t.setText(eclFile.getSubNames());
		s.addView(t);
		setContentView(s);
	  }
  }
