package com.example.codenames.GameEngine;

public interface GameEngine {
    Game game = new Game();
    GameBoard gameBoard = new GameBoard();
    HintManager hintManager = new HintManager();
    boolean isHost = false;
    Messenger messenger = new Messenger();


    public void gameStart();

    public void gameFinished();

    public void gamePause();

    public void switchTurn();


}
