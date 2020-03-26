# Treasure Hunter 2
This project is a simple game in which the player controls a boat with the objective of finding hidden treasure. The player is aided with a minimap feature which provides them with the shortest path to the treasure using an implementation of the A* pathfinding algoritm (see https://en.wikipedia.org/wiki/A*_search_algorithm).

This implementation of the A* algorithm uses the diagonal distance as the determining heuristic (see http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html) and a custom min heap based priority queue for speed and efficiency. 

This is a remake of https://github.com/shayan5/TreasureHunter. Initially, the project was using a java applet based GUI which has now been deprecated on most modern browsers. As a result, I rewrote the app as an opportunity to learn Java Spring Websockets and Gradle. The GUI has been rewritten in JavaScript, HTML/CSS and has a responsive design thanks to JQuery and BootStrap.

All icons/sprites are courtesy of https://icons8.com/. 




