package com.th2.treasurehunter2;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GameController {


  @MessageMapping("/hello")
  @SendTo("/topic/greetings")
  @SendToUser(destinations = "/queue/reply", broadcast = false)
  public Greeting greeting(HelloMessage message) throws Exception {
    //Thread.sleep(1000); // simulated delay
    return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
  }

  /*
  @MessageMapping("/newgrid")
  @SendToUser(destinations = "/queue/newgrid", broadcast = false)
  public World newGrid() {
    Grid world = new Grid();
    return new World(world.drawMap());
  }
  */

  @MessageMapping("/newgame")
  @SendToUser(destinations = "/queue/reply", broadcast = false)
  public String newGame(){
    WorldGenerator wg = new WorldGenerator();
    World world = wg.getWorld();
    return world.getJson();
  }
}