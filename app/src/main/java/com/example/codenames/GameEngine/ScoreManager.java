package com.example.codenames.GameEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Die Klasse ScoreManager verwaltet die Punkte der Teams und bestimmt den Gewinner sowie die Verlierer.
 */
public class ScoreManager {
    private Map<Team, Integer> teams;
    private int totalScore;
    private Team winner;
    private boolean hasWinner = false;
    private List<Team> losers;
    private final int maxTeamScore;

    /**
     * Konstruktor für den ScoreManager.
     *
     * @param totalScore   Der anfängliche Gesamtpunktestand.
     * @param maxTeamScore Die maximale Punktzahl, die ein Team erreichen kann.
     * @param teams        Die teilnehmenden Teams.
     * @throws IllegalArgumentException wenn maxTeamScore kleiner oder gleich 0 ist oder totalScore kleiner als 0 ist.
     */
    public ScoreManager(int totalScore, int maxTeamScore, List<Team> teams) {
        if (maxTeamScore <= 0) throw new IllegalArgumentException("Ungültiger Maximalpunktestand");
        if (totalScore < 0) throw new IllegalArgumentException("Ungültiger Gesamtpunktestand");

        this.maxTeamScore = maxTeamScore;
        this.totalScore = totalScore;
        this.teams = new HashMap<>();
        this.losers = new ArrayList<>();

        for (Team team : teams) {
            addTeam(team);
        }
    }

    /**
     * Fügt ein Team hinzu.
     *
     * @param team Das hinzuzufügende Team.
     * @throws IllegalArgumentException wenn das Team null ist.
     */
    public void addTeam(Team team) {
        if (team == null) throw new IllegalArgumentException("Ungültiges Team");
        if (!teams.containsKey(team)) teams.put(team, 0);
    }

    /**
     * Entfernt ein Team.
     *
     * @param team Das zu entfernende Team.
     * @throws IllegalArgumentException wenn das Team null ist.
     */
    public void removeTeam(Team team) {
        if (team == null) throw new IllegalArgumentException("Ungültiges Team");
        teams.remove(team);
    }

    /**
     * Fügt einem Team Punkte hinzu.
     *
     * @param team   Das Team, dem Punkte hinzugefügt werden sollen.
     * @param points Die Anzahl der hinzuzufügenden Punkte.
     * @throws IllegalArgumentException wenn das Team null ist oder die Punkte negativ sind.
     */
    public void addPoints(Team team, int points) {
        if (team == null) throw new IllegalArgumentException("Ungültiges Team");
        if (points < 0) throw new IllegalArgumentException("Ungültige Punkte");

        if (!hasWinner) {
            if (teams.containsKey(team)) {
                int currentPoints = teams.get(team);
                if (currentPoints < maxTeamScore) {
                    int newPoints = currentPoints + points;
                    if (newPoints >= maxTeamScore) {
                        newPoints = maxTeamScore;
                        winner = team;
                        hasWinner = true;
                        updateLosers();
                    }
                    teams.put(team, newPoints);
                    totalScore += (newPoints - currentPoints);
                }
            }
        }
    }

    /**
     * Entfernt einem Team Punkte.
     *
     * @param team   Das Team, dem Punkte entfernt werden sollen.
     * @param points Die Anzahl der zu entfernenden Punkte.
     * @throws IllegalArgumentException wenn das Team null ist oder die Punkte negativ sind.
     */
    public void removePoints(Team team, int points) {
        if (team == null) throw new IllegalArgumentException("Ungültiges Team");
        if (points < 0) throw new IllegalArgumentException("Ungültige Punkte");

        if (!hasWinner) {
            if (teams.containsKey(team)) {
                int currentPoints = teams.get(team);
                int newPoints = currentPoints - points;
                if (newPoints < 0) newPoints = 0;

                teams.put(team, newPoints);
                totalScore -= points;
            }
        }
    }

    /**
     * Aktualisiert die Liste der Verlierer basierend auf dem Gewinnerteam.
     */
    private void updateLosers() {
        losers.clear();
        for (Team team : teams.keySet()) {
            if (!team.equals(winner)) {
                losers.add(team);
            }
        }
    }

    /**
     * Gibt die maximale Punktzahl zurück, die ein Team erreichen kann.
     *
     * @return die maximale Punktzahl.
     */
    public int getMaxTeamScore() {
        return maxTeamScore;
    }

    /**
     * Gibt den Gesamtpunktestand zurück.
     *
     * @return der Gesamtpunktestand.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Gibt die Map der Teams und deren Punktestände zurück.
     *
     * @return die Map der Teams und deren Punktestände.
     */
    public Map<Team, Integer> getTeams() {
        return teams;
    }

    /**
     * Gibt die Punktzahl eines bestimmten Teams zurück.
     *
     * @param team Das Team, dessen Punktzahl zurückgegeben werden soll.
     * @return die Punktzahl des Teams, oder -1 wenn das Team nicht existiert.
     * @throws IllegalArgumentException wenn das Team null ist.
     */
    public int getTeamPoints(Team team) {
        if (team == null) throw new IllegalArgumentException("Ungültiges Team");
        return teams.getOrDefault(team, -1);
    }

    /**
     * Gibt die Punktestände aller Teams als String zurück.
     *
     * @return die Punktestände aller Teams.
     */
    public String getAllTeamPoints() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Team, Integer> entry : teams.entrySet()) {
            sb.append("Team ").append(entry.getKey().getTeamColor()).append(": hat ").append(entry.getValue()).append(" Punkte\n");
        }
        return sb.toString();
    }

    /**
     * Gibt das Gewinnerteam zurück.
     *
     * @return das Gewinnerteam.
     */
    public Team getWinner() {
        return winner;
    }

    /**
     * Gibt die Liste der Verliererteams zurück.
     *
     * @return die Liste der Verliererteams.
     */
    public List<Team> getLosers() {
        return losers;
    }
}
