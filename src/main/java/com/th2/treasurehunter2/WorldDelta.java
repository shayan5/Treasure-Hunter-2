package com.th2.treasurehunter2;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WorldDelta {
    public List<Node> mapChanges;
    public String state;
    public int moves;
    public int sonars;

    public WorldDelta(World world) {
        this.mapChanges = new ArrayList<Node>();
        this.moves = world.moves;
        this.sonars = world.sonars;
        this.state = world.state;
    }

    public void addChange(Node n){
        mapChanges.add(n);
    }

    public String getJson(){
        JsonObject json = new JsonObject();
        json.addProperty("state", state);
        json.addProperty("moves", moves);
        json.addProperty("sonars", sonars);
        json.addProperty("mapChanges", new Gson().toJson(mapChanges));
        return json.toString();
    }
}