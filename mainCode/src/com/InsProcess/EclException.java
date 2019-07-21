package com.InsProcess;

public class EclException {
    public NullPointerException varNotDefined(){
        return new NullPointerException("var not defined");
    }
    public NullPointerException varTypeUnknown(){
        return new NullPointerException("var type unknown");
    }
}
