package com.InsProcess;

public class ChangeTask {
	public boolean singleMode;
	public int mode;
	public int a;
	public int b;
	public int c;
	public int d;
	public float m;
	public float n;
	public float r;
	public float s;
	
	public ChangeTask(int way, int mode, int inta, int intb, int intc, int intd, float floatr, float floats, float floatm, float floatn){
	  singleMode=way==0;
	  this.mode=mode;
	  a=inta;
	  b=intb;
	  c=intc;
	  d=intd;
	  m=floatm;
	  n=floatn;
	  r=floatr;
	  s=floats;
	}
  }
