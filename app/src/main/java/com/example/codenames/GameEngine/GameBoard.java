package com.example.codenames.GameEngine;

import java.util.*;

/**
 * Repräsentiert ein Spielbrett für ein wortbasiertes Spiel.
 * Verwalten von Wörtern, deren Farben und Nachverfolgen gewählter Wörter.
 */
public class GameBoard {
    public static final int minWordAmount = 15;
    private final int maxWords;
    public Map<String, Word> words;
    // TODO private
    private Map<String, Word> chosenWords;
    private Map<ColorValue, Integer> colorLimits;

    /**
     * Konstruiert ein GameBoard-Objekt mit einer Liste von Wörtern.
     *
     * @param wordsList die Liste der Wörter zur Initialisierung des Spielbretts
     * @throws IllegalArgumentException wenn die bereitgestellte wordsList null ist,
     *                                  oder wenn die Anzahl der Wörter zu gering für ein Spiel ist (weniger als 15),
     *                                  oder wenn die Anzahl der Wörter nicht durch 3 teilbar ist, wenn weniger als 15
     */
    public GameBoard(int maxWords, List<String> wordsList) {
        if (wordsList == null)
            throw new IllegalArgumentException("Ungültige Wörterliste");
        if (wordsList.size() < minWordAmount)
            throw new IllegalArgumentException("Anzahl der Wörter ist zu niedrig für ein Spiel (mindestens 15 Wörter)");
        if (wordsList.size() < minWordAmount && wordsList.size() % 3 != 0)
            throw new IllegalArgumentException("Anzahl der fehlenden Wörter für ein Spiel: " + (3 - (wordsList.size() % 3)));
        if (maxWords < 15 || Double.isInfinite(maxWords) || Double.isNaN(maxWords))
            throw new IllegalArgumentException("Ungültige maxWords Angabe");
        if (maxWords % 3 != 0)
            throw new IllegalArgumentException("Anzahl der maximalen Wörter ist nicht valide: Schreibe nur in Dreierschritten");

        this.maxWords = maxWords;

        words = new HashMap<>();
        chosenWords = new HashMap<>();
        colorLimits = new EnumMap<>(ColorValue.class);
        initializeColorLimits(maxWords);
        assignWordValues(wordsList);
    }

    /**
     * Initialisiert die Limits für jede Farbe basierend auf der Gesamtanzahl der Wörter.
     *
     * @param totalLength die Gesamtanzahl der Wörter
     */
    private void initializeColorLimits(int totalLength) {
        int loseWordAmount = 1;
        int teamWordsAmountOne = (totalLength) / 3;

        // TODO int teamWordsAmountOne = (totalLength - 1) / 3;
        int teamWordsAmountTwo = (totalLength) / 3;

        int neutrallyWordsAmount = totalLength - loseWordAmount - teamWordsAmountOne - teamWordsAmountTwo;


        colorLimits.put(ColorValue.BLACK, loseWordAmount);
        colorLimits.put(ColorValue.RED, teamWordsAmountOne);
        colorLimits.put(ColorValue.BLUE, teamWordsAmountTwo);
        colorLimits.put(ColorValue.YELLOW, neutrallyWordsAmount);
    }

    /**
     * Weist Farben basierend auf den Spielregeln den Wörtern zu.
     *
     * @param wordsList die Liste der Wörter, denen Farben zugewiesen werden sollen
     */
    private void assignWordValues(List<String> wordsList) {
        Random random = new Random();
        List<ColorValue> availableColors = new ArrayList<>(colorLimits.keySet());

        for (String word : wordsList) {
            ColorValue assignedColor = getRandomAvailableColor(random, availableColors);
            addWord(word, assignedColor);
        }
    }

    /**
     * Gibt eine zufällige verfügbare Farbe aus der Liste der verfügbaren Farben zurück.
     *
     * @param random         die Zufallsinstanz
     * @param availableColors die Liste der verfügbaren Farben
     * @return eine zufällige verfügbare Farbe
     */
    private ColorValue getRandomAvailableColor(Random random, List<ColorValue> availableColors) {
        ColorValue color = null;
        while (color == null) {
            ColorValue candidate = availableColors.get(random.nextInt(availableColors.size()));
            if (colorLimits.get(candidate) > 0) {
                color = candidate;
            } else {
                availableColors.remove(candidate);
            }
        }
        return color;
    }

    /**
     * Fügt dem Spielbrett ein Wort mit einer angegebenen Farbe hinzu.
     *
     * @param word  das hinzuzufügende Wort
     * @param color die mit dem Wort verbundene Farbe
     */
    public void addWord(String word, ColorValue color) {
        if (words.size() <= maxWords) {
            if (colorLimits.get(color) > 0) {
                words.put(word, new Word(color, word));
                colorLimits.put(color, colorLimits.get(color) - 1);

            } else {
                throw new IllegalArgumentException("Farbe " + color + " ist nicht verfügbar.");
            }
        }
    }

    /**
     * Bewegt ein Wort vom Hauptbrett zur Sammlung der gewählten Wörter.
     *
     * @param word das Wort, das zu den gewählten Wörtern verschoben werden soll
     * @throws IllegalArgumentException wenn das Wort leer oder null ist,
     *                                  oder wenn das Wort nicht auf dem Spielbrett gefunden wird
     */
    public void addChosenWord(String word) {
        if (word == null || word.trim().isEmpty())
            throw new IllegalArgumentException("Ungültiges Wort");

        Word target = words.get(word);

        if (target == null)
            throw new IllegalArgumentException("Ungültiges Wort: Wort befindet sich nicht im Spielfeld");

        words.remove(word);
        chosenWords.put(word, target);
    }

    /**
     * Berechnet die Anzahl der fehlenden Wörter, die benötigt werden, um die Spielanforderungen zu erfüllen.
     *
     * @return die Anzahl der fehlenden Wörter
     */
    public int areMissingWords() {
        if (words.size() < minWordAmount)
            return minWordAmount - words.size();
        else if (words.size() >= 15 && words.size() % 3 != 0)
            return 3 - (words.size() % 3);
        else
            return 0;
    }

    /**
     * Gibt eine Zeichenkette zurück, die das Spielbrett repräsentiert.
     *
     * @return eine Zeichenkette, die das Spielbrett repräsentiert
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game Board:\n");

        sb.append("Words:\n");
        for (Map.Entry<String, Word> entry : words.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }

        sb.append("Chosen Words:\n");
        for (Map.Entry<String, Word> entry : chosenWords.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Gibt die Karte der gewählten Wörter zurück.
     *
     * @return die Karte der gewählten Wörter
     */
    public Map<String, Word> getChosenWords() {
        return chosenWords;
    }

    /**
     * Gibt die Karte der Wörter auf dem Spielbrett zurück.
     *
     * @return die Karte der Wörter auf dem Spielbrett
     */
    public Map<String, Word> getWords() {
        return words;
    }

    /**
     * Gibt das Word-Objekt zurück, das mit dem angegebenen Wort verknüpft ist.
     *
     * @param word das abzurufende Wort
     * @return das Word-Objekt, das mit dem Wort verknüpft ist, oder null, wenn nicht gefunden
     */
    public Word getWord(String word) {
        return words.get(word);
    }

    /**
     * Entfernt das angegebene Wort sowohl aus der words- als auch aus der chosenWords-Map.
     *
     * @param word das zu entfernende Wort
     */
    public void removeWord(String word) {
        words.remove(word);
        chosenWords.remove(word);
    }

    public int getMaxWords(){
        return maxWords;
    }


    public int getMinWordAmount(){
        return minWordAmount;
    }

    /**
     * Gibt eine Liste der Farben zurück, die noch Wörter benötigen.
     *
     * @return eine Liste der fehlenden Farben
     */
    public List<ColorValue> getMissingColors() {
        List<ColorValue> missingColors = new ArrayList<>();
        for (Map.Entry<ColorValue, Integer> entry : colorLimits.entrySet()) {
            if (entry.getValue() > 0) {
                missingColors.add(entry.getKey());
            }
        }
        return missingColors;
    }
}
