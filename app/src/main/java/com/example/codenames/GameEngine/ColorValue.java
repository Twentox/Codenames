package com.example.codenames.GameEngine;

/**
 * Das {@code ColorValue}-Enum repräsentiert verschiedene Farbwerte,
 * die mit einer ID und einem Punktwert verknüpft sind.
 */
public enum ColorValue {
    BLUE("blue", 1),
    RED("red", 1),
    YELLOW("yellow", 0),
    BLACK("black", -1);

    private final String id;
    private final int value;

    /**
     * Konstruktor für {@code ColorValue}.
     *
     * @param id die ID der Farbe
     * @param value der Punktwert der Farbe
     */
    ColorValue(String id, int value) {
        this.id = id;
        this.value = value;
    }

    /**
     * Gibt den Punktwert der Farbe zurück.
     *
     * @return der Punktwert der Farbe
     */
    public int getValue() {
        return value;
    }

    /**
     * Gibt die ID der Farbe zurück.
     *
     * @return die ID der Farbe
     */
    public String getId() {
        return id;
    }
}
