package com.th2.treasurehunter2;


public class ProcessCommands {

    public static WorldDelta processCommand(Command command, World world) {
        Node boat = world.getBoat();
        String direction = command.getCommand();
        WorldDelta changes = new WorldDelta(world);
        if (direction.equals("up")) {
            changes = moveBoat(changes, world, boat.x, boat.y - 1);
        } else if (direction.equals("down")) {
            changes = moveBoat(changes, world, boat.x, boat.y + 1);
        } else if (direction.equals("left")) {
            changes = moveBoat(changes, world, boat.x - 1, boat.y);
        } else if (direction.equals("right")) {
            changes = moveBoat(changes, world, boat.x + 1, boat.y);
        } else if (direction.equals("sonar")) {
            changes = handleSonar(changes, world);
        }
        return changes;
    }


    private static WorldDelta revealTreasure(WorldDelta changes, World world){
        Node treasure = world.getTreasure();
        treasure.hidden = false;
        changes.addChange(treasure);
        return changes;
    }

    private static WorldDelta moveBoat(WorldDelta changes, World world, int newX, int newY) {
        Node[][] map = world.getMap();
        Node boat = world.getBoat();
        if (world.moves > 0 && !(newY < 0 || newY >= WorldGenerator.DEFAULT_HEIGHT || newX < 0 || newX >= WorldGenerator.DEFAULT_WIDTH
                || !map[newY][newX].walkable)) {
            if (map[newY][newX].type.equals("treasure")){ //user walked on treasure
                changes.state = "won";
                return revealTreasure(changes, world);
            } else {
                if (map[newY][newX].type.equals("sail")){
                    changes = updateMoves(world, changes, WorldGenerator.SAIL_INCREMENT);
                } else if (map[newY][newX].type.equals("battery")){
                    changes = updateSonars(world, changes, WorldGenerator.BATTERY_INCREMENT);
                }
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
                changes.updateBoat(boat);
            }
            changes = updateMoves(world, changes, -1);
        }
        return changes;
    }


    private static WorldDelta updateSonars(World world, WorldDelta changes, int amount){
        world.sonars += amount;
        changes.sonars = world.sonars;
        if (world.sonars <= 0 && world.moves <= 0){
            changes.state = "lost";
            return revealTreasure(changes, world);
        }
        return changes;
    }

    private static WorldDelta updateMoves(World world, WorldDelta changes, int amount){
        world.moves += amount;
        changes.moves = world.moves;
        if (world.sonars <= 0 && world.moves <= 0){
            changes.state = "lost";
            return revealTreasure(changes, world);
        }
        return changes;
    }
    

    private static WorldDelta handleSonar(WorldDelta changes, World world) {
        Node boat = world.getBoat();
        Node treasure = world.getTreasure();
        if (world.sonars <= 0){
            return changes;
        }
        if (PathFinder.getDistance(boat, treasure) <= WorldGenerator.SONAR_RANGE){        
            try {
                changes.mapChanges = PathFinder.findPath(boat, treasure, world);
                changes.state = "won";
                return revealTreasure(changes, world);
            } catch (HeapEmptyException | HeapFullException e) {
                e.printStackTrace();
            }
        }
        changes = updateSonars(world, changes, - 1);
        return changes;
    }
}