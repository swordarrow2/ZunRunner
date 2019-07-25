package com.InsProcess.helper;

public class EclFloatStack {
    private final int maxDepth = 5;
    private int depth = 0;
    private float[] stack = new float[maxDepth];

    public void push(float n) {
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
        }
        stack[depth++] = n;
    }

    public float pop() {
        if (depth == 0) {
            throw new RuntimeException("stack blanck");
        }
        return stack[--depth];
    }

    public float peek() {
        if (depth == 0) {
            throw new RuntimeException("stack blanck");
        }
        return stack[depth - 1];
    }
}
