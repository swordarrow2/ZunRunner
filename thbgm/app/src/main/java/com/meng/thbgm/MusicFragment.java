package com.meng.thbgm;

import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.meng.thbgm.fileRead.Helper;
import com.meng.thbgm.fileRead.THfmt;

import java.io.File;

public class MusicFragment extends ListFragment implements AdapterView.OnItemLongClickListener {

    public String name;
    public AudioTrack trackplayer = null;
    private THfmt thfmt = null;

    public MusicFragment() {
        this("");
    }

    public MusicFragment(String name) {
        this.name = name;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        File fmt = new File(MainActivity.mainFloder + name + "/thbgm.fmt");
        thfmt = new THfmt(fmt);
        thfmt.load();
        //    Toast.makeText(getActivity(), fmt.getAbsolutePath(), Toast.LENGTH_LONG).show();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, thfmt.names));
        getListView().setOnItemLongClickListener(this);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        getListView().setItemChecked(position, true);
        if (trackplayer != null && trackplayer.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            trackplayer.stop();
            trackplayer.release();
            trackplayer = null;
        }
        THfmt.MusicInfo musicInfo = thfmt.musicInfos[position];
        int start = musicInfo.start;
        int bufsize = musicInfo.length;
        byte[] data = new byte[bufsize];
		
		//File f=new File(MainActivity.mainFloder + name + "/thbgm.dat");
		//data=new byte[(int)f.length()];
		//start=0;
		
        Helper.readFile(data, start, name);
        trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC,
                musicInfo.rate,
                musicInfo.channels == 2 ? AudioFormat.CHANNEL_CONFIGURATION_STEREO : AudioFormat.CHANNEL_CONFIGURATION_MONO,
                musicInfo.bitsPerSample == 16 ? AudioFormat.ENCODING_PCM_16BIT : AudioFormat.ENCODING_PCM_8BIT, bufsize, AudioTrack.MODE_STATIC);
        trackplayer.write(data, 0, data.length);
        // 1 frame = bytesPerSample * channels
        int frameLength = musicInfo.bitsPerSample / 8 * musicInfo.channels;
        int startFrame = musicInfo.repeatStart / frameLength;
        int endFrame = musicInfo.length / frameLength;
        Toast.makeText(getActivity(), "fileOffset:" + musicInfo.start + " len:" + musicInfo.length + " loopAtMusicOffset:" + musicInfo.repeatStart + "(" + (1f * startFrame / endFrame * 100) + "%)", Toast.LENGTH_LONG).show();
        trackplayer.setLoopPoints(startFrame, endFrame, 1000);
        trackplayer.play();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", thfmt.names[position]);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(getActivity(), "已复制到剪贴板", Toast.LENGTH_SHORT).show();
        return true;
    }
}
