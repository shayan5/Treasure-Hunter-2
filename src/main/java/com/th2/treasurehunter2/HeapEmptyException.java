package com.th2.treasurehunter2;

@SuppressWarnings("serial")
public class HeapEmptyException extends Exception {
    public HeapEmptyException(){
        super("Heap in empty");
    }
}