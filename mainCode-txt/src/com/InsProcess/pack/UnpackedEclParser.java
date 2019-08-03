package com.InsProcess.pack;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class UnpackedEclParser {

    private File eclFile;

    public UnpackedEclParser(File unpackedEclFile) {
        eclFile = unpackedEclFile;
    }

    public String parse() {
        String[] strs = read().split("sub");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < strs.length; ++i) {
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
            e.printStackTrace();
        }
        return s;
    }

    private void save(String unpackedEcl) {
        try {
			String tmp=unpackedEcl.replaceAll("\\s","");
            FileOutputStream fos = new FileOutputStream(new File(eclFile.getParent() + "/subed/" + tmp.substring(0, tmp.indexOf("(")) + ".txt"));
            OutputStreamWriter writer = new OutputStreamWriter(fos, "Shift_JIS");
            writer.write(unpackedEcl);
            writer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
