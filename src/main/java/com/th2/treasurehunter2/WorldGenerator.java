package com.th2.treasurehunter2;

import java.util.concurrent.ThreadLocalRandom;

public class WorldGenerator {
    public static final int DEFAULT_WIDTH = 60;
    public static final int DEFAULT_HEIGHT = 15;
    public static final int SONAR_RANGE = 200;
    public static final int DEFAULT_SONARS = 3;
    public static final int DEFAULT_MOVES = 30; 
    private final int DEFAULT_PERCENT = 20; //percent of islands nodes
    private final int BATTERY = 1;
    public static final int BATTERY_INCREMENT = 1;
    private final int SAIL = 2;
    public static final int SAIL_INCREMENT = 5;

    protected Node treasure;
    protected Node boat;

    protected Node[][] map;

    public WorldGenerator() {
        buildMap();
    }

    public World getWorld(){
        return new World(map, treasure, boat, DEFAULT_MOVES, DEFAULT_SONARS);
    }

    private void buildMap(){
        map = new Node[DEFAULT_HEIGHT][DEFAULT_WIDTH];
        for (int x = 0; x < DEFAULT_WIDTH; x++){
            for (int y = 0; y < DEFAULT_HEIGHT; y++){
                int random = (int)(Math.random() * 100 + 1);
                if (random <= BATTERY){
                    map[y][x] = new Node(true, x, y, "battery");
                } else if (random <= SAIL){
                    map[y][x] = new Node(true, x, y, "sail");
                } else if (random <= DEFAULT_PERCENT){
                    map[y][x] = new Node(false, x, y, "land");
                } else {
                    map[y][x] = new Node(true, x, y, "water");
                }
            }
        }
        buildBoat(getRandomNode());
        buildTreasure(getRandomNode());
    }

    private Node getRandomNode() {
        int x = ThreadLocalRandom.current().nextInt(0, DEFAULT_HEIGHT);
        int y = ThreadLocalRandom.current().nextInt(0, DEFAULT_WIDTH);

        Node result = map[x][y];
        if (result.type.equals("boat") || result.type.equals("treasure")) {
            return getRandomNode();
        } else {
            return result;
        }
    }
    private void buildBoat(Node n){
        n.type = "boat";
        n.walkable = false;
        this.boat = n;
    }

    private void buildTreasure(Node n){
        n.type = "treasure";
        n.walkable = true;
        n.hidden = true;
        this.treasure = n;
    }


}