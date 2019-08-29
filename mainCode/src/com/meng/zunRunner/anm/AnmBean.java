package com.meng.zunRunner.anm;

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

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(header.toString()).append("\nsprite:\n");
		for (Sprite sprite : sprites) {
			stringBuilder.append(sprite.toString()).append("\n");
		}
		stringBuilder.append("anmIns:");
		for (ArrayList<AnmIns> list : scripts) {
			for (AnmIns anmIns : list) {
				stringBuilder.append(anmIns.toString()).append("\n");
			}
		}
		stringBuilder.append("thtx:");
		stringBuilder.append(thtx.toString());
		return stringBuilder.toString();
	}
}
