package com.InsProcess;

public class Goto extends Ins {
	LoopFlag loopFlag;
	public Goto(LoopFlag loopFlag) {
		super(new Sub(""));
		this.loopFlag = loopFlag;
	  }

	@Override
	public String toString() {
		return "goto " + loopFlag.getName() + " @ 0;\n";
	  }

  }
