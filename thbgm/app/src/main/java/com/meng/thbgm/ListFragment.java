package com.meng.thbgm;

import android.app.*;
import android.content.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import com.meng.thbgm.fileRead.*;

import java.io.*;

public class ListFragment extends android.app.ListFragment {

    private boolean dualPane; // 是否在一屏上同时显示列表和详细内容
    private int curCheckPosition = 0; // 当前选择的索引位置
    private AudioTrack trackplayer=null;
    private TH10fmt th10fmt=null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_checked, Data.TITLES)); // 为列表设置适配器
        View detailFrame = getActivity().findViewById(R.id.detail); // 获取布局文件中添加的FrameLayout帧布局管理器
        dualPane = detailFrame != null && detailFrame.getVisibility() == View.VISIBLE; // 判断是否在一屏上同时显示列表和详细内容
        if (savedInstanceState != null) {
            curCheckPosition = savedInstanceState.getInt("curChoice", 0); // 更新当前选择的索引位置
        }
        if (dualPane) { // 如果在一屏上同时显示列表和详细内容
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 设置列表为单选模式
            showDetails(curCheckPosition); // 显示详细内容
        }
        th10fmt = new TH10fmt(new File(Environment.getExternalStorageDirectory() + "/thbgm.fmt"));
        th10fmt.load();
    }

    // 重写onSaveInstanceState()方法，保存当前选中的列表项的索引值
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", curCheckPosition);
    }

    // 重写onListItemClick()方法
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(trackplayer==null){
            TH10fmt.MusicInfo musicInfo = th10fmt.musicInfos[1];
            int start = musicInfo.start;
            int bufsize = musicInfo.end - start;
            bufsize <<= 2;
            byte[] data = new byte[bufsize];
            Helper.readFile(data, start);
            trackplayer = new AudioTrack(AudioManager.STREAM_MUSIC, musicInfo.rate, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT, bufsize, AudioTrack.MODE_STATIC);
            trackplayer.write(data, 0, data.length);//往track中写数据
            //   trackplayer.stop();//停止播放
            //    trackplayer.release();//释放底层资源。
            trackplayer.play();//开始
        }
        //  showDetails(position); // 调用showDetails()方法显示详细内容
    }

    @Override
    public void onPause() {
        super.onPause();
        trackplayer.stop();
        trackplayer.release();
    }

    void showDetails(int index) {
        curCheckPosition = index; // 更新保存当前索引位置的变量的值为当前选中值

        if (dualPane) { // 当在一屏上同时显示列表和详细内容时
            getListView().setItemChecked(index, true); // 设置选中列表项为选中状态
            DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.detail); // 获取用于显示详细内容的Fragment
            if (details == null || details.getShownIndex() != index) { // 如果如果
                details = DetailFragment.newInstance(index); // 创建一个新的DetailFragment实例用于显示当前选择项对应的详细内容
                // 要在activity中管理fragment, 需要使用FragmentManager
                FragmentTransaction ft = getFragmentManager().beginTransaction();// 获得一个FragmentTransaction的实例
                ft.replace(R.id.detail, details); // 替换原来显示的详细内容
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE); // 设置转换效果
                ft.commit(); // 提交事务
            }

        } else { // 在一屏上只能显示列表或详细内容中的一个内容时
            // 使用一个新的Activity显示详细内容
            Intent intent = new Intent(getActivity(), DetailActivity.class); // 创建一个Intent对象
            intent.putExtra("index", index); // 设置一个要传递的参数
            startActivity(intent); // 开启一个指定的Activity
        }
    }

}
