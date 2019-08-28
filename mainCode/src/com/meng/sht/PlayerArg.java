package com.meng.sht;

public class PlayerArg {
	public short maxPower; // 默认4
	public short totalOffset; // 最大offset数量
	public float hitBox; // 判定 (无用)
	public float powerAttractSpeed; // 收p点的速度 (无用)
	public float powerAttractHitBox; // 收p点的判定(高速) (无用)
	public float normalSpeed; // 高速 速度
	public float forcedSpeed; // 低速速度
	public float normalDiagonalSpeed; // 斜向高速(指分配到xy的速度)
	public float forcedDiagonalSpeed; // 斜向低速
	public short maxPowerLevel; // 最大的子机数(power的最大百位值)
	public short unknown1; // 不知道
	public int powerVar; // power要修改的内容(40)
	public int maxDamage; // 单帧子弹最高伤害
	public int[] unknown = new int[5]; // 不知道
	
	@Override
	public String toString() {
		return maxPower+" "+totalOffset+" "+hitBox+" "+powerAttractSpeed+" "+powerAttractHitBox+" "+normalSpeed+" "+forcedSpeed+" "+normalDiagonalSpeed+" "+forcedDiagonalSpeed+" "+maxPowerLevel+" "+unknown1
				+" "+powerVar+" "+maxDamage+" "+unknown[0]+" "+unknown[1]+" "+unknown[2]+" "+unknown[3]+" "+unknown[4];
	}
}
