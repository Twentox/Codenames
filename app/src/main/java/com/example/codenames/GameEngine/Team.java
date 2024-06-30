package com.example.codenames.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Die Klasse repräsentiert ein Team von Spielern.
 */
public class Team {
    private List<Player> memberList; // Liste der Teammitglieder
    private Player leader; // Der Teamleiter
    private String teamColor; // Die Farbe des Teams

    /**
     * Konstruktor für ein Team.
     *
     * @param teamColor Die Farbe des Teams
     * @param leader    Der Teamleiter
     * @param members   Weitere Mitglieder des Teams
     * @throws IllegalArgumentException wenn die Teamfarbe ungültig ist oder ein Mitglied null ist
     */
    public Team(String teamColor, Player leader, Player... members) {
        if (teamColor == null || teamColor.trim().isEmpty()) {
            throw new IllegalArgumentException("Teamfarbe ist ungültig");
        }

        this.teamColor = teamColor;
        setLeader(leader);

        memberList = new ArrayList<>();
        for (Player member : members) {
            try {
                if (!member.equals(leader)) {
                    addMember(member);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            memberList.add(leader);
        }
    }
    /**
     * Konstruktor für ein Team.
     *
     * @param teamColor Die Farbe des Teams
     * @param leader    Der Teamleiter
     * @throws IllegalArgumentException wenn die Teamfarbe ungültig ist oder ein Mitglied null ist
     */
    public Team(String teamColor, Player leader) {
        if (teamColor == null || teamColor.trim().isEmpty()) {
            throw new IllegalArgumentException("Teamfarbe ist ungültig");
        }

        this.teamColor = teamColor;
        setLeader(leader);

        memberList = new ArrayList<>();
    }

    /**
     * Fügt ein neues Mitglied zum Team hinzu.
     *
     * @param member Das hinzuzufügende Mitglied
     * @throws IllegalArgumentException wenn das Mitglied null ist
     */
    public void addMember(Player member) {
        if (member == null) {
            throw new IllegalArgumentException("Mitspieler ist nicht vorhanden");
        }
        memberList.add(member);
    }

    /**
     * Entfernt ein Mitglied aus dem Team.
     *
     * @param member Das zu entfernende Mitglied
     * @throws IllegalArgumentException wenn das Mitglied null ist
     */
    public void removeMember(Player member) {
        if (member == null) {
            throw new IllegalArgumentException("Mitspieler ist nicht vorhanden");
        }
        if (!member.equals(leader)){
            memberList.remove(member);
        }
    }

    /**
     * Setzt den Teamleiter.
     *
     * @param leader Der neue Teamleiter
     * @throws IllegalArgumentException wenn der Teamleiter null ist
     */
    public void setLeader(Player leader) {
        if (leader == null) {
            throw new IllegalArgumentException("Teamleiter ist nicht vorhanden");
        }
        this.leader = leader;
    }

    /**
     * Gibt den Teamleiter zurück.
     *
     * @return Der Teamleiter
     */
    public Player getLeader() {
        return leader;
    }

    /**
     * Gibt die Liste der Teammitglieder zurück.
     *
     * @return Liste der Teammitglieder
     */
    public List<Player> getMemberList() {
        return memberList;
    }

    /**
     * Gibt die Farbe des Teams zurück.
     *
     * @return Die Farbe des Teams
     */
    public String getTeamColor() {
        return teamColor;
    }

    /**
     * Setzt die Farbe des Teams.
     *
     * @param teamColor Die neue Farbe des Teams
     */
    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    /**
     * Vergleicht dieses Team-Objekt mit einem anderen Objekt.
     *
     * @param o Das zu vergleichende Objekt
     * @return true, wenn beide Objekte gleich sind, andernfalls false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        return Objects.equals(teamColor, team.teamColor) &&
                Objects.equals(leader, team.leader) &&
                Objects.equals(memberList, team.memberList);
    }

    /**
     * Gibt eine textuelle Darstellung dieses Team-Objekts zurück.
     *
     * @return Eine textuelle Darstellung dieses Team-Objekts
     */
    @Override
    public String toString() {
        return "Team{" +
                "teamColor='" + teamColor + '\'' +
                ", leader=" + leader +
                ", memberList=" + memberList +
                '}';
    }
}
