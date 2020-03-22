var stompClient = null;
var worldMap = null;
var boat = null;
var treasure = null;

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
            updateMap(JSON.parse(jsonData.body));
        });
        stompClient.subscribe('/user/queue/reply', function (data) {
            var response = JSON.parse(data.body);
            worldMap = JSON.parse(response.map);
            boat = JSON.parse(response.boat);
            treasure = JSON.parse(response.treasure);
            drawMap();
        });
        stompClient.send("/app/newgame", {}, '');
    });
}

function updateMap(changes){
    for (var i = 0; i < changes.length; i++){
        var element = changes[i];
        document.getElementById(element.y + "-" + element.x).innerHTML = "<img src='/icons/" + element.type + ".png'>";
    }

}

function drawMap(){
    var tbl = "<tr>"
    for (var i = 0; i < worldMap.length; i++){
        for (var j = 0; j < worldMap[0].length; j++){
            tbl += "<td id=" + i + "-" + j + ">";
            if (worldMap[i][j].type == "water"){
                tbl += "<img src='/icons/water.png'>";
            } else if (worldMap[i][j].type == "land"){
                tbl += "<img src='/icons/land.png'>";
            } else if (worldMap[i][j].type == "boat"){
                tbl += "<img src='/icons/boat.png'>";
            } else if (worldMap[i][j].type == "treasure"){
                tbl += "<img src='/icons/treasure.png'>";
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

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
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