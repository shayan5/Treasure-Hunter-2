package com.th2.treasurehunter2;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    

    public static List<Node> findPath (Node start, Node end, World world) throws
        HeapEmptyException, HeapFullException {
        Heap<Node> openSet = new Heap<Node>(WorldGenerator.DEFAULT_HEIGHT * WorldGenerator.DEFAULT_WIDTH);
        Heap<Node> closedSet = new Heap<Node>(WorldGenerator.DEFAULT_HEIGHT * WorldGenerator.DEFAULT_WIDTH);

        start.gCost = 0;
        start.hCost = getDistance(start, end);
        start.parent = start;

        openSet.add(start);

        while (!openSet.isEmpty()){
            Node current = openSet.removeFirst();
            if (current.equals(end)){
                return retracePath(start, end);
            }

            for (Node node : getNeighbours(current, world)){
                node.hCost = getDistance(node, end);
                int newGCost = current.gCost + getDistance(node, current);
                if (node.parent == null){
                    node.parent = current;
                    node.gCost = newGCost;
                } else if (node.gCost == -1) {
                    continue;
                } else if (newGCost < node.gCost){
                    node.parent = current;
                    node.gCost = newGCost;
                }

                if (!closedSet.contains(node) && !openSet.contains(node)){
                    openSet.add(node);
                }
            }
            closedSet.add(current);
        }
        return new ArrayList<Node>();
    }

    public static List<Node> getNeighbours(Node node, World world){
        List<Node> neighbours = new ArrayList<Node>();
        Node[][] map = world.getMap();
        int width = WorldGenerator.DEFAULT_WIDTH;
        int height = WorldGenerator.DEFAULT_HEIGHT;
        
		int xPos = node.x + 1;
		int yPos = node.y;
        
        
		if (xPos >= 0 && xPos < width && yPos >= 0 && yPos < height
				&& map[yPos][xPos].walkable == true) {
			neighbours.add(map[yPos][xPos]);
		}
		
		xPos = node.x - 1;
		yPos = node.y;
		
		if (xPos >= 0 && xPos < width && yPos >= 0 && yPos < height
				&& map[yPos][xPos].walkable == true) {
			neighbours.add(map[yPos][xPos]);
		}
		
		xPos = node.x;
		yPos = node.y + 1;
		
		if (xPos >= 0 && xPos < width && yPos >= 0 && yPos < height
				&& map[yPos][xPos].walkable == true) {
			neighbours.add(map[yPos][xPos]);
		}
		
		xPos = node.x;
		yPos = node.y - 1;
		
		if (xPos >= 0 && xPos < width && yPos >= 0 && yPos < height
				&& map[yPos][xPos].walkable == true) {
			neighbours.add(map[yPos][xPos]);
		}
		
		/* Add all viable neighbours including diagonal neighbours
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				int xPos = node.gridX + x;
				int yPos = node.gridY + y;
				if (!(x == 0 && y == 0) && xPos >= 0 && xPos < width && yPos >= 0 && yPos < height
						&& map[yPos][xPos].walkable == true) {
					neighbours.add(map[yPos][xPos]);
				}
			}
		} */

		return neighbours;
    }

    private static List<Node> retracePath(Node start, Node end){
        Node current = end;
        List<Node> path = new ArrayList<Node>();
        while (current != start && current != null){
            current.inPath = true;
            path.add(current);
            current = current.parent;
        }
        return path;
    }

    public static int getDistance(Node start, Node end){
		int dstX = Math.abs(start.x - end.x);
		int dstY = Math.abs(start.y - end.y);
		if (dstX > dstY)
			return 14 * dstY + 10 * (dstX - dstY);
		return 14 * dstX + 10 * (dstY - dstX);
    }

}