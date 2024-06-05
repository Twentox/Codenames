package com.example.codenames.GameEngine;

public class Word {

    private ValueType color;
    private String word;
    private boolean isChoosen = false;

    public Word(ValueType value, String word){
        this.color= value;
        this.word = word;
    }

    public ValueType getColor(){
        return this.color;
    }

    public String getWord(){
        return this.word;
    }

    // TODO getPoints()

    public String toString(){
        return "Word: " + word + ", ValueType: " + color + ", ColorID: " + color.getValues();
    }
}
