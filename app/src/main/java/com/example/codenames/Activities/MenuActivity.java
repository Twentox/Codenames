package com.example.codenames.Activities;


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

import com.example.codenames.R;
import com.example.codenames.networking.*;

public class MenuActivity extends AppCompatActivity {

    String userName;
    private Button createGameBtn;
    private Button sichtbar;

    private Handler handler = new Handler();
    private Runnable checkUserNameRunnable;

    WiFiDirectHandler wiFiDirectHandler;
    ConnectionHandler connectionHandler = ConnectionHandler.getConnectionHandler();
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

        WiFiDirectHandler.initWiFiDirectHandler(this);
        wiFiDirectHandler = WiFiDirectHandler.getWiFiDirectHandler();

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


    @Override
    protected void onResume() {
        super.onResume();
        wiFiDirectHandler.registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wiFiDirectHandler.unregisterReceiver();
    }



    public void createGame(View view) {
        if(userName == "undefined"){
            Toast.makeText(this, "Setze ein Benutzername in den Einstellungen!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(MenuActivity.this, CreateConnectionActivity.class);
            startActivity(intent);
        }

    }

    public void gameRules(View view) {
        System.out.println(userName);
        if(userName == "undefined"){
            Toast.makeText(this, "Setze ein Benutzername in den Einstellungen!", Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(MenuActivity.this, GameEngineActivity.class);
            startActivity(intent);
        }
    }


    public void startPeerDiscovery(View view) {
        wiFiDirectHandler.discoverPeers();
        waitForConnection();
    }


    public void waitForConnection() {
        Thread t = new Thread(() -> {
            synchronized (connectionHandler.isConnected) {
                try {
                    connectionHandler.isConnected.wait();
                    waitForContinue();
                } catch (InterruptedException e) {}
            }
        });

        t.start();
    }

    public void waitForContinue() {
        Thread t = new Thread(() -> {
            if (connectionHandler.receiveContinue()) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(MenuActivity.this, ChooseTeamActivity.class);
                    startActivity(intent);
                });
            }
        });

        t.start();
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