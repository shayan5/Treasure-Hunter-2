var stompClient = null;

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
    var socket = new SockJS('/treasurehunter');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/changes', function(jsonData){
            var response = JSON.parse(jsonData.body);
            updateState(response.state);
            updateMoves(response.moves);
            updateSonars(response.sonars);
            updateMap(response.mapChanges);
        });
        stompClient.subscribe('/user/queue/reply', function (data) {
            var response = JSON.parse(data.body);
            updateMoves(response.moves);
            updateSonars(response.sonars);
            worldMap = JSON.parse(response.map);
            drawMap(worldMap);
        });
        stompClient.send("/app/newgame", {}, '');
    });
}

function displayWin(){
    document.getElementById("message").innerHTML = "You won! Press Start Game to play again.";
    disconnect();
}

function displayLoss(){
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