package com.th2.treasurehunter2;

public class Node {
    protected boolean walkable;
    protected int x;
    protected int y;
    protected String type;

    public Node(boolean walkable, int x, int y, String type) {
        this.walkable = walkable;
        this.x = x;
        this.y = y;
        this.type = type;
    }      
}