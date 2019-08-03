package com.InsProcess.helper;

public class EclIntStack {
    private final int maxDepth = 5;
    private int depth = 0;
    private int[] stack = new int[maxDepth];

    public void push(int n) {
        if (depth == maxDepth - 1) {
            throw new RuntimeException("stack full");
        }
        stack[depth++] = n;
    }

    public int pop() {
        if (depth == 0) {
            throw new RuntimeException("stack blanck");
        }
        return stack[--depth];
    }

    public int peek() {
        if (depth == 0) {
            throw new RuntimeException("stack blanck");
        }
        return stack[depth - 1];
    }
}