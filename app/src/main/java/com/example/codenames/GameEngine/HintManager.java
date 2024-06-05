package com.example.codenames.GameEngine;

public class HintManager {


    private String hint;
    private int meaningWords;

    public HintManager(String hint, int meaningWords){
        setHint(hint);
        setMeaningWords(meaningWords);
    }

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
