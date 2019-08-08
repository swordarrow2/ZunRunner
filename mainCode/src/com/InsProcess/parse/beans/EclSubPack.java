package com.InsProcess.parse.beans;
import com.InsProcess.parse.*;

public class EclSubPack {
    public String subName;
    public int position;
    public EclSub sub;
	
	public EclFile eclFile;
	
	public EclSubPack(EclFile file){
	  eclFile=file;
	}

    @Override
    public String toString() {
        sub.insList();
        return sub.toString();
    }
}
