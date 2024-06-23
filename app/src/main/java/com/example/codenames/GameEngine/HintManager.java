package com.example.codenames.GameEngine;

public class HintManager {


    private String hint;
    private int meaningWords;


    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean validateHint(){
        return true;
    }

    public void setMeaningWords(int meaningWords) {
        this.meaningWords = meaningWords;
    }
}
