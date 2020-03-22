package com.th2.treasurehunter2;

public class ProcessCommands {
    
    
    public static MapDelta processCommand(Command command, World world){
        Node boat = world.getBoat();
        String direction = command.getCommand();
        MapDelta changes = new MapDelta();
        if (direction.equals("up")){
            changes = moveBoat(changes, world, boat.x, boat.y - 1); 
        } else if (direction.equals("down")){
            changes = moveBoat(changes, world, boat.x, boat.y + 1); 
        } else if (direction.equals("left")){
            changes = moveBoat(changes, world, boat.x - 1, boat.y);
        } else if (direction.equals("right")){
            changes = moveBoat(changes, world, boat.x + 1, boat.y);
        }
        return changes;
    }

    private static MapDelta moveBoat(MapDelta changes, World world, int newX, int newY){
        Node[][] map = world.getMap();
        Node boat = world.getBoat();
        if (!(newY < 0 || newY >= WorldGenerator.DEFAULT_HEIGHT ||
        newX < 0 || newX >= WorldGenerator.DEFAULT_WIDTH
        || !map[newY][newX].walkable)){
            map[boat.y][boat.x].type = "water";
            map[boat.y][boat.x].walkable = true;
            map[newY][newX].type = "boat";
            map[newY][newX].walkable = false;
            changes.addChange(new Node(true, boat.x, boat.y, "water"));
            changes.addChange(new Node(false, newX, newY, "boat"));
            boat.x = newX;
            boat.y = newY;
            world.setMap(map);
            world.setBoat(boat);
        }
        return changes;
    }
}