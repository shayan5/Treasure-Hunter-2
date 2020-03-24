var stompClient = null;
var worldMap = null;
var boat = null;

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
    document.getElementById("message").innerHTML = "";
    var socket = new SockJS('/treasurehunter');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/changes', function(jsonData){
            var response = JSON.parse(jsonData.body);
            updateMoves(response.moves);
            updateSonars(response.sonars);
            boat = JSON.parse(response.boat);
            updateMap2(response.mapChanges);
            updateState(response.state);
            //drawOverWorld();
            drawCanvas();
            //updateMap(response.mapChanges);
        });
        stompClient.subscribe('/user/queue/reply', function (data) {
            var response = JSON.parse(data.body);
            updateMoves(response.moves);
            updateSonars(response.sonars);
            worldMap = JSON.parse(response.map);
            boat = JSON.parse(response.boat);
            //drawMap(worldMap);
            drawMap2();
            //drawOverWorld();
            drawCanvas();
        });
        stompClient.send("/app/newgame", {}, '');
    });
}

function drawCanvas(){
    var scale = 5;
    //alert("<canvas id='canvasMap' width='" + worldMap[0].length + "' height='" + worldMap.length +  "'></canvas>");
    document.getElementById("canvasDiv").innerHTML = "<canvas id='canvasMap' width='" + worldMap[0].length * scale+ "' height='" + worldMap.length * scale +  "'></canvas>";
    //document.getElementById("canvasDiv").innerHTML = "<canvas id='canvasMap'</canvas>";
    const canvas = document.getElementById('canvasMap');
    const ctx = canvas.getContext('2d');
    //ctx.scale(5, 5);

    for (var i = 0; i < worldMap.length; i++){
        for (var j = 0; j < worldMap[0].length; j++){
            var element = worldMap[i][j];
            var colour = 'blue';
            if (element.type == "boat"){
                colour = 'black';
            } else if (element.type == "treasure" /*&& element.hidden == false*/){
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

function drawOverWorld(){
    var map = "";
    for (var i = 0; i < worldMap.length; i++){
        var row = "<p>"
        for (var j = 0; j < worldMap[0].length; j++){
            if (worldMap[i][j].type == "boat"){
                row += "*";
            } else {
                row += "-";
            }
        }
        row += "</p>";
        map += row;
    }
    document.getElementById("overWorld").innerHTML = map;
}

function displayTreasure(){
    /*
    for (var i = 0; i < worldMap.length; i++){
        for (var j = 0; j < worldMap[0].length; j++){
            if (worldMap[i][j].type == "treasure"){
                worldMap[i][j].hidden = false;
                alert(worldMap[i][j].hidden);
            }
        }
    }*/
    drawMap2();
}

function displayWin(){
    displayTreasure();
    document.getElementById("message").innerHTML = "You won! Press Start Game to play again.";
    disconnect();
}

function displayLoss(){
    displayTreasure();
    document.getElementById("message").innerHTML = "Game Over - Press Start Game to play again.";
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

function updateSonars(sonars){
    document.getElementById("numSonars").innerHTML = sonars;
}

function updateMap2(changes){
    var changes = JSON.parse(changes);
    for (var i = 0; i < changes.length; i++){
        var element = changes[i];
        worldMap[element.y][element.x] = element;
    }
    drawMap2();
}

function updateMap(changes){
    for (var i = 0; i < changes.length; i++){
        var element = changes[i];
        if (element.inPath == true && element.type != "treasure"){
            document.getElementById(element.y + "-" + element.x).innerHTML = "<img src='/icons/sonar.png'>";
        } else {
            document.getElementById(element.y + "-" + element.x).innerHTML = "<img src='/icons/" + element.type + ".png'>";
        }
    }

}

function drawMap2(){
    var radius = 5;
    var boatX = boat.x;
    var boatY = boat.y;

    var tbl = "<tr>" 
    for (var i = boatY - radius ; i < boatY + radius; i++){
        for (var j = boatX - radius; j < boatX + radius; j++){
            tbl += "<td id=" + i + "-" + j + ">"; //out of bounds
            if (i < 0 || j < 0 || i >= worldMap.length || j >= worldMap[0].length){
                tbl +=  "<img src='/icons/bounds.png'>";
            } else { //in bounds
                if (worldMap[i][j].inPath == true && worldMap[i][j].type == "treasure"){
                    tbl +=  "<img src='/icons/treasure.png'>";
                } else if (worldMap[i][j].inPath == true){
                    tbl +=  "<img src='/icons/sonar.png'>";
                } else {
                    if (worldMap[i][j].hidden){
                        tbl +=  "<img src='/icons/water.png'>"
                    } else {
                        tbl +=  "<img src='/icons/" + worldMap[i][j].type + ".png'>";
                    }
                }
            }
            tbl += "</td>";
        }
        tbl += "</tr><tr>";
    }
    tbl += "</tr>";
    document.getElementById("grid").innerHTML = tbl;
}

function drawMap(worldMap){
    var tbl = "<tr>"
    for (var i = 0; i < worldMap.length; i++){
        for (var j = 0; j < worldMap[0].length; j++){
            tbl += "<td id=" + i + "-" + j + ">";
            if (worldMap[i][j].hidden == true){
                tbl +=  "<img src='/icons/water.png'>";
            } else {
                tbl +=  "<img src='/icons/" + worldMap[i][j].type + ".png'>";
            }
            tbl += "</td>";
        }    
        tbl += "</tr><tr>";
    }
    tbl += "</tr>";
    document.getElementById("grid").innerHTML = tbl;
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function fireSonar(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'sonar'}));
}

function moveUp(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'up'}));
}

function moveDown(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'down'}));
}

function moveLeft(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'left'}));
}

function moveRight(){
    stompClient.send("/app/command", {}, JSON.stringify({'command' : 'right'}));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});

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