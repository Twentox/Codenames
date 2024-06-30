package com.example.codenames.GameEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert ein InfoBoard, das Nachrichten von Spielern speichert.
 */
public class InfoBoard {
    private List<String> messages; // Liste der gespeicherten Nachrichten

    /**
     * Konstruktor für das InfoBoard.
     */
    public InfoBoard() {
        this.messages = new ArrayList<>();
    }

    /**
     * Fügt eine Nachricht mit dem Namen des Spielers zum InfoBoard hinzu.
     *
     * @param playerName Der Name des Spielers
     * @param message Die hinzuzufügende Nachricht
     */
    public void addMessage(String teamColor, String message) {
        messages.add(teamColor + ": " + message);
    }

    /**
     * Gibt alle gespeicherten Nachrichten zurück.
     *
     * @return Liste der gespeicherten Nachrichten
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Löscht alle Nachrichten aus dem InfoBoard.
     */
    public void clearMessages() {
        messages.clear();
    }
}
