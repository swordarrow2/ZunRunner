package com.meng.zunRunner;

import android.os.*;
import android.widget.*;
import com.InsProcess.*;
import com.badlogic.gdx.backends.android.*;
import java.io.*;

public class MainActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initializeForView(new MyGame(), new AndroidApplicationConfiguration()));
		ScrollView s=new ScrollView(this);
		TextView t=new TextView(this);
		t.setText(new UnpackedEclParser(new File(Environment.getExternalStorageDirectory()+"/tencent/QQfile_recv/st06bs.txt")).parse());
		s.addView(t);
		setContentView(s);
	  }
  }
