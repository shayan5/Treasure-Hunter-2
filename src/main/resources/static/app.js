var stompClient = null;
var worldMap = null;
var boat = null;
var radius = 5;
var minimapScale = 5;

$( document ).ready(function(){
    connect();
});

function resetGame(){
    disconnect();
    connect();
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}


function connect() {
    generateTable();
    var socket = new SockJS('/treasurehunter');
    stompClient = Stomp.over(socket);
    stompClient.debug = null;
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/user/queue/changes', function(jsonData){
            var response = JSON.parse(jsonData.body);
            updateMoves(response.moves);
            boat = JSON.parse(response.boat);
            updateMap(response.mapChanges);
            updateState(response.state);
            drawCanvas();
        });
        stompClient.subscribe('/user/queue/reply', function (data) {
            var response = JSON.parse(data.body);
            updateMoves(response.moves);
            worldMap = JSON.parse(response.map);
            boat = JSON.parse(response.boat);
            drawCanvas();
            drawMap();
        });
        stompClient.subscribe('/user/queue/minimap', function(data){
            updateCanvas(JSON.parse(data.body));
        });
        stompClient.send("/app/newgame", {}, '');
    });
}

function updateCanvas(data){
    drawCanvas();
    const canvas = document.getElementById('canvasMap');
    const ctx = canvas.getContext('2d');
    for (var i = 0; i < data.length; i++){
        ctx.fillStyle = "white";
        if (data[i].type == "boat"){
            ctx.fillStyle = "black";
        } else if (data[i].type == "treasure"){
            ctx.fillStyle = "yellow";
        }
        ctx.fillRect(data[i].x * minimapScale, data[i].y * minimapScale, minimapScale, minimapScale);
    }


}

function generateTable(){
    var tbl = "";
    for (var i = 0; i < radius * 2; i++){
        tbl += "<tr>"
        for (var j = 0; j < radius * 2; j++){
            tbl += "<td><img id='" + i + "-"+ j +"' src='/icons/bounds.png'></img></td>";
        }
        tbl += "</tr>";
    }
    document.getElementById("grid").innerHTML = tbl;
}

function drawCanvas(){
    var scale = minimapScale;
    document.getElementById("canvasDiv").innerHTML = "<canvas id='canvasMap' width='" + worldMap[0].length * scale+ "' height='" + worldMap.length * scale +  "'></canvas>";
    const canvas = document.getElementById('canvasMap');
    const ctx = canvas.getContext('2d');

    for (var i = 0; i < worldMap.length; i++){
        for (var j = 0; j < worldMap[0].length; j++){
            var element = worldMap[i][j];
            var colour = 'blue';
            if (element.type == "boat"){
                colour = 'black';
            } else if (element.type == "treasure"){
                colour = 'yellow';
            } else if (element.inPath == true){
                colour = 'white';
            } else if (element.type == "land"){
                colour = 'green';
            }
            ctx.fillStyle = colour;
            ctx.fillRect(j * scale, i * scale, scale, scale);
        }
    }

}


function displayTreasure(){
    drawMap();
}

function displayWin(){
    displayTreasure();
    setTimeout(function() {
        alert("You won! Press Reset to play again.");
    },100)
    disconnect();
}

function displayLoss(){
    displayTreasure();
    setTimeout(function() {
        alert("Game Over - You can out of moves. Press Reset to play again.");
    },100)
    disconnect();
}

function updateState(state){
    if (state == "won"){
        displayWin();
    } else if (state == "lost"){
        displayLoss();
    }
}

function updateMoves(moves){
    document.getElementById("numMoves").innerHTML = moves;
}


function updateMap(changes){
    var changes = JSON.parse(changes);
    for (var i = 0; i < changes.length; i++){
        var element = changes[i];
        worldMap[element.y][element.x] = element;
    }
    drawMap();
}


function drawMap(){
    var boatX = boat.x;
    var boatY = boat.y;

    var x = 0;
    var y = 0;
    for (var i = boatY - radius ; i < boatY + radius; i++){
        for (var j = boatX - radius; j < boatX + radius; j++){
            var pic = "";
            if (i < 0 || j < 0 || i >= worldMap.length || j >= worldMap[0].length){
                pic = '/icons/bounds.png';
            } else { //in bounds 
                if (worldMap[i][j].hidden){
                    pic = '/icons/water.png';
                } else {
                    pic = "/icons/" + worldMap[i][j].type + ".png";
                }  
            }
            $("#"+ y +"-" + x).attr("src", pic);
            x = x + 1;
        }
        x = 0;
        y = y + 1;
    }

}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

function moveUp(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'up'}));
    stompClient.send("/app/pathfind", {}, JSON.stringify({'map': asciiMap()}));
}

function moveDown(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'down'}));
    stompClient.send("/app/pathfind", {}, JSON.stringify({'map': asciiMap()}));
}

function moveLeft(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'left'}));
    stompClient.send("/app/pathfind", {}, JSON.stringify({'map': asciiMap()}));
}

function moveRight(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'right'}));
    stompClient.send("/app/pathfind", {}, JSON.stringify({'map': asciiMap()}));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#reset" ).click(function() { resetGame(); });
});

function asciiMap(){
    var res = [];
    for (var i = 0; i < worldMap.length; i++){
        var row = [];
        for (var j = 0; j < worldMap[0].length; j++){
            if (worldMap[i][j].type == "land"){
                row.push("L");
            } else if (worldMap[i][j].type == "boat"){
                row.push("B");
            } else if (worldMap[i][j].type == "treasure"){
                row.push("T");
            } else {
                row.push("W");
            }
        }
        res.push(row)
    }
    return res;        
}

$(document).keydown(function(e) {
    switch(e.which) {
        case 37: // left
        moveLeft();
        break;

        case 38: // up
        moveUp();
        break;

        case 39: // right
        moveRight();
        break;

        case 40: // down
        moveDown();
        break;

        default: return; // exit this handler for other keys
    }
    e.preventDefault(); // prevent the default action (scroll / move caret)
});