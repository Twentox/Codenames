package com.example.codenames.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.codenames.R;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void saveUserNameButton(View view) {
        EditText textField = findViewById(R.id.set_user_name);
        String userName = textField.getText().toString().trim(); // Trimmen, um Leerzeichen zu entfernen

        if ("undefined".equals(userName)) {
            Toast.makeText(this, "Name ist nicht valide!", Toast.LENGTH_SHORT).show();
        } else {
            saveUserName(this, userName);
            Intent intent = new Intent(UserInfoActivity.this, MenuActivity.class);
            startActivity(intent);
        }
    }

    public void saveUserName(Context context, String userName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", userName);
        editor.apply();
    }

}