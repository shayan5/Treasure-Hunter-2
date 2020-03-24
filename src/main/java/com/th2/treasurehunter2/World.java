package com.th2.treasurehunter2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class World {
    private Node[][] map;
    private Node boat;
    private Node treasure;
    public int moves;
    public int sonars;
    public String state;

    public World(){
    }

    public World(Node[][] map, Node treasure, Node boat, int moves, int sonars) {
        this.map = map;
        this.boat = boat;
        this.treasure = treasure;
        this.moves = moves;
        this.sonars = sonars;
        this.state = "play";
    }

    public String getJson(){
        Gson g = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject json = new JsonObject();
        json.addProperty("state", state);
        json.addProperty("moves", moves);
        json.addProperty("sonars", sonars);
        json.addProperty("map", g.toJson(map));
        json.addProperty("boat", g.toJson(boat));
        json.addProperty("treasure", g.toJson(treasure));
        return json.toString();
    }

    public void cleanPathData(){
        for (Node[] nodes : map) {
            for (Node node : nodes) {
                node.gCost = -1;
                node.inPath = false;
                node.parent = null;
                node.heapIndex = -1;
            }
        }
    }

    public Node[][] getMap() {
        return map;
    }

    public void setMap(Node[][] map) {
        this.map = map;
    }

    public Node getBoat() {
        return boat;
    }

    public void setBoat(Node boat) {
        this.boat = boat;
    }

    public Node getTreasure() {
        return treasure;
    }

    public void setTreasure(Node treasure) {
        this.treasure = treasure;
    }

}