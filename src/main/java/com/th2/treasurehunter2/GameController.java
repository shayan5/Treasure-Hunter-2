package com.th2.treasurehunter2;

import com.google.gson.Gson;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {


  @MessageMapping("/command")
  @SendToUser(destinations = "/queue/changes", broadcast = false)
  public String processCommand(Command command, SimpMessageHeaderAccessor headerAccessor){
    World world = (World) headerAccessor.getSessionAttributes().get("world");
    WorldDelta changes = ProcessCommands.processCommand(command, world);
    return new Gson().toJson(changes);
  }

  @MessageMapping("/newgame")
  @SendToUser(destinations = "/queue/reply", broadcast = false)
  public String newGame(SimpMessageHeaderAccessor headerAccessor){
    World world = new WorldGenerator().getWorld();
    headerAccessor.getSessionAttributes().put("world", world);
    return world.getJson();
  }
}