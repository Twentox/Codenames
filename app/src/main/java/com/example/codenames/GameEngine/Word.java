package com.example.codenames.GameEngine;

/**
 * Die {@code Word}-Klasse speichert Informationen über ein Wort,
 * einschließlich seiner zugehörigen Farbe, Punktzahl und Auswahlstatus.
 */
public class Word {
    private ColorValue color;
    private String word;
    /**
     * Konstruktor für das Objekt {@code Word}.
     *
     * @param color die Farbe, die diesem Wort zugeordnet wird
     * @param word das Wort selbst; darf nicht leer sein und muss gültig sein
     * @throws IllegalArgumentException wenn das Wort leer oder {@code null} ist
     */
    public Word(ColorValue color, String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("Ungültiges Wort: Das Wort darf nicht leer sein.");
        }
        setColor(color);
        this.word = word;
    }

    /**
     * Gibt die Farbe des Wortes zurück.
     *
     * @return die Farbe des Wortes
     */
    public ColorValue getColor() {
        return color;
    }

    /**
     * Gibt die Farbenid des Wortes zurück.
     *
     * @return die Farbenid des Wortes
     */
    public String getColorID(){
        return color.getId();
    }

    /**
     * Gibt den Punktwert der Farbe des Wortes zurück.
     *
     * @return der Punktwert der Farbe des Wortes
     */
    public int getWordValue() {
        return color.getValue();
    }

    /**
     * Gibt das Wort zurück.
     *
     * @return das Wort
     */
    public String getWord() {
        return word;
    }

    /**
     * Setzt eine Farbe
     *
     * @param color
     */
    public void setColor(ColorValue color) {
        this.color = color;
    }

    /**
     * Gibt eine String-Repräsentation des Wortes zurück,
     * einschließlich des Wortes, des Punktwerts, der Farbe und der Farb-ID.
     *
     * @return eine String-Repräsentation des Wortes
     */
    @Override
    public String toString() {
        return "Word: " + word + ", Points: " + getWordValue() +
                ", ColorValue: " + color + ", ColorID: " + getColorID();
    }
}
