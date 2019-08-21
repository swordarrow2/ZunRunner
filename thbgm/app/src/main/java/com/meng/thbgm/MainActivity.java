package com.meng.thbgm;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.meng.thbgm.fileRead.Helper;
import com.meng.thbgm.fileRead.TH10fmt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); // 设置使用的布局内容
        //根据采样率，采样精度，单双声道来得到frame的大小。
        // int bufsize = AudioTrack.getMinBufferSize(8000,//每秒8K个点
        //         AudioFormat.CHANNEL_CONFIGURATION_STEREO,//双声道
        //         AudioFormat.ENCODING_PCM_16BIT);//一个采样点16比特-2个字节
        //注意，按照数字音频的知识，这个算出来的是一秒钟buffer的大小。
        //创建AudioTrack
        TH10fmt th10fmt = new TH10fmt(new File(Environment.getExternalStorageDirectory() + "/thbgm.fmt"));
        th10fmt.load();
        int start = th10fmt.musicInfos[0].start;
        int end = th10fmt.musicInfos[0].end;
        int bufsize = end - start;
        byte[] data = new byte[bufsize];
        StringBuilder stringBuilder=new StringBuilder();
        for(byte b:data){
      //      stringBuilder.append(Integer.toHexString(b&0xff)).append(" ");
        }
        Toast.makeText(this,data.length+"",Toast.LENGTH_LONG).show();
        Helper.readFile(data, start);
        final AudioTrack trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC, th10fmt.musicInfos[0].rate,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufsize,
                AudioTrack.MODE_STATIC);
        trackplayer.write(data, 0, data.length);//往track中写数据
        trackplayer.play();//开始
     //   trackplayer.stop();//停止播放
    //    trackplayer.release();//释放底层资源。
    }
}