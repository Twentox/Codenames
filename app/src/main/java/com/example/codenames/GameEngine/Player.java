package com.example.codenames.GameEngine;

public class Player {

    private String name;
    // TODO Socket


    public void Player(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
