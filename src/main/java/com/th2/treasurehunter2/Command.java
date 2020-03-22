package com.th2.treasurehunter2;

public class Command {
    private String command;
    
    public Command(){

    }

    public Command(String userCommand){
        this.command = userCommand;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}