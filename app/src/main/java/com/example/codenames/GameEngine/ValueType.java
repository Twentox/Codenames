package com.example.codenames.GameEngine;

public enum ValueType {
    BLUE(1), RED(2),YELLOW(3),BLACK(4);

    private int values;

    private ValueType(int values){
        this.values = values;
    }

    public int getValues(){
        return values;
    }
}
