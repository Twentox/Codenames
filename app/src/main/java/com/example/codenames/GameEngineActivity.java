package com.example.codenames;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.codenames.GameEngine.*;
import com.example.codenames.socketNetworkDataBase.*;
import com.example.codenames.services.postgresql.SQLStatements;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class GameEngineActivity extends AppCompatActivity {
    ArrayList<String> words = new ArrayList<String>();
    GameBoard gameBoard;

    private int wordClickedCounter = 0;
    TextView collectedWordsCounter;

    ExecutorService executorService = Executors.newCachedThreadPool();

    boolean isBoss = true;

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

        /*
        words.add("Ball");
        words.add("Apfel");
        words.add("Birne");
        words.add("Parika");
        words.add("Haus");
        words.add("Flasche");
        words.add("Fanta");
        words.add("Handy");
        words.add("Maus");
        words.add("Tastatur");
        words.add("Uni");
        words.add("Bett");
        words.add("Tisch");
        words.add("Boden");
        words.add("Lampe");
        words.add("Holz");
        words.add("Fenster");
        words.add("Batterie");

         */

        collectedWordsCounter = findViewById(R.id.wordCounter);


        Future<GameBoard> gameBoardFuture = executorService.submit(() ->{
            String databaseWords = getDatabaseWords();
            List<String> wordsList = removeNewlinesAndConvertToList(databaseWords);
            gameBoard = new GameBoard(21,wordsList);
            return gameBoard;
        });
        try {
            GameBoard gameBoard1 = gameBoardFuture.get();
            fillGameBoardGridDetective(gameBoard1);
        }catch (ExecutionException | InterruptedException err){
            System.out.println(err);
        }finally {
            executorService.shutdown();
        }

    }


    public static List<String> removeNewlinesAndConvertToList(String inputString) {
        List<String> resultList = new ArrayList<>();
        if (inputString != null && !inputString.isEmpty()) {
            String[] lines = inputString.split("\r?\n");
            for (String line : lines) {
                resultList.add(line);
            }
        }
        return resultList;
    }


    public String getDatabaseWords(){
        String res = "";
        try {
            Random random = new Random();
            int randomPosition = random.nextInt(2000) + 1 ;
            Client client = new ClientImpl("202.61.249.51", 50000);
            client.connect();

            DataOutputStream dos = client.getDataOutputStream();
            client.sendData(dos,SQLStatements.getValuesFromColumnAndTable("nomen", "words",randomPosition, randomPosition + 21), DataTypes.REQUEST.getType());
            String data = client.recieveData(client.getDataInputStream());
            if(data.equals(DataTypes.HEADER.getType())){
                String artifactID = client.recieveData(client.getDataInputStream());
                String version = client.recieveData(client.getDataInputStream());
                if(NetworkEngine.checkVersion(artifactID, version)){
                    String data2 = client.recieveData(client.getDataInputStream());

                    if(data2.equals(DataTypes.ANWSER.getType())){
                        String words = client.recieveData(client.getDataInputStream());
                        res = words;
                        client.disconnect();
                    }
                }
            }


        }catch (IllegalArgumentException | IOException err ){
            System.out.println(err);
        }
        return res;
    }



    public void fillGameBoardGridDetective(GameBoard gameBoard) {
        GridLayout gameBoardGrid = findViewById(R.id.game_gridlayout);
        int totalColumns = 3;
        int totalRows = 21 / totalColumns;


        gameBoardGrid.setColumnCount(totalColumns);
        gameBoardGrid.setRowCount(totalRows);

        int wordIndex = 0;
        for (Word word : gameBoard.words.values()) {
            Button button = new Button(this);
            button.setTag("unchecked");
            button.setText(word.getWord());
            button.setTextColor(Color.WHITE);
            if (word.getColorID().equals("blue")) {
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_backgroundcolor_blue));
            } else if (word.getColorID().equals("red")) {
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_backgroundcolor_red));
            } else if (word.getColorID().equals("yellow")) {
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_backgroundcolor_yellow));
            } else {
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_backgroundcolor_black));
            }

            collectClickedWordsDetective(button);

            // Berechne die Zeilen- und Spaltenposition für das aktuelle Wort
            int row = wordIndex / totalColumns;
            int col = wordIndex % totalColumns;
            wordIndex++;

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(row);
            params.columnSpec = GridLayout.spec(col);
            params.setMargins(8, 8, 8, 8);
            params.width = 300;
            params.height = 125;

            // Button zum GridLayout hinzufügen mit LayoutParams
            gameBoardGrid.addView(button, params);
        }
    }



    public void fillGameBoardInvestigator(){
        GridLayout gameBoardGrid = findViewById(R.id.game_gridlayout);
        int totalColumns = 3;
        int totalRows = gameBoard.getMaxWords() / 2;
        // Sicherstellen, dass das GridLayout die richtige Anzahl von Zeilen und Spalten hat
        gameBoardGrid.setColumnCount(totalColumns);
        gameBoardGrid.setRowCount(totalRows);

        for(int i = 0; i < gameBoard.getMaxWords(); i++){
            Button button = new Button(this);
            button.setTag("unchecked");
            button.setText(gameBoard.words.get(i).getWord());
            button.setTextColor(Color.WHITE);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.button_backgroundcolor_yellow));

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

    /*
    public void Methode_wechseln(View view) {
        if(isBoss){
            fillGameBoardGridDetective();
            isBoss = false;
        }else {
            fillGameBoardInvestigator();
            isBoss = true; 
        }
    }

     */


    public void collectClickedWordsDetective(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button == null) {
                    System.out.println("Button ist null");
                    return;
                }

                // Erhalte den aktuellen Hintergrund des Buttons als Drawable
                Drawable buttonBackground = button.getBackground();

                // Bestimme die aktuelle Farbe des Buttons


                // Hole den aktuellen Tag des Buttons
                String tag = (String) button.getTag();

                // Logik basierend auf dem aktuellen Tag des Buttons
                if ("unchecked".equals(tag)) {

                    if (buttonBackground.getConstantState().equals(ContextCompat.getDrawable(v.getContext(), R.drawable.button_backgroundcolor_blue).getConstantState())) {
                        // blue
                        button.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_border_blue));
                    } else if (buttonBackground.getConstantState().equals(ContextCompat.getDrawable(v.getContext(), R.drawable.button_backgroundcolor_red).getConstantState())) {
                        // red
                        button.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_border_red));
                    } else if (buttonBackground.getConstantState().equals(ContextCompat.getDrawable(v.getContext(), R.drawable.button_backgroundcolor_black).getConstantState())) {
                        // black
                        button.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_border_black));
                    } else {
                        // default case
                        button.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_border_yellow));
                    }
                    button.setTag("checked");

                    wordClickedCounter++;
                } else if ("checked".equals(tag)) {

                    if (buttonBackground.getConstantState().equals(ContextCompat.getDrawable(v.getContext(), R.drawable.button_border_blue).getConstantState())) {
                        // blue
                        button.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_backgroundcolor_blue));
                    } else if (buttonBackground.getConstantState().equals(ContextCompat.getDrawable(v.getContext(), R.drawable.button_border_red).getConstantState())) {
                        // red
                        button.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_backgroundcolor_red));
                    } else if (buttonBackground.getConstantState().equals(ContextCompat.getDrawable(v.getContext(), R.drawable.button_border_black).getConstantState())) {
                        // black
                        button.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_backgroundcolor_black));
                    } else {
                        // default case
                        button.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.button_backgroundcolor_yellow));
                    }

                    button.setTag("unchecked");

                    wordClickedCounter--;
                }

                // Setze den Text des Counter-Elements
                collectedWordsCounter.setText(String.valueOf(wordClickedCounter));
            }
        });
    }



    // TODO Button für wechseln des Feldes in Bezug auf Rolle
    // TODO Navigation durch die App machen
    // TODO Team Implementieren



}