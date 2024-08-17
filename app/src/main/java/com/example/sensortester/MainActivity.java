package com.example.sensortester;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends AppCompatActivity {
    AppCompatButton login_button,signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        login_button=findViewById(R.id.login_button);
        signup_button=findViewById(R.id.signup_button);
        login_button.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        });
        signup_button.setOnClickListener(v -> {
            Intent intent=new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
        });
    }
}