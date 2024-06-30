package com.example.codenames.GameEngine;

/**
 * Class representing a player in the game.
 */
public class Player {
    private String name;
    private GameRoles role;

    /**
     * Constructs a player with a specified name and role.
     *
     * @param name The name of the player. Must not be null or empty.
     * @param role The role of the player in the game.
     * @throws IllegalArgumentException if the name is null or empty.
     */
    public Player(String name, GameRoles role){
        setName(name);
        setRole(role);
    }

    public Player(String name){
        setName(name);
    }

    /**
     * Retrieves the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     *
     * @param name The name to set for the player. Must not be null or empty.
     * @throws IllegalArgumentException if the name is null or empty.
     */
    public void setName(String name) {
        if (name.trim().isEmpty() || name == null) {
            throw new IllegalArgumentException("Ungültiger Name");
        }
        this.name = name;
    }

    /**
     * Sets the role of the player.
     *
     * @param role The role to set for the player.
     */
    public void setRole(GameRoles role) {
        this.role = role;
    }

    /**
     * Retrieves the role of the player.
     *
     * @return The role of the player.
     */
    public GameRoles getRole() {
        return role;
    }
}
