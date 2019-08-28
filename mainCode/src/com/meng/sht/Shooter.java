package com.meng.sht;

import com.badlogic.gdx.math.Vector2;

public class Shooter {
	public byte rate; // 发弹间隔
	public byte delay; // 发弹时间点
	public short power; // 每发子弹的威力
	public Vector2 pos = new Vector2(); // 发弹点相对坐标
	public Vector2 hitBox = new Vector2(); // 子弹判定的长和宽
	public float angle; // 发射方向
	public float speed; // 发射速度
	public int unknown1; // fsl之后加入
	public byte option; // 子机号,0=main
	public byte unknown2;
	public short ANM; // 自机弹贴图
	public short SE; // 音效
	public byte rate2; // rate2
	public byte delay2; // delay2
	public int[] flags = new int[12];
	public static final int size=88;
	@Override
	public String toString() { 
		return rate+" "+delay+" "+power+" "+pos+" "+hitBox+" "+angle+" "+speed+" "+unknown1+" "+option+" "+unknown2+" "+ANM+" "+SE+" "+rate2+" "+delay2+" "+
				flags[0]+" "+flags[1]+" "+flags[2]+" "+flags[3]+" "+flags[4]+" "+flags[5]+" "+flags[6]+" "+flags[7]+" "+
				flags[8]+" "+flags[9]+" "+flags[10]+" "+flags[11];
	}
}
