package com.example.codenames;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {


    private Button create_game_btn;
    private Button join_game_btn;
    private Button game_rules_btn;
    private View main_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_activity);

        create_game_btn = findViewById(R.id.create_game_btn);
        join_game_btn = findViewById(R.id.join_game_btn);
        game_rules_btn = findViewById(R.id.game_rules_btn);
        main_layout = findViewById(R.id.main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void createGame(View view){
        main_layout.setVisibility(view.GONE);
        setContentView(R.layout.invite_players_activity);

    }
    public void joinGame(View view){
        main_layout.setVisibility(view.GONE);
        setContentView(R.layout.join_game_activity);
    }public void gameRules(View view){
        main_layout.setVisibility(view.GONE);
        setContentView(R.layout.game_rules_activity);
    }



}