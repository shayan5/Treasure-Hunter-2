package com.th2.treasurehunter2;

import java.util.concurrent.ThreadLocalRandom;

public class WorldGenerator {
    private final int DEFAULT_WIDTH = 60;
    private final int DEFAULT_HEIGHT = 15;
    private final int DEFAULT_PERCENT = 20; //percent of islands nodes

    protected Node treasure;
    protected Node boat;

    protected Node[][] map;

    public WorldGenerator() {
        buildMap();
    }

    public World getWorld(){
        return new World(map, treasure, boat);
    }

    private void buildMap(){
        map = new Node[DEFAULT_HEIGHT][DEFAULT_WIDTH];
        for (int x = 0; x < DEFAULT_WIDTH; x++){
            for (int y = 0; y < DEFAULT_HEIGHT; y++){
                int random = (int)(Math.random() * 100 + 1);
                if (random <= DEFAULT_PERCENT){
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
        this.treasure = n;
    }

    /*
    public String drawMap() {
        StringBuilder sb = new StringBuilder();
        for (int x = 0; x < DEFAULT_HEIGHT; x++) {
            for (int y = 0; y < DEFAULT_WIDTH; y++){
                if (map[x][y].type.equals("water")) {
                    sb.append("W");
                } else if (map[x][y].type.equals("land")){
                    sb.append("L");
                } else if (map[x][y].type.equals("boat")){
                    sb.append("B");    
                } else {
                    sb.append("T");
                }
            }
            sb.append("N");
        }
        return sb.toString();
    } */

    /*
    public void move(String direction){
        System.out.println(direction);
    } */
}