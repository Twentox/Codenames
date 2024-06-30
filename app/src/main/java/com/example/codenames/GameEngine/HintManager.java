package com.example.codenames.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Diese Klasse verwaltet Hinweise und deren Anzahl für ein Codenames-Spiel.
 */
public class HintManager {
    private List<String> words; // Liste der verfügbaren Wörter
    private boolean isRevealed; // Gibt an, ob der Hinweis bereits aufgedeckt wurde
    private String hint; // Der aktuelle Hinweis
    private int hintAmount; // Die Anzahl der mit dem Hinweis verbundenen Wörter

    /**
     * Konstruktor für HintManager.
     *
     * @param words Map mit den verfügbaren Wörtern.
     */
    public HintManager(Map<String, Word> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Ungültige Wörterliste");
        }

        this.words = new ArrayList<>(words.keySet());
        this.isRevealed = false;
        this.hint = null;
        this.hintAmount = -1;
    }

    /**
     * Setzt einen neuen Hinweis.
     *
     * @param hint Der Hinweis, der gesetzt werden soll.
     * @throws IllegalArgumentException wenn der Hinweis ungültig ist.
     */
    public void setHint(String hint) {
        if (hint == null || hint.trim().isEmpty()) {
            throw new IllegalArgumentException("Ungültiger Hinweis");
        }

        if (!isRevealed && checkHint(hint)) {
            this.hint = hint;
        }
    }

    /**
     * Setzt die Anzahl der mit dem Hinweis verbundenen Wörter.
     *
     * @param hintAmount Die Anzahl der Wörter.
     * @throws IllegalArgumentException wenn die Anzahl ungültig ist.
     */
    public void setHintAmount(int hintAmount) {
        if (hintAmount < 0 || hintAmount > 9) {
            throw new IllegalArgumentException("Ungültige Anzahl von Hinweisen");
        }

        if (!isRevealed) {
            this.hintAmount = hintAmount;
        }
    }

    /**
     * Gibt den Hinweis zurück und markiert ihn als aufgedeckt.
     *
     * @return Der aktuelle Hinweis.
     */
    public String revealHint() {
        isRevealed = true;
        return hint;
    }

    /**
     * Gibt die Anzahl der mit dem Hinweis verbundenen Wörter zurück und markiert sie als aufgedeckt.
     *
     * @return Die Anzahl der Wörter.
     */
    public int revealHintAmount() {
        isRevealed = true;
        return hintAmount;
    }

    /**
     * Überprüft, ob der gegebene Hinweis gültig ist.
     *
     * @param hint Der zu überprüfende Hinweis.
     * @return true, wenn der Hinweis gültig ist, sonst false.
     */
    public boolean checkHint(String hint) {
        for (String word : words) {
            if (word.equalsIgnoreCase(hint)) {
                return false;
            }
        }
        return true;
    }

    public int getHintAmount() {
        return hintAmount;
    }

    public String getHint() {
        return hint;
    }

    public List<String> getWords() {
        return words;
    }

    public boolean isRevealed() {
        return isRevealed;
    }
}
