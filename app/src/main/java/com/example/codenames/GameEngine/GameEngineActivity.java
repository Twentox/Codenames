package com.example.codenames.GameEngine;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codenames.R;

import java.util.ArrayList;

public class GameEngineActivity extends AppCompatActivity {
    GameBoard gameBoard = new GameBoard();
    private int wordClickedCounter = 0;
    TextView collectedWordsCounter;

    ArrayList<Button> buttonArrayList = new ArrayList<>();
    String w = "Ball\nApfel\nBirne\nParika\nBaum\nHaus\nFlasche\nFanta\nHandy\nMaus\nTastaturr\nUni\nBett\nTisch\nBoden";
    ArrayList<String> hintWordList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gameBoard.ArrStrToWordList(gameBoard.databaseStrToArrStr(w));
        gameBoard.fillGameBoardGrid();

        collectedWordsCounter = findViewById(R.id.wordCounter);
        System.out.println(collectedWordsCounter.getText());
          fillGameBoardGridDetective();
          //fillGameBoardInvestigator();
    }


    public void fillGameBoardGridDetective(){
        GridLayout gameBoardGrid = findViewById(R.id.game_gridlayout);
        int totalColumns = 3;
        int totalRows = 5;
        // Sicherstellen, dass das GridLayout die richtige Anzahl von Zeilen und Spalten hat
        gameBoardGrid.setColumnCount(totalColumns);
        gameBoardGrid.setRowCount(totalRows);


        for (int i = 0; i < 15; i++) {
            Button button = new Button(this);
            buttonArrayList.add(button);
            button.setTag("unchecked");
            button.setText(gameBoard.words.get(i).getWord());
            button.setTextColor(Color.WHITE);
            if(gameBoard.words.get(i).getColor().getValues() == 1){
                button.setBackgroundColor(Color.parseColor("#4361ee"));
            } else if (gameBoard.words.get(i).getColor().getValues() == 2) {
                button.setBackgroundColor(Color.parseColor("#d62828"));
            } else if (gameBoard.words.get(i).getColor().getValues() == 3) {
                button.setBackgroundColor(Color.parseColor("#ffba08"));
            }else {
                button.setBackgroundColor(Color.parseColor("#000000"));
            }

            collectClickedWordsDetective(button);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(i / totalColumns); // Berechnet die Zeile
            params.columnSpec = GridLayout.spec(i % totalColumns); // Berechnet die Spalte
            params.setMargins(8, 8, 8, 8);
            // Button zum GridLayout hinzufügen mit LayoutParams
            params.width = 300;
            params.height = 125;

            gameBoardGrid.addView(button, params);
        }
    }

    public void fillGameBoardInvestigator(){
        GridLayout gameBoardGrid = findViewById(R.id.game_gridlayout);
        int totalColumns = 3;
        int totalRows = 5;
        // Sicherstellen, dass das GridLayout die richtige Anzahl von Zeilen und Spalten hat
        gameBoardGrid.setColumnCount(totalColumns);
        gameBoardGrid.setRowCount(totalRows);

        for(int i = 0; i < 15; i++){
            Button button = new Button(this);
            buttonArrayList.add(button);
            button.setTag("unchecked");
            button.setText(gameBoard.words.get(i).getWord());
            button.setTextColor(Color.WHITE);
            button.setBackgroundColor(Color.parseColor("#f2cc8f"));

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(i / totalColumns); // Berechnet die Zeile
            params.columnSpec = GridLayout.spec(i % totalColumns); // Berechnet die Spalte
            params.setMargins(8, 8, 8, 8);
            // Button zum GridLayout hinzufügen mit LayoutParams
            params.width = 300;
            params.height = 125;

            gameBoardGrid.addView(button, params);
        }
    }


    public void collectClickedWordsDetective(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button == null) {
                    System.out.println("Button ist null");
                    return;
                }

                String backgroundColorHex = getButtonBackgroundColorHex(button);
                if (backgroundColorHex == null) {
                    System.out.println("Hintergrundfarbe nicht verfügbar");
                    return;
                }

                String tag = (String) button.getTag();
                GradientDrawable border = new GradientDrawable();
                if ("unchecked".equals(tag)) {
                    border.setStroke(5, Color.WHITE);
                    button.setTag("checked");
                    wordClickedCounter++;
                } else if ("checked".equals(tag)) {
                    button.setTag("unchecked");
                    border.setStroke(0, Color.TRANSPARENT); // Randfarbe entfernen
                    wordClickedCounter--;
                }

                collectedWordsCounter.setText(String.valueOf(wordClickedCounter));
                addWordToGameBoard(backgroundColorHex, button.getText().toString());

                // Setze den Hintergrund mit dem ursprünglichen ColorDrawable und dem Rand
                Drawable originalBackground = button.getBackground();
                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{originalBackground, border});
                button.setBackground(layerDrawable);
            }
        });
    }

    private void addWordToGameBoard(String colorHex, String wordText) {
        ValueType valueType = null;
        switch (colorHex) {
            case "#000000":
                valueType = ValueType.BLACK;
                break;
            case "#FFBA08":
                valueType = ValueType.YELLOW;
                break;
            case "#4361EE":
                valueType = ValueType.BLUE;
                break;
            case "#D62828":
                valueType = ValueType.RED;
                break;
        }
        if (valueType != null && !gameBoard.choosenWords.contains(new Word(valueType, wordText))) {
            gameBoard.choosenWords.add(new Word(valueType, wordText));
        }
    }

    private String getButtonBackgroundColorHex(Button button) {
        Drawable background = button.getBackground();
        if (background instanceof ColorDrawable) {
            int color = ((ColorDrawable) background).getColor();
            return String.format("#%06X", (0xFFFFFF & color));
        }
        return null;
    }



    public void sendInformation(View view){
        for(Word word: gameBoard.choosenWords){
            System.out.println(word);
        }
    }




}