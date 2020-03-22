package com.th2.treasurehunter2;

public class Node implements HeapItem {
    protected boolean walkable;
    protected boolean hidden;
    protected int x;
    protected int y;
    protected String type;
    protected int heapIndex;
    protected Node parent;
    protected boolean inPath;
    protected int gCost;
    protected int hCost;

    public Node(){
        
    }

    public Node(boolean walkable, int x, int y, String type) {
        this.walkable = walkable;
        this.x = x;
        this.y = y;
        this.type = type;
        this.inPath = false;
        this.gCost = -1; //gcost of -1 is infinite
        this.hidden = false;
    }

    public int getFCost(){
        return gCost + hCost;
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

    @Override
    public int compareTo(HeapItem o) {
        Node n = (Node) o;
        if (getFCost() < n.getFCost()){
            return 1;
        } else if (getFCost() == n.getFCost() && hCost >= n.hCost){
            return 1;
        }
        return 0;
    }

    @Override
    public void setHeapIndex(int index) {
        heapIndex = index;

    }

    @Override
    public int getHeapIndex() {
        return heapIndex;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof Node){
           Node n = (Node) o;
           if (x == n.x && y == n.y){
               return true;
           }
        }
        return false;
    }
}