package com.example.codenames.GameEngine;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public GameBoard gameBoard = new GameBoard();
    private List<Team> teams = new ArrayList<Team>();
    private ScoreManager scoreManager;
    private Messenger messenger;
    private HintManager hintManager;
    private boolean isHost;




    public void initGameBoard(){
        gameBoard = new GameBoard();
    }




}
