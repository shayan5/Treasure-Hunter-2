package com.th2.treasurehunter2;

import java.util.ArrayList;

import com.google.gson.Gson;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

  @MessageMapping("/command")
  @SendToUser(destinations = "/queue/changes", broadcast = false)
  public String processCommand(Command command, SimpMessageHeaderAccessor headerAccessor) {
    World world = (World) headerAccessor.getSessionAttributes().get("world");
    WorldDelta changes = ProcessCommands.processCommand(command, world);
    String result = changes.getJson();
    return result;
  }

  @MessageMapping("/newgame")
  @SendToUser(destinations = "/queue/reply", broadcast = false)
  public String newGame(SimpMessageHeaderAccessor headerAccessor) {
    World world = new WorldGenerator().getWorld();
    headerAccessor.getSessionAttributes().put("world", world);
    return world.getJson();
  }

  @MessageMapping("/pathfind")
  @SendToUser(destinations = "/queue/minimap", broadcast = false)
  public String pathFind(AsciiMap map) {
    World world = map.convertToNodeWorld();
    try {
      ArrayList<Node> path = (ArrayList<Node>) PathFinder.findPath(world.getBoat(), world.getTreasure(), world);
      path.add(world.getBoat());
      Gson g = new Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create();
      return g.toJson(path);
    } catch (HeapEmptyException | HeapFullException e) {
      e.printStackTrace();
    }
    return "{}";
  }
}