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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        //rank E:193 N:194 H:196 L:232
        stringBuilder.append("time:").append(time).append(" id:").append(id).append(" size:").append(size).append(" param_mask:").append(param_mask).append(" rank_mask:").append(toUByte(rank_mask)).append(" param_count:").append(param_count).append("\n");
        stringBuilder.append(printArray(data));
        return stringBuilder.toString();
    }

    private int toUByte(byte buf) {
        return buf & 0xFF;
    }
    public int readInt(int pos) {
        return (data[pos] & 0xff) | (data[pos + 1] & 0xff) << 8 | (data[pos + 2] & 0xff) << 16 | (data[pos + 3] & 0xff) << 24;
    }
	public float readFloat(int pos){
		int l;                                           
	    l = data[pos + 0];                                
	    l &= 0xff;                                       
	    l |= ((long) data[pos + 1] << 8);                 
	    l &= 0xffff;                                     
	    l |= ((long) data[pos + 2] << 16);                
	    l &= 0xffffff;                                   
	    l |= ((long) data[pos + 3] << 24);                
	    return Float.intBitsToFloat(l);              
	}
    private String printArray(byte[] array) {
        if (array.length == 0) {
            return "no param";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0;i<data.length;i+=4){
            stringBuilder.append(((param_mask>>(i/4))&0b1)==1?"var:":"num:").append(readInt(i)).append(" | ");
     //     stringBuilder.append(Integer.toHexString(b & 0xFF)).append((i + 1) % 4 == 0 ? " | " : " ");
        }
        return stringBuilder.toString();
    }

	/**
	 * 浮点转换为字节
	 * 
	 * @param f
	 * @return
	 */
	public static byte[] float2byte(float f) {
		int fbit = Float.floatToIntBits(f);
		byte[] b = new byte[4];  
		b[0] = (byte) fbit;  
		b[1] = (byte) (fbit >> 8);  
		b[2] = (byte) (fbit >> 16);  
		b[3] = (byte) (fbit >> 24);  
	    return b;
	  }

	/**
	 * 字节转换为浮点
	 * 
	 * @param b 字节（至少4个字节）
	 * @param index 开始位置
	 * @return
	 */
	public static float byte2float(byte[] b, int index) {  
	    int l;                                           
	    l = b[index + 0];                                
	    l &= 0xff;                                       
	    l |= ((long) b[index + 1] << 8);                 
	    l &= 0xffff;                                     
	    l |= ((long) b[index + 2] << 16);                
	    l &= 0xffffff;                                   
	    l |= ((long) b[index + 3] << 24);                
	    return Float.intBitsToFloat(l);                  
	  }
}
