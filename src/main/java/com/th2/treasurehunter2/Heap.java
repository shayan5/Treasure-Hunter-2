package com.th2.treasurehunter2;

public class Heap<T extends HeapItem> {
	public T[] items;
    protected int maxHeapSize;
    protected int currentItemCount; //number of items in the heap

    @SuppressWarnings("unchecked")
	public Heap(int maxHeapSize){
        this.maxHeapSize = maxHeapSize;
        this.items = (T[]) new HeapItem[maxHeapSize];
        this.currentItemCount = 0;
    }

    public boolean isEmpty(){
        return currentItemCount == 0;
    }

    public boolean isFull(){
        return currentItemCount == maxHeapSize;
    }

    public boolean contains(T item){
        if (item.getHeapIndex() < 0){
            return false;
        } 
        if (items[item.getHeapIndex()] != null){
            return items[item.getHeapIndex()].equals(item);
        } 
        return false;
    }

    public void add(T item) throws HeapFullException {
        if (isFull()) {
            throw new HeapFullException();
        } else {
            item.setHeapIndex(currentItemCount);
            items[currentItemCount] = item; //add new item to the end
            sortUp(item);
            currentItemCount++;
        }
    }

    public void sortUp(T item){
        while (item.getHeapIndex() != 0) {
            T parent = items[Math.floorDiv((item.getHeapIndex() + 1), 2) - 1];
            if (item.compareTo(parent) == 1){
                swap(item, parent);
            } else {
                break;
            }
        }
    }

    public void swap(T first, T second){
        int fIndex = first.getHeapIndex();
        int sIndex = second.getHeapIndex();

        second.setHeapIndex(fIndex);
        first.setHeapIndex(sIndex);

        items[fIndex] = second;
        items[sIndex] = first;
    }

    public T removeFirst() throws HeapEmptyException {
        if (isEmpty()){
            throw new HeapEmptyException();
        } else {
            T topItem = items[0];
            currentItemCount--;
            items[0] = items[currentItemCount];
            items[0].setHeapIndex(0);
            sortDown(items[0]);
            return topItem;
        }
    }

    public void sortDown(T item){
        T left = null;
        T right = null;

        if (currentItemCount > item.getHeapIndex() * 2 + 1){
            left = items[item.getHeapIndex() * 2 + 1];
        }
        if (currentItemCount > item.getHeapIndex() * 2 + 2){
            right = items[item.getHeapIndex() * 2 + 2];
        }

        if (left != null && right != null){
            if (right.compareTo(left) == 1){
                if (right.compareTo(item) == 1){
                    swap(right, item);
                    sortDown(item);
                }
            } else {
                if (left.compareTo(item) == 1){
                    swap(left, item);
                    sortDown(item);
                }
            }
        } else if (left == null && right != null && right.compareTo(item) == 1){
            swap(right, item);
            sortDown(item);
        } else if (left != null && right == null && left.compareTo(item) == 1){
            swap(left, item);
            sortDown(item);
        }
    }
}
