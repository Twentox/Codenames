package com.example.codenames.GameEngine;

public class Main {

    public static void main(String[] args) {
        GameBoard gameBoard = new GameBoard();
        String w = "Banane\nApfel\nBirne\nParika\nBaum\nHaus\nFlasche\nFanta\nHandy\nMaus\nTastatur\nUni\nBett\nTisch\nBoden";

        gameBoard.ArrStrToWordList(gameBoard.databaseStrToArrStr(w));
        gameBoard.fillGameBoardGrid();

        for(int i = 0; i < gameBoard.grid.length; i++){
            for(int j = 0; j < gameBoard.grid[0].length; j++){
                System.out.println(gameBoard.grid[i][j]);
            }
        }

        System.out.println(gameBoard.getCellValue(0,2));



    }
}
