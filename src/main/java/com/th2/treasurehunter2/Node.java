package com.th2.treasurehunter2;

public class Node {
    protected boolean walkable;
    protected int x;
    protected int y;
    protected String type;

    public Node(){
        
    }

    public Node(boolean walkable, int x, int y, String type) {
        this.walkable = walkable;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}