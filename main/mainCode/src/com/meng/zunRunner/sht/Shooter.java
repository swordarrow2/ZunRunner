package com.meng.zunRunner.sht;

import com.badlogic.gdx.math.Vector2;

public class Shooter {
    public byte rate; // 发弹间隔
    public byte delay; // 发弹时间点
    public short power; // 每发子弹的威力
    public Vector2 pos = new Vector2(); // 发弹点相对坐标
    public Vector2 hitBox = new Vector2(); // 子弹判定的长和宽
    public float angle; // 发射方向
    public float speed; // 发射速度
    public int unknown1; // MoF之后加入
    public byte option; // 子机号,0=main
    public byte unknown2;
    public short ANM; // 自机弹贴图
    public short SE; // 音效
    public byte rate2; // rate2
    public byte delay2; // delay2
    public int[] flags = new int[12];
    public static final int size = 88;
}
