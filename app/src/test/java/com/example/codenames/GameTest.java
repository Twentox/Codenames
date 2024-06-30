/*
package com.example.codenames;

import com.example.codenames.GameEngine.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTest {
    private HintManager hintManager;
    private GameBoard gameBoard;
    private ScoreManager scoreManager;
    private InfoBoard infoBoard;
    private Map<String,Team> teams;

    public GameTest(){
        teams = new HashMap<>();
        infoBoard = new InfoBoard();
    }

    public void createGameBoard(List<String> words){
        try{
            gameBoard = new GameBoard(15,words);
            hintManager = new HintManager(gameBoard.getWords());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void createTeam(Player leader, String teamColor){
        try {
            if (teams.size() == 1)
                scoreManager = new ScoreManager(gameBoard.getTotalWords(),gameBoard.getTotalTeamwords(), List.of((Team) teams));

            if (isTeamAvailble(teamColor)){
                Team team = new Team(teamColor,leader);
                teams.put(teamColor,team);
                scoreManager.addTeam(team);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addTeamMembers(Player leader, String teamColor, Player ... players){
        try {
            if (!isTeamAvailble(teamColor)){
                for (Player player: players){
                    teams.get(teamColor).addMember(player);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isTeamAvailble(String teamColor){
        return teams.get(teamColor) == null;
    }

    public void addTeamPoint(String teamColor, int points){
        try {
            if (!isTeamAvailble(teamColor)){
                scoreManager.addPoints(teams.get(teamColor),points);
                updateInfoboard(teamColor,": wurden " + points + " hinzugefügt");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeTeamPoint(String teamColor, int points){
        try {
            if (!isTeamAvailble(teamColor)){
                scoreManager.removePoints(teams.get(teamColor),points);
                updateInfoboard(teamColor,": wurden " + points + " abgezogen");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, Team> getTeams() {
        return teams;
    }

    public boolean isGameBoardReady(){
        return gameBoard.areMissingWords() == 0;
    }

    public void addWordToGameBoard(String word, ColorValue color){
        try {
            gameBoard.addWord(word,color);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void removeWordToGameBoard(String word, ColorValue color){
        try {
            gameBoard.removeWord(word);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getHint(){
        return hintManager.revealHint() + String.valueOf(hintManager.revealHintAmount());
    }

    public void setHint(String hint, int amount){
        try {
            hintManager.setHint(hint);
            hintManager.setHintAmount(amount);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void updateInfoboard(String teamColor, String msg){
        try{
            infoBoard.addMessage(teamColor,msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

 */


