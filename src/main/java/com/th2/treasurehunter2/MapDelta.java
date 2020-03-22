package com.th2.treasurehunter2;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class MapDelta {
    private List<Node> changes;

    public MapDelta(){
        this.changes = new ArrayList<Node>();
    }

    public void addChange(Node n){
        changes.add(n);
    }

    public String getJson(){
        return new Gson().toJson(changes);
    }
}