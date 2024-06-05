package com.example.codenames;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;

import com.example.codenames.GameEngine.GameEngineActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_activity);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void createGame(View view) {
        System.out.println("Die createGame() Methode wurde aufgerufen!");
        setContentView(R.layout.invite_players_activity);
    }

    public void joinGame(View view) {
        System.out.println("Die createGame() Methode wurde aufgerufen!");
        setContentView(R.layout.join_game_activity);
    }

    public void gameRules(View view) {
        Intent intent = new Intent(MainActivity.this, GameEngineActivity.class);
        startActivity(intent);
    }




}