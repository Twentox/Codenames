package com.example.codenames.GameEngine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameBoard {
    public List<Word> words = new ArrayList<Word>();
    protected Word[][] grid = new Word[3][5];
    protected ArrayList<Word> choosenWords = new ArrayList<Word>();

    public GameBoard(){
    }

    public void init(){


    }

    public String[] databaseStrToArrStr(String databaseWords){
        if(databaseWords != ""){
            String[] StrWords = databaseWords.split("\n");
            return StrWords;
        }else{
            throw new IllegalArgumentException("String ist Leer");
        }
    }

    public void ArrStrToWordList(String[] StrWords){
        int colorCounter = 1;
        for(int i = 0; i < StrWords.length; i++){
            if(colorCounter <= 4){
                words.add(new Word(ValueType.RED, StrWords[i]));
            } else if(colorCounter > 4 && colorCounter <= 8){
                words.add(new Word(ValueType.BLUE, StrWords[i]));
            } else if (colorCounter > 8 && colorCounter <= 14) {
                words.add(new Word(ValueType.YELLOW, StrWords[i]));
            }else{
                words.add(new Word(ValueType.BLACK, StrWords[i]));
            }
            colorCounter = colorCounter + 1;

        }
    }

    public void fillGameBoardGrid(){
        mixWordList();
        int index = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                if(index < words.size()){
                    grid[i][j] = words.get(index);
                    index++;
                }else{
                    grid[i][j] = null;
                }
            }
        }
    }

    public void mixWordList(){
        Collections.shuffle(words);
    }

    public void updateWordList(Word word){
        if(words.contains(word)){
            words.remove(word);
        }
    }

    public Word getCellValue(int row, int column){
        if(row <= grid.length && row > -1 && column > -1 && column <= grid[0].length){
            return grid[row][column];
        }else {
            throw new IllegalArgumentException("row oder column ist zu gross oder Klein");
        }
    }

    public void isCorrectGuess(Word word, ArrayList<Word> hintWordList){
        hintWordList.contains(word);
        calculateWordPoints(word);
        updateWordList(word);
    }


    public int calculateWordPoints(Word word){
        if(word.getColor().getValues() == 1 || word.getColor().getValues() == 2){
            return -1;
        } else if (word.getColor().getValues() == 3) {
            return 0;
        }else {
            return 1;
        }
    }
}
