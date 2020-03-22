package com.th2.treasurehunter2;

public interface HeapItem extends Comparable<HeapItem>{
    public void setHeapIndex(int index);
    public int getHeapIndex();
}