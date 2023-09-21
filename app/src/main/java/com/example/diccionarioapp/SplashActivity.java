package com.example.diccionarioapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Configura un temporizador para pasar a la siguiente actividad después de la duración de la pantalla de inicio
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Crea un Intent para iniciar la siguiente actividad
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                // Cierra la actividad actual
                finish();
            }
        }, 2000);
    }
    }
