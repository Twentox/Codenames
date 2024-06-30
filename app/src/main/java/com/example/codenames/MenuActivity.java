package com.example.codenames;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codenames.networking.*;

public class MenuActivity extends AppCompatActivity {
    WiFiDirectHandler wiFiDirectHandler;

    String userName;
    private Button createGameBtn;
    private Button sichtbar;

    private Handler handler = new Handler();
    private Runnable checkUserNameRunnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_activity);


        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        wiFiDirectHandler = new WiFiDirectHandler(this);

        createGameBtn = findViewById(R.id.create_game_btn);
        sichtbar = findViewById(R.id.fuer_ein_neues_Spiel_sichtbar_sein);

        checkUserNameRunnable = new Runnable() {
            @Override
            public void run() {
                checkUserName();
                handler.postDelayed(this, 1000); // Check every second
            }
        };

        // Start the periodic check
        handler.post(checkUserNameRunnable);

    }

    public void createGame(View view) {
        if(userName == "undefined"){
            Toast.makeText(this, "Setze ein Benutzername in den Einstellungen!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(MenuActivity.this, CreateP2PGroupActivity.class);
            startActivity(intent);
        }

    }

    /*
    public void startPeerDiscovery(View view) {
        wiFiDirectHandler.discoverPeers();
    }

     */
    public void gameRules(View view) {
        System.out.println(userName);
        if(userName == "undefined"){
            Toast.makeText(this, "Setze ein Benutzername in den Einstellungen!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(MenuActivity.this, GameEngineActivity.class);
            startActivity(intent);
        }
    }

    public void userSettings(View view){
        if(userName == "undefined"){
            Toast.makeText(this, "Setze ein Benutzername in den Einstellungen!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(MenuActivity.this, UserInfoActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the handler when the activity is destroyed to prevent memory leaks
        handler.removeCallbacks(checkUserNameRunnable);
    }


    public String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "undefined");
        return userName;
    }

    public void checkUserName() {
        userName = getUserName(this);
        TextView currentUserName = findViewById(R.id.current_userName);
        currentUserName.setText(userName);

        if (!"undefined".equals(userName)) {
            Log.d("MenuActivity", "Username is not undefined");
        } else {
            Log.d("MenuActivity", "Username is undefined");
        }
    }


}