package com.meng.anm;

import java.util.ArrayList;

public class AnmPart {

	public int start = 0;
	public AnmHeader header;
	public int[] spriteOffset;
	public AnmInsOffset[] anmInsOffsets;
	public String picName;
	public Sprite[] sprite_ts;
	public ArrayList<AnmIns>[] scripts;
	public THTX thtx;

	int FORMAT_BGRA8888 = 1;
	int FORMAT_RGB565 = 3;
	int FORMAT_ARGB4444 = 5; /* 0xGB 0xAR */
	int FORMAT_GRAY8 = 7;

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(header.toString()).append("\nsprite:\n");
		for (Sprite sprite : sprite_ts) {
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
