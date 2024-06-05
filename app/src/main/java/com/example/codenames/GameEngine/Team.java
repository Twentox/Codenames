package com.example.codenames.GameEngine;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private ValueType team;
    private List<String> role = new ArrayList<String>();

    private List<Player> playersList = new ArrayList<>();

    public void Team(ValueType team, List<String> role){
        setTeam(team);
        setRole(role);
    }

    public ValueType getTeam() {
        return team;
    }

    public void setTeam(ValueType team) {
        this.team = team;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {this.role = role;}
}
