package com.example.codenames.GameEngine;

import java.net.Socket;
import java.util.List;

public interface Game {

    // GameBoard Management;
    void createGameBoard(List<String> words);
    void addWordToGameBoard(String word, ColorValue color);
    void removeWordFromGameBoard(String word);
    void resetGameBoard();

    // Team Management
    void createTeam(Player leader, String teamColor);
    void addTeamMembers(Player leader, String teamColor, Player ... players);
    void addTeamPoints(String teamColor, int points);
    void removeTeamPoints(String teamColor, int points);
    List<Player> getTeamMembers(String teamColor);

    // Game Management
    boolean isGameActive();
    boolean isGameStartReady();

    // Player Management
    void addPlayer(Player player);
    void removePlayer(Player player);
    Socket getPlayerDetails(String playerName);

}
