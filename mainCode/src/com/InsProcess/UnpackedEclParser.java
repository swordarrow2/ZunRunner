package com.InsProcess;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class UnpackedEclParser {
  
	File eclFile;
	
	public UnpackedEclParser(File unpackedEclFile) {
		eclFile = unpackedEclFile;
	  }

	  public String parse(){
		String code=read();//.replaceAll("\\s","");
		ArrayList<String> list=new ArrayList<>();
		String[] strs=code.split("sub");
	//	  int index1=code.indexOf("sub");
		
	//	int index2=code.indexOf("}")+1;
	//	while(index1!=0){
	//	  list.add("gggggggggggggggggggggggggg");
	//	  list.add(code.substring(index1,index2 ));
	//	  index1=index2;
	//	  index2=code.indexOf("}", index1)+1;
	//  }
		
		StringBuilder sb=new StringBuilder();
		
		  for(int i=1;i<strs.length;++i){
			  sb.append("ggggggggggggggggggggg");
			  sb.append(strs[i]);
			  save(strs[i]);
		}
		
		
		return sb.toString();
	  }








	

	public String read() {
        String s = "";
        try {    
            if (!eclFile.exists()) {
                eclFile.createNewFile();
			  }
            long filelength = eclFile.length();
            byte[] filecontent = new byte[(int) filelength];
            FileInputStream in = new FileInputStream(eclFile);
            in.read(filecontent);
            in.close();
            s = new String(filecontent, "Shift_JIS");
		  } catch (Exception e) {

		  }
        return s;
	  }
	  
	private void save(String unpackedEcl) {
        try {
			FileOutputStream fos = new FileOutputStream(new File(eclFile.getParent()+"/aaa/"+unpackedEcl.substring(0,unpackedEcl.indexOf("("))+".txt"));
            OutputStreamWriter writer = new OutputStreamWriter(fos,"Shift_JIS");
            writer.write(unpackedEcl);
            writer.flush();
            fos.close();
		  } catch (IOException e) {
            e.printStackTrace();
		  }
	  }
  }
