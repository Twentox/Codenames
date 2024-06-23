package com.example.codenames;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import static java.util.regex.Pattern.matches;

import com.example.codenames.GameEngine.Game;
import com.example.codenames.GameEngine.ValueType;
import com.example.codenames.GameEngine.Word;


import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testDatabaseStrToArrStr(){
        Game game = new Game();
        String[] arrStr = game.gameBoard.databaseStrToArrStr("Apfel\nBirne\nTraube");
        assertArrayEquals(arrStr, new String[]{"Apfel", "Birne", "Traube"});
    }

    @Test
    public void testArrStrToWordList(){
        Game game = new Game();
        game.gameBoard.ArrStrToWordList(new String[]{"Apfel", "Birne", "Traube"});
        ArrayList<Word> sampleWords = new ArrayList<>();
        sampleWords.add(new Word(ValueType.RED, "Apfel"));
        sampleWords.add(new Word(ValueType.RED, "Birne"));
        sampleWords.add(new Word(ValueType.RED, "Traube"));
        assertEquals(game.gameBoard.words,sampleWords);
    }

    @Test
    public void testMixWordList(){
        Game game = new Game();
        game.gameBoard.ArrStrToWordList(new String[]{"Apfel", "Birne", "Traube"});
        ArrayList<Word> sampleWords = new ArrayList<>();
        sampleWords.add(new Word(ValueType.RED, "Apfel"));
        sampleWords.add(new Word(ValueType.RED, "Birne"));
        sampleWords.add(new Word(ValueType.RED, "Traube"));
        game.gameBoard.mixWordList();
        assertNotEquals(game.gameBoard.words, sampleWords);

    }

    @Test
    public void testCalculateWordPoints(){
        Game game = new Game();
        assertEquals(game.gameBoard.calculateWordPoints(new Word(ValueType.RED, "Apfel")), -1);
    }



}