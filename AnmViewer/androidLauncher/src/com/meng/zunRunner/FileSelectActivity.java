package com.meng.zunRunner;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.text.*;
import javax.xml.transform.*;
import com.meng.gui.ui.*;
import android.widget.*;

public class FileSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, 22);
		
	  }
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == 22) {
            Uri inputUri = data.getData();
            String path = ContentHelper.absolutePathFromUri(this, inputUri);
            if (!TextUtils.isEmpty(path)) {
                PicMain.anmPath=path;
				startActivity(new Intent(this,MainActivity.class));
				finish();
			  } else {
                Toast.makeText(this,"路径未找到",Toast.LENGTH_SHORT).show();
			  }
		  }
	  }
  }
