package com.th2.treasurehunter2;

import com.google.gson.Gson;

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
        return map.toString();
    }

}