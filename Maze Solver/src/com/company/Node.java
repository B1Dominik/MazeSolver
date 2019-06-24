package com.company;

public class Node {
    private int x;
    private int y;
    private Node parent;

    Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    public String toString() {
        return "x = " + x + " y = " + y;
    }

    Node getParent() {
        return this.parent;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

}