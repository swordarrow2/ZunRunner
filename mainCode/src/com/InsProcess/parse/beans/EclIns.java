package com.InsProcess.parse.beans;

/**
 * @author Administrator th10_instr_t
 */
public class EclIns {

    public int time;
    public short id;
    public short size;
    public short param_mask;
    /* The rank bitmask.
     *   1111LHNE
     * Bits mean: easy, normal, hard, lunatic. The rest are always set to 1. */
    public byte rank_mask;
    /* There doesn't seem to be a way of telling how many parameters there are
     * from the additional data. */
    public byte param_count;
    /* From TH13 on, this field stores the number of current stack references
     * in the parameter list. */
    public int zero;
    public byte data[];
}
