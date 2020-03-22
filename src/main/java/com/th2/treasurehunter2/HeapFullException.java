package com.th2.treasurehunter2;

@SuppressWarnings("serial")
public class HeapFullException extends Exception {
    
    public HeapFullException(){
        super("Heap is full");
    }

}