package com.InsProcess.helper;

public class EclNumberStack {
    private final int maxDepth = 5;
    private int depth = 0;
    private EclVar[] stack = new EclVar[maxDepth];

    public void push(EclVar n) {
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
        }
        stack[depth++] = n;
    }

    public void push(int n) {
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
        }
        stack[depth++] = new EclVar(n);
    }

    public void push(float n) {
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
        }
        stack[depth++] = new EclVar(n);
    }

    public void push(double n) {
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
        }
        stack[depth++] = new EclVar((float) n);
    }

    public EclVar pop() {
        if (depth == 0) {
            throw new RuntimeException("stack blanck");
        }
        return stack[--depth];
    }

    public int popInt() {
        if (depth == 0) {
            throw new RuntimeException("stack blanck");
        }
        EclVar eclVar = stack[depth];
        switch (eclVar.type) {
            case EclVar.typeInt:
                --depth;
                return eclVar.intValve;
            case EclVar.typeFloat:
                throw new RuntimeException("not int value");
            case EclVar.typeString:
                throw new RuntimeException("not int value");
            default:
                throw new RuntimeException("not int value");
        }
    }

    public float popFloat() {
        if (depth == 0) {
            throw new RuntimeException("stack blanck");
        }
        EclVar eclVar = stack[depth];
        switch (eclVar.type) {
            case EclVar.typeInt:
                throw new RuntimeException("not float value");
            case EclVar.typeFloat:
                --depth;
                return eclVar.floatValue;
            case EclVar.typeString:
            default:
                throw new RuntimeException("not float value");
        }
    }

    public EclVar peek() {
        if (depth == 0) {
            throw new RuntimeException("stack blanck");
        }
        return stack[depth - 1];
    }
}
