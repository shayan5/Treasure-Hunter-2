package com.th2.treasurehunter2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class World {
    private Node[][] map;
    private Node boat;
    private Node treasure;

    public World(){
    }

    public World(Node[][] map, Node treasure, Node boat) {
        this.map = map;
        this.boat = boat;
        this.treasure = treasure;
    }

    public String getJson(){
        Gson g = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("map", g.toJson(map));
        json.addProperty("boat", g.toJson(boat));
        json.addProperty("treasure", g.toJson(treasure));
        return json.toString();
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