package com.meng.zunRunner.anm.beans;

public class AnmHeader {
    public int version;
    public short sprites;
    public short scripts;
    /* Actually zero. */
    public short zero1;
    public short w;
    public short h;
    public short format;
    public int nameoffset;
    public short x;
    public short y;
    /*
     * As of TH16.5, unused in every game that uses this stucture, but still
     * present in the files.
     */
    public int memorypriority;
    public int thtxoffset;
    public short hasdata;
    public short lowresscale;
    public int nextoffset;
    public int[] zero2 = new int[6];
}
