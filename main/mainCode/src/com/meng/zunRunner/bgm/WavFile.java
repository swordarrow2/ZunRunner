package com.meng.zunRunner.bgm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.meng.zunRunner.bgm.FmtFile.MusicInfo;

public class WavFile {
	private FileHandle fileHandle;
	private FmtFile fmtFile;

    
	public WavFile(String fileName, final FmtFile fmtFile) {
		this.fmtFile = fmtFile;
		fileHandle = Gdx.files.absolute("F:\\111111111111\\th10\\1111.wav");
		Music music = Gdx.audio.newMusic(fileHandle);
		music.setOnCompletionListener(new Music.OnCompletionListener() {
			
			@Override
			public void onCompletion(Music music) {
				MusicInfo musicInfo=fmtFile.musicInfos[0];
				int oneSecond=musicInfo.rate*musicInfo.channels*musicInfo.bitsPerSample/8;
				float second=((float)musicInfo.length)/oneSecond;
				music.setPosition(second);
		music.play();
			}
		});
		music.play();
	}
}
