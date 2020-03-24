package com.th2.treasurehunter2;

public class AsciiMap {
    String[][] map;
    
    public AsciiMap(){

    }

    public String[][] getMap() {
        return map;
    }

    public void setMap(String[][] map) {
        this.map = map;
    }

    public World convertToNodeWorld(){
        Node[][] result = new Node[map.length][map[0].length];
        Node boat = null;
        Node treasure = null;
        for (int i = 0 ; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++){
                if (map[i][j].equals("W")){
                    result[i][j] = new Node(true, j, i, "water");
                } else if (map[i][j].equals("T")){
                    result[i][j] = new Node(true, j, i, "treasure");
                    treasure = result[i][j];
                } else if (map[i][j].equals("B")){
                    result[i][j] = new Node(false, j, i, "boat");
                    boat = result[i][j];
                } else {
                    result[i][j] = new Node(false, j, i, "land");
                }
            }
        }
        return new World(result, treasure, boat, WorldGenerator.DEFAULT_MOVES, WorldGenerator.DEFAULT_SONARS);
    }
}