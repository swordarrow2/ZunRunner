package com.InsProcess.parse;


import com.InsProcess.helper.EclVar;

public class Ins {

    public int insNum;
    public EclVar[] args;

    Ins(int insNum, EclVar... args) {
        this.insNum = insNum;
        this.args = args;
    }

}
