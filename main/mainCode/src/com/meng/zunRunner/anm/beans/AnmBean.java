package com.meng.zunRunner.anm.beans;

import java.util.ArrayList;

public class AnmBean {
    public int start = 0;
    public AnmHeader header;
    public int[] spriteOffset;
    public AnmInsOffset[] anmInsOffsets;
    public String picName;
    public Sprite[] sprites;
    public ArrayList<AnmIns>[] scripts;
    public THTX thtx;
}
