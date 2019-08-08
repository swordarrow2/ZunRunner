package com.InsProcess.parse.beans;

/**
 * @author Administrator th10_list_t
 */
public class EclList {
    public byte[] magic = new byte[4];
    public int count;
    public byte[] data;
	
	public String[] getFileName(){
	  if(data.length==0){
		return null;
	  }
		return new String(data).split(String.valueOf((char)0));
	}

    @Override
    public String toString() {
        return String.format("%s,count:%d,data:%s", new String(magic), count, new String(data));
    }
}
