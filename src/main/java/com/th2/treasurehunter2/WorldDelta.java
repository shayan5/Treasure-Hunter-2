package com.th2.treasurehunter2;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class WorldDelta {
    public List<Node> mapChanges;
    public String state;
    public int moves;
    public Node boat;

    public WorldDelta(World world) {
        this.mapChanges = new ArrayList<Node>();
        this.moves = world.moves;
        this.state = world.state;
        this.boat = world.getBoat();
    }

    public void addChange(Node n){
        mapChanges.add(n);
    }

    public void updateBoat(Node boat){
        this.boat = boat;
    }

    public String getJson(){
        Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject json = new JsonObject();
        json.addProperty("boat", g.toJson(boat));
        json.addProperty("state", state);
        json.addProperty("moves", moves);
        json.addProperty("mapChanges", g.toJson(mapChanges));
        return json.toString();
    }
}